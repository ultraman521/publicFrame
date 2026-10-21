package com.publicframe.framework.utils

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.publicframe.framework.PublicFrame

/**
 * 主线程安全的 Toast 工具，后一条会立刻替换前一条（防连点队列堆积）。
 */
object ToastUtils {

    @Volatile
    private var lastToast: Toast? = null

    private val mainHandler = Handler(Looper.getMainLooper())

    @JvmStatic
    fun show(message: String) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            doShow(message)
        } else {
            mainHandler.post { doShow(message) }
        }
    }

    @JvmStatic
    fun show(resId: Int) = show(PublicFrame.appContext.getString(resId))

    private fun doShow(message: String) {
        lastToast?.cancel()
        lastToast = Toast.makeText(PublicFrame.appContext, message, Toast.LENGTH_SHORT).also {
            it.show()
        }
    }
}
