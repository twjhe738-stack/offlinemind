package com.offlinemind.app.domain.generation

enum class GenerationState {
    IDLE,
    PREPARING,
    STREAMING,
    COMPLETED,
    STOPPED,
    FAILED,
}

class GenerationStateMachine {
    var state: GenerationState = GenerationState.IDLE
        private set

    fun transitionTo(next: GenerationState) {
        check(next in allowedTransitions.getValue(state)) {
            "Invalid generation transition: $state -> $next"
        }
        state = next
    }

    private companion object {
        val allowedTransitions = mapOf(
            GenerationState.IDLE to setOf(GenerationState.PREPARING),
            GenerationState.PREPARING to setOf(
                GenerationState.STREAMING,
                GenerationState.STOPPED,
                GenerationState.FAILED,
            ),
            GenerationState.STREAMING to setOf(
                GenerationState.COMPLETED,
                GenerationState.STOPPED,
                GenerationState.FAILED,
            ),
            GenerationState.COMPLETED to setOf(GenerationState.IDLE, GenerationState.PREPARING),
            GenerationState.STOPPED to setOf(GenerationState.IDLE, GenerationState.PREPARING),
            GenerationState.FAILED to setOf(GenerationState.IDLE, GenerationState.PREPARING),
        )
    }
}
