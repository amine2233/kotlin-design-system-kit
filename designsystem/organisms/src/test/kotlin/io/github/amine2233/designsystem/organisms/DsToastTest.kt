package io.github.amine2233.designsystem.organisms

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class DsToastTest {
    private val first = DsToastMessage("Premier", id = "1")
    private val second = DsToastMessage("Deuxième", id = "2")
    private val third = DsToastMessage("Troisième", id = "3")

    private fun queueOf(vararg messages: DsToastMessage) = messages.fold(DsToastQueueState()) { queue, message -> queue.enqueue(message) }

    @Test
    fun `toasts come out in the order they were shown`() {
        val shown =
            generateSequence(queueOf(first, second, third)) { queue -> queue.current?.let { queue.dismiss(it.id) } }
                .mapNotNull { it.current?.id }
                .toList()
        assertEquals(listOf("1", "2", "3"), shown)
    }

    @Test
    fun `dismissing the current toast promotes the first pending one`() {
        val queue = queueOf(first, second, third).dismiss("1")
        assertEquals(second, queue.current)
        assertEquals(listOf(third), queue.pending)
    }

    @Test
    fun `dismissing the last toast empties the queue`() {
        val queue = queueOf(first).dismiss("1")
        assertNull(queue.current)
        assertEquals(emptyList<DsToastMessage>(), queue.pending)
    }

    @Test
    fun `showing a toast while one is visible queues it behind`() {
        val queue = queueOf(first, second)
        assertEquals(first, queue.current)
        assertEquals(listOf(second), queue.pending)
    }

    @Test
    fun `dismissing a pending toast removes only that one`() {
        val queue = queueOf(first, second, third).dismiss("2")
        assertEquals(first, queue.current)
        assertEquals(listOf(third), queue.pending)
    }

    @Test
    fun `dismissing an unknown id leaves the queue unchanged`() {
        val queue = queueOf(first, second)
        assertEquals(queue, queue.dismiss("inconnu"))
        assertEquals(DsToastQueueState(), DsToastQueueState().dismiss("1"))
    }

    @Test
    fun `showing an existing id replaces it in place`() {
        val updatedFirst = first.copy(message = "Premier modifié")
        val updatedSecond = second.copy(style = DsToastStyle.Error)

        val asCurrent = queueOf(first, second).enqueue(updatedFirst)
        assertEquals(updatedFirst, asCurrent.current)
        assertEquals(listOf(second), asCurrent.pending)

        val asPending = queueOf(first, second, third).enqueue(updatedSecond)
        assertEquals(first, asPending.current)
        assertEquals(listOf(updatedSecond, third), asPending.pending)
    }

    @Test
    fun `a positive duration is kept and anything else falls back to four seconds`() {
        assertEquals(4.seconds, DsToastDefaults.Duration)
        assertEquals(6.seconds, dsToastDismissDelay(6.seconds, isScreenReaderOn = false))
        assertEquals(4.seconds, dsToastDismissDelay(Duration.ZERO, isScreenReaderOn = false))
        assertEquals(4.seconds, dsToastDismissDelay((-3).seconds, isScreenReaderOn = false))
    }

    @Test
    fun `a screen reader gets at least ten seconds`() {
        assertEquals(10.seconds, dsToastDismissDelay(4.seconds, isScreenReaderOn = true))
        assertEquals(10.seconds, dsToastDismissDelay(Duration.ZERO, isScreenReaderOn = true))
        assertEquals(15.seconds, dsToastDismissDelay(15.seconds, isScreenReaderOn = true))
    }
}
