package io.github.amine2233.designsystem.molecules

import io.github.amine2233.designsystem.atoms.DsTagTone
import org.junit.Assert.assertEquals
import org.junit.Test

class DsDeviceStatusTest {
    @Test
    fun `connected is success`() {
        assertEquals(DsTagTone.Success, dsDeviceStatusTone(DsDeviceStatus.Connected))
    }

    @Test
    fun `connecting is info`() {
        assertEquals(DsTagTone.Info, dsDeviceStatusTone(DsDeviceStatus.Connecting))
    }

    @Test
    fun `offline is neutral`() {
        assertEquals(DsTagTone.Neutral, dsDeviceStatusTone(DsDeviceStatus.Offline))
    }

    @Test
    fun `attention is error`() {
        assertEquals(DsTagTone.Error, dsDeviceStatusTone(DsDeviceStatus.Attention))
    }

    @Test
    fun `every status has its own tone`() {
        assertEquals(
            DsDeviceStatus.entries.size,
            DsDeviceStatus.entries
                .map(::dsDeviceStatusTone)
                .toSet()
                .size,
        )
    }
}
