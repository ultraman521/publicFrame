package com.publicframe.framework

import android.app.Application
import android.content.Context
import com.publicframe.framework.crash.CrashHandler

/**
 * 单例初始化入口。宿主 App 在 `Application.onCreate` 中调用：
 *
 * ```kotlin
 * PublicFrame.initialize(this, FrameworkConfiguration(apiBaseUrl = "https://api.example.com/"))
 * ```
 */
object PublicFrame {

    @Volatile
    private var application: Application? = null

    @Volatile
    private var currentConfiguration: FrameworkConfiguration = FrameworkConfiguration()

    @JvmStatic
    @JvmOverloads
    fun initialize(
        application: Application,
        configuration: FrameworkConfiguration = FrameworkConfiguration()
    ) {
        synchronized(this) {
            this.application = application
            currentConfiguration = configuration
        }
        if (configuration.enableCrashHandler) {
            CrashHandler.install(application)
        }
    }

    @JvmStatic
    fun isInitialized(): Boolean = application != null

    @JvmStatic
    fun requireApplication(): Application =
        application ?: error("PublicFrame.initialize(application) must be called first")

    @JvmStatic
    fun configuration(): FrameworkConfiguration = currentConfiguration

    /** 全局 Application Context，未初始化时抛出 [IllegalStateException]。 */
    val appContext: Context
        get() = requireApplication()
}
