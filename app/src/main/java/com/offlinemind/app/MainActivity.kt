package com.offlinemind.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val modelFile = File(filesDir, "model.bin")

        setContent {
            var prompt by remember { mutableStateOf("") }
            var response by remember { mutableStateOf("") }
            var isGenerating by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()
            val scrollState = rememberScrollState()

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(scrollState)
                ) {
                    Text(
                        text = "OfflineMind AI",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = prompt,
                        onValueChange = { prompt = it },
                        label = { Text("Ask your offline model...") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (modelFile.exists()) {
                                isGenerating = true
                                response = ""
                                val manager = LlmManager(this@MainActivity, modelFile.absolutePath)
                                scope.launch {
                                    try {
                                        manager.generateResponseStream(prompt).collect { token ->
                                            response += token
                                        }
                                    } catch (e: Exception) {
                                        response = "Error: ${e.localizedMessage}"
                                    } finally {
                                        isGenerating = false
                                    }
                                }
                            } else {
                                response = "Model file not found at: ${modelFile.absolutePath}\nPlease place a valid MediaPipe .bin model file there."
                            }
                        },
                        enabled = !isGenerating && prompt.isNotBlank(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isGenerating) "Generating Response..." else "Generate")
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Response:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = response.ifEmpty { "Responses will appear here..." },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
