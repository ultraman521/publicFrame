package com.news.guagua.net

/** Centralized URL configuration used by the legacy Retrofit adapter. */
object UrlUtils {
    private var apiBaseUrl: String? = null
    private var imageBaseUrl: String = ""

    @JvmStatic
    fun getInstance(): UrlUtils = this

    @JvmStatic
    fun configure(apiBaseUrl: String, imageBaseUrl: String = "") {
        require(apiBaseUrl.startsWith("http://") || apiBaseUrl.startsWith("https://")) {
            "apiBaseUrl must start with http:// or https://"
        }
        require(apiBaseUrl.endsWith('/')) { "apiBaseUrl must end with '/' for Retrofit" }
        this.apiBaseUrl = apiBaseUrl
        this.imageBaseUrl = imageBaseUrl
    }

    @JvmStatic
    fun getUrl(): String = apiBaseUrl
        ?: error("Call UrlUtils.configure(apiBaseUrl) before creating the Retrofit client")

    @JvmStatic
    fun setUrl(random: Boolean) = Unit

    @JvmStatic
    fun sethost(host: String) = configure(host)

    @JvmStatic
    fun getImage_url(): String = imageBaseUrl

    @JvmStatic
    fun setImage_url(imageUrl: String) {
        imageBaseUrl = imageUrl
    }
}
