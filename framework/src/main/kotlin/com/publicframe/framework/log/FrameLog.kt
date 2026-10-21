package com.publicframe.framework.log

import android.util.Log
import com.publicframe.framework.PublicFrame

/**
 * 统一日志门面，受 [com.publicframe.framework.FrameworkConfiguration.enableLogging]
 * 控制：关闭时所有调用都是空操作，宿主无需自行判断 BuildConfig.DEBUG。
 */
object FrameLog {

    private const val DEFAULT_TAG = "PublicFrame"

    private val enabled: Boolean
        get() = PublicFrame.isInitialized() && PublicFrame.configuration().enableLogging

    fun d(tag: String = DEFAULT_TAG, message: String) {
        if (enabled) Log.d(tag, message)
    }

    fun i(tag: String = DEFAULT_TAG, message: String) {
        if (enabled) Log.i(tag, message)
    }

    fun w(tag: String = DEFAULT_TAG, message: String, tr: Throwable? = null) {
        if (enabled) Log.w(tag, message, tr)
    }

    fun e(tag: String = DEFAULT_TAG, message: String, tr: Throwable? = null) {
        if (enabled) Log.e(tag, message, tr)
    }
}
