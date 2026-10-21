package com.offlinemind.app

import android.content.Context
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LlmManager(private val context: Context, private val modelPath: String) {

    fun generateResponseStream(prompt: String): Flow<String> = callbackFlow {
        val options = LlmInference.LlmInferenceOptions.builder()
            .setModelPath(modelPath)
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
