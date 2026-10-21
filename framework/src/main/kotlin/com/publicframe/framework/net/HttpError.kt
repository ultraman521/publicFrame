package com.publicframe.framework.net

import com.google.gson.JsonParseException
import com.publicframe.framework.R
import com.publicframe.framework.PublicFrame
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 把网络/解析异常翻译成可直接展示给用户的文案。
 * 文案来自 :framework 的字符串资源（默认中文，附 values-en）。
 */
object HttpError {

    fun messageOf(t: Throwable): String {
        val context = PublicFrame.appContext
        return when (t) {
            is ApiException -> t.message

            is HttpException -> when (t.code()) {
                401 -> context.getString(R.string.frame_error_unauthorized)
                403 -> context.getString(R.string.frame_error_forbidden)
                404 -> context.getString(R.string.frame_error_not_found)
                in 500..599 -> context.getString(R.string.frame_error_server, t.code())
                else -> context.getString(R.string.frame_error_http, t.code())
            }

            is SocketTimeoutException -> context.getString(R.string.frame_error_timeout)

            is InterruptedIOException -> context.getString(R.string.frame_error_timeout)

            is UnknownHostException, is ConnectException ->
                context.getString(R.string.frame_error_network)

            is JsonParseException -> context.getString(R.string.frame_error_parse)

            else -> t.message?.takeIf { it.isNotBlank() }
                ?: context.getString(R.string.frame_error_unknown)
        }
    }
}
