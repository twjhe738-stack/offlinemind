package com.offlinemind.app

import android.content.Context
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.File

class LlmManager(private val context: Context) {

    private val modelFile: File by lazy {
        val internalFile = File(context.filesDir, "model.bin")
        if (!internalFile.exists()) {
            // Try to copy from public Download folder if available
            val externalDownload = File("/sdcard/Download/model.bin")
            if (externalDownload.exists()) {
                externalDownload.copyTo(internalFile, overwrite = true)
            }
        }
        internalFile
    }

    fun generateResponseStream(prompt: String): Flow<String> = callbackFlow {
        if (!modelFile.exists()) {
            trySend("Model file not found at: ${modelFile.absolutePath}\nPlease place a valid MediaPipe .bin model file there or in /sdcard/Download/model.bin.")
            close()
            return@callbackFlow
        }

        val options = LlmInference.LlmInferenceOptions.builder()
            .setModelPath(modelFile.absolutePath)
            .setMaxTokens(512)
            .setResultListener { partialResult, done ->
                trySend(partialResult)
                if (done) close()
            }
            .build()

        val inference = LlmInference.createFromOptions(context, options)
        inference.generateResponseAsync(prompt)

        awaitClose {
            // Cleanup on flow cancellation
        }
    }
}
