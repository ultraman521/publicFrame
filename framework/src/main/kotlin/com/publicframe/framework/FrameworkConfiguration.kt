package com.publicframe.framework

/**
 * Configuration shared by applications that embed PublicFrame.
 *
 * The base URL is optional because an application may only use local modules.
 * When configured, it must be an absolute HTTP(S) URL ending with '/'.
 */
data class FrameworkConfiguration(
    val apiBaseUrl: String? = null,
    val enableLogging: Boolean = false,
    val cacheSizeBytes: Long = 50L * 1024L * 1024L
) {
    init {
        require(cacheSizeBytes >= 0) { "cacheSizeBytes must not be negative" }
        apiBaseUrl?.let {
            require(it.startsWith("http://") || it.startsWith("https://")) {
                "apiBaseUrl must start with http:// or https://"
            }
            require(it.endsWith('/')) { "apiBaseUrl must end with '/' for Retrofit" }
        }
    }
}
