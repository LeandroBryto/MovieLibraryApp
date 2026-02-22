package com.leandro.movielibraryapp.util

import com.leandro.movielibraryapp.ui.detail.formatRuntime
import org.junit.Assert.assertEquals
import org.junit.Test

class FormatUtilsTest {
    @Test
    fun `formatRuntime deve retornar formato correto de horas e minutos`() {
        assertEquals("2h 5min", formatRuntime(125))
        assertEquals("1h 30min", formatRuntime(90))
        assertEquals("0 min", formatRuntime(0))
        assertEquals("0 min", formatRuntime(null))
    }
}