package com.publicframe.framework

/**
 * 脚手架全局配置。
 *
 * [apiBaseUrl] 可为空——纯本地应用不需要网络；配置时必须是
 * 以 `/` 结尾的绝对 HTTP(S) 地址（Retrofit 要求）。
 */
data class FrameworkConfiguration(
    val apiBaseUrl: String? = null,
    val enableLogging: Boolean = false,
    val enableCrashHandler: Boolean = true,
    val cacheSizeBytes: Long = 50L * 1024L * 1024L,
    val connectTimeoutMillis: Long = 15_000L,
    val readTimeoutMillis: Long = 15_000L,
    val commonHeaders: Map<String, String> = emptyMap()
) {
    init {
        require(cacheSizeBytes >= 0) { "cacheSizeBytes must not be negative" }
        require(connectTimeoutMillis > 0) { "connectTimeoutMillis must be positive" }
        require(readTimeoutMillis > 0) { "readTimeoutMillis must be positive" }
        apiBaseUrl?.let {
            require(it.startsWith("http://") || it.startsWith("https://")) {
                "apiBaseUrl must start with http:// or https://"
            }
            require(it.endsWith('/')) { "apiBaseUrl must end with '/' for Retrofit" }
        }
    }
}
