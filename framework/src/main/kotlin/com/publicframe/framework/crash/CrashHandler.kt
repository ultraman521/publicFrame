package com.publicframe.framework.crash

import android.app.Application
import android.os.Build
import com.publicframe.framework.log.FrameLog
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 最小化崩溃记录：把未捕获异常写入 `filesDir/crash/` 下的文本文件，
 * 然后交回系统默认处理（进程照常终止，不拦截崩溃对话框）。
 */
internal object CrashHandler {

    @Volatile
    private var installed = false

    fun install(application: Application) {
        if (installed) return
        installed = true

        val previous = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            runCatching { writeToFile(application, thread, throwable) }
                .onFailure { FrameLog.w("CrashHandler", "failed to persist crash log", it) }
            previous?.uncaughtException(thread, throwable)
        }
    }

    private fun writeToFile(application: Application, thread: Thread, throwable: Throwable) {
        val dir = File(application.filesDir, "crash").apply { mkdirs() }
        val stamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val file = File(dir, "crash_$stamp.txt")

        file.printWriter().use { out ->
            out.appendLine("time    : ${Date()}")
            out.appendLine("thread  : ${thread.name}")
            out.appendLine("version : ${Build.VERSION.SDK_INT} (${Build.VERSION.RELEASE})")
            out.appendLine("model   : ${Build.MANUFACTURER} ${Build.MODEL}")
            out.appendLine()
            throwable.printStackTrace(out)
        }
    }
}
