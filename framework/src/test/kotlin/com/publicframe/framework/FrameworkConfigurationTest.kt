package com.publicframe.framework

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class FrameworkConfigurationTest {

    @Test
    fun `default configuration is valid`() {
        val config = FrameworkConfiguration()
        assertEquals(null, config.apiBaseUrl)
        assertEquals(false, config.enableLogging)
        assertEquals(50L * 1024L * 1024L, config.cacheSizeBytes)
    }

    @Test
    fun `valid api baseUrl is accepted`() {
        val config = FrameworkConfiguration(apiBaseUrl = "https://api.example.com/")
        assertEquals("https://api.example.com/", config.apiBaseUrl)
    }

    @Test
    fun `api baseUrl without trailing slash is rejected`() {
        assertThrows(IllegalArgumentException::class.java) {
            FrameworkConfiguration(apiBaseUrl = "https://api.example.com")
        }
    }

    @Test
    fun `api baseUrl with unsupported scheme is rejected`() {
        assertThrows(IllegalArgumentException::class.java) {
            FrameworkConfiguration(apiBaseUrl = "ftp://api.example.com/")
        }
    }

    @Test
    fun `negative cache size is rejected`() {
        assertThrows(IllegalArgumentException::class.java) {
            FrameworkConfiguration(cacheSizeBytes = -1)
        }
    }

    @Test
    fun `non positive timeouts are rejected`() {
        assertThrows(IllegalArgumentException::class.java) {
            FrameworkConfiguration(connectTimeoutMillis = 0)
        }
        assertThrows(IllegalArgumentException::class.java) {
            FrameworkConfiguration(readTimeoutMillis = -1)
        }
    }
}
