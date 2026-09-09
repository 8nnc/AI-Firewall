package com.aifirewall.app.engine.network

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class UdpForwarderTest {

    @Test
    fun testUdpFlowKeyUniqueness() {
        val key1 = UdpForwarder.UdpFlowKey("192.168.1.100", 54321, "1.1.1.1", 53, false)
        val key2 = UdpForwarder.UdpFlowKey("192.168.1.100", 54321, "1.1.1.1", 53, false)
        val key3 = UdpForwarder.UdpFlowKey("192.168.1.100", 54322, "1.1.1.1", 53, false)

        assertEquals(key1, key2)
        assertEquals(key1.hashCode(), key2.hashCode())
        assertNotEquals(key1, key3)
    }
}
