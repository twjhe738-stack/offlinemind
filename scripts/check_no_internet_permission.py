#!/usr/bin/env python3
"""Fail if the app source, merged manifests, or packaged APK request INTERNET."""

import argparse
import os
import shutil
import subprocess
import sys
import xml.etree.ElementTree as ET
from pathlib import Path


PERMISSION = "android.permission.INTERNET"
ANDROID_NAME = "{http://schemas.android.com/apk/res/android}name"
ROOT = Path(__file__).resolve().parents[1]
SOURCE_MANIFEST = ROOT / "app/src/main/AndroidManifest.xml"
MERGED_MANIFESTS = ROOT / "app/build/intermediates/merged_manifest"


def manifest_has_internet(path: Path) -> bool:
    root = ET.parse(path).getroot()
    return any(
        element.tag in {"uses-permission", "uses-permission-sdk-23"}
        and element.get(ANDROID_NAME) == PERMISSION
        for element in root.iter()
    )


def check_manifest(path: Path) -> bool:
    if manifest_has_internet(path):
        print(f"FAIL: {PERMISSION} found in {path}", file=sys.stderr)
        return False
    print(f"PASS: no INTERNET permission in {path}")
    return True


def find_aapt(explicit: str | None) -> str | None:
    if explicit:
        return explicit
    found = shutil.which("aapt")
    if found:
        return found
    sdk_root = os.environ.get("ANDROID_SDK_ROOT") or os.environ.get("ANDROID_HOME")
    if sdk_root:
        build_tools = Path(sdk_root) / "build-tools"
        candidates = sorted(build_tools.glob("*/aapt"), reverse=True)
        if candidates:
            return str(candidates[0])
    return None


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--apk", type=Path, help="Packaged APK to inspect")
    parser.add_argument("--aapt", help="Path to Android SDK aapt executable")
    parser.add_argument(
        "--source-only",
        action="store_true",
        help="Check only the source manifest (not a release gate)",
    )
    args = parser.parse_args()

    ok = check_manifest(SOURCE_MANIFEST)
    if args.source_only:
        return 0 if ok else 1

    manifests = sorted(MERGED_MANIFESTS.rglob("AndroidManifest.xml"))
    if not manifests:
        print(
            f"FAIL: no merged manifest found under {MERGED_MANIFESTS}",
            file=sys.stderr,
        )
        return 1
    for manifest in manifests:
        ok = check_manifest(manifest) and ok

    if not args.apk or not args.apk.is_file():
        print("FAIL: provide an existing packaged APK with --apk", file=sys.stderr)
        return 1

    aapt = find_aapt(args.aapt)
    if not aapt:
        print(
            "FAIL: Android SDK aapt was not found; APK permission was not checked",
            file=sys.stderr,
        )
        return 1

    result = subprocess.run(
        [aapt, "dump", "permissions", str(args.apk)],
        check=False,
        capture_output=True,
        text=True,
    )
    if result.returncode != 0:
        print(f"FAIL: aapt could not inspect APK: {result.stderr.strip()}", file=sys.stderr)
        return 1
    if PERMISSION in result.stdout:
        print(f"FAIL: {PERMISSION} found in {args.apk}", file=sys.stderr)
        ok = False
    else:
        print(f"PASS: no INTERNET permission in {args.apk}")
    return 0 if ok else 1


if __name__ == "__main__":
    raise SystemExit(main())
