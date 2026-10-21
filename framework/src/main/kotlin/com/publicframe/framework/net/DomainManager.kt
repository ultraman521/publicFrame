package com.publicframe.framework.net

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * 运行时域名切换（替代旧项目的 retrofit-url-manager）。
 *
 * 设定后，除 [excludeHosts] 外的所有请求都会被 [DomainInterceptor]
 * 重写到该域名的 scheme/host/port，路径保持不变。
 *
 * ```kotlin
 * DomainManager.set("https://api.example.com/")   // 切换
 * DomainManager.set(null)                          // 恢复默认
 * ```
 */
object DomainManager {

    @Volatile
    private var override: HttpUrl? = null

    /** 不参与切换的域名（如统计、OSS），切换自身无效。 */
    val excludeHosts: MutableSet<String> = hashSetOf()

    fun set(url: String?) {
        override = url?.let {
            require(it.startsWith("http://") || it.startsWith("https://")) {
                "domain must start with http:// or https://"
            }
            it.toHttpUrl()
        }
    }

    fun current(): HttpUrl? = override

    internal fun rewriteIfNeeded(url: HttpUrl): HttpUrl? {
        val target = override ?: return null
        if (url.host == target.host) return null
        if (url.host in excludeHosts || target.host in excludeHosts) return null
        return url.newBuilder()
            .scheme(target.scheme)
            .host(target.host)
            .port(target.port)
            .build()
    }
}

/**
 * 挂在 [OkHttpClient] 上的域名重写拦截器，[ApiClient] 已默认安装。
 */
class DomainInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request: Request = chain.request()
        val rewritten = DomainManager.rewriteIfNeeded(request.url)
        val finalRequest = if (rewritten != null) {
            request.newBuilder().url(rewritten).build()
        } else {
            request
        }
        return chain.proceed(finalRequest)
    }
}
