package com.offlinemind.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OfflineMindApp()
        }
    }
}

@Composable
private fun OfflineMindApp() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(PaddingValues(horizontal = 24.dp, vertical = 32.dp)),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge,
                )
                Text(
                    text = stringResource(R.string.app_tagline),
                    style = MaterialTheme.typography.bodyLarge,
                )
                StatusCard(
                    title = stringResource(R.string.foundation_title),
                    description = stringResource(R.string.foundation_description),
                )
                StatusCard(
                    title = stringResource(R.string.model_status_title),
                    status = stringResource(R.string.model_status_empty),
                    description = stringResource(R.string.model_status_description),
                )
                StatusCard(
                    title = stringResource(R.string.privacy_title),
                    description = stringResource(R.string.privacy_description),
                )
                Text(
                    text = stringResource(R.string.roadmap_hint),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun StatusCard(
    title: String,
    description: String,
    status: String? = null,
) {
    Card {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            status?.let {
                Text(text = it, style = MaterialTheme.typography.bodyLarge)
            }
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
