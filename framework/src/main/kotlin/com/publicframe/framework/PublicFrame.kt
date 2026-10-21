package com.publicframe.framework

import android.app.Application

/**
 * Small, stable initialization seam for the reusable framework.
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
    }

    @JvmStatic
    fun isInitialized(): Boolean = application != null

    @JvmStatic
    fun requireApplication(): Application =
        application ?: error("PublicFrame.initialize(application) must be called first")

    @JvmStatic
    fun configuration(): FrameworkConfiguration = currentConfiguration
}
