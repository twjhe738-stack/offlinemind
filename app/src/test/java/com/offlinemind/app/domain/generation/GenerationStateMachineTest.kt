package com.offlinemind.app.domain.generation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class GenerationStateMachineTest {
    @Test
    fun generationCanCompleteAndStartAgain() {
        val machine = GenerationStateMachine()

        machine.transitionTo(GenerationState.PREPARING)
        machine.transitionTo(GenerationState.STREAMING)
        machine.transitionTo(GenerationState.COMPLETED)
        machine.transitionTo(GenerationState.PREPARING)

        assertEquals(GenerationState.PREPARING, machine.state)
    }

    @Test
    fun generationCanStopWhilePreparingOrStreaming() {
        val machine = GenerationStateMachine()

        machine.transitionTo(GenerationState.PREPARING)
        machine.transitionTo(GenerationState.STOPPED)
        machine.transitionTo(GenerationState.PREPARING)
        machine.transitionTo(GenerationState.STREAMING)
        machine.transitionTo(GenerationState.STOPPED)

        assertEquals(GenerationState.STOPPED, machine.state)
    }

    @Test
    fun invalidTransitionsAreRejected() {
        val machine = GenerationStateMachine()

        assertThrows(IllegalStateException::class.java) {
            machine.transitionTo(GenerationState.STREAMING)
        }
    }

    @Test
    fun failedGenerationCanBeRetried() {
        val machine = GenerationStateMachine()

        machine.transitionTo(GenerationState.PREPARING)
        machine.transitionTo(GenerationState.FAILED)
        machine.transitionTo(GenerationState.PREPARING)

        assertEquals(GenerationState.PREPARING, machine.state)
    }
}
