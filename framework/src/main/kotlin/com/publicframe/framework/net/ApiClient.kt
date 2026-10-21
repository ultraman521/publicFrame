package com.publicframe.framework.net

import com.google.gson.GsonBuilder
import com.publicframe.framework.FrameworkConfiguration
import com.publicframe.framework.PublicFrame
import com.publicframe.framework.log.FrameLog
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * Retrofit/OkHttp 统一构建入口，按 [FrameworkConfiguration] 惰性初始化。
 *
 * ```kotlin
 * val api = ApiClient.service<DemoApi>()
 * ```
 *
 * [service] 默认使用 `FrameworkConfiguration.apiBaseUrl`，也可按需传入
 * 其它 base URL（多服务场景），实例按 URL 缓存。
 */
object ApiClient {

    private const val TAG = "ApiClient"

    private val retrofitCache = ConcurrentHashMap<String, Retrofit>()

    private val gson by lazy {
        GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create()
    }

    val okHttpClient: OkHttpClient by lazy { buildClient() }

    fun retrofit(baseUrl: String? = null): Retrofit {
        val url = baseUrl
            ?: PublicFrame.configuration().apiBaseUrl
            ?: error("No base URL: pass one explicitly or set FrameworkConfiguration.apiBaseUrl")
        return retrofitCache.getOrPut(url) {
            Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
    }

    inline fun <reified T> service(baseUrl: String? = null): T = retrofit(baseUrl).create(T::class.java)

    private fun buildClient(): OkHttpClient {
        val config = PublicFrame.configuration()
        FrameLog.i(TAG, "building OkHttpClient: logging=${config.enableLogging}, cache=${config.cacheSizeBytes}B")

        return OkHttpClient.Builder()
            .apply {
                connectTimeout(config.connectTimeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS)
                readTimeout(config.readTimeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS)

                if (config.cacheSizeBytes > 0) {
                    val cacheDir = File(PublicFrame.requireApplication().cacheDir, "http_cache")
                    cache(Cache(cacheDir, config.cacheSizeBytes))
                }

                if (config.commonHeaders.isNotEmpty()) {
                    addInterceptor(CommonHeadersInterceptor(config.commonHeaders))
                }
                addInterceptor(DomainInterceptor())

                if (config.enableLogging) {
                    addInterceptor(
                        HttpLoggingInterceptor { message -> FrameLog.d("HTTP", message) }
                            .apply { level = HttpLoggingInterceptor.Level.BODY }
                    )
                }
            }
            .build()
    }
}

/** 为所有请求追加公共请求头（token、渠道号等）。 */
class CommonHeadersInterceptor(
    private val headers: Map<String, String>
) : okhttp3.Interceptor {
    override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response {
        val request = chain.request().newBuilder().apply {
            headers.forEach { (name, value) -> header(name, value) }
        }.build()
        return chain.proceed(request)
    }
}
