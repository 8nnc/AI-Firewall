package com.aifirewall.app.engine.network

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class TcpForwarderTest {

    @Test
    fun testTcpFlowKeyEquality() {
        val key1 = TcpForwarder.TcpFlowKey("10.0.0.1", 12345, "8.8.8.8", 443, false)
        val key2 = TcpForwarder.TcpFlowKey("10.0.0.1", 12345, "8.8.8.8", 443, false)
        val key3 = TcpForwarder.TcpFlowKey("10.0.0.1", 12346, "8.8.8.8", 443, false)

        assertEquals(key1, key2)
        assertEquals(key1.hashCode(), key2.hashCode())
        assertNotEquals(key1, key3)
    }

    @Test
    fun testTcpStateTransitions() {
        var state = TcpForwarder.TcpState.CLOSED
        assertEquals(TcpForwarder.TcpState.CLOSED, state)

        state = TcpForwarder.TcpState.SYN_SENT
        assertEquals(TcpForwarder.TcpState.SYN_SENT, state)

        state = TcpForwarder.TcpState.ESTABLISHED
        assertEquals(TcpForwarder.TcpState.ESTABLISHED, state)

        state = TcpForwarder.TcpState.CLOSE_WAIT
        assertEquals(TcpForwarder.TcpState.CLOSE_WAIT, state)

        state = TcpForwarder.TcpState.CLOSED
        assertEquals(TcpForwarder.TcpState.CLOSED, state)
    }
}
