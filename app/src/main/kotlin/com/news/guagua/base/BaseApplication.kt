package com.news.guagua.base

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.news.guagua.utils.UiUtils
import com.publicframe.framework.FrameworkConfiguration
import com.publicframe.framework.PublicFrame
import me.goldze.mvvmhabit.utils.Utils

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        UiUtils.init(this)
        Utils.init(this)
        me.goldze.mvvmhabit.base.BaseApplication.setApplication(this)
        PublicFrame.initialize(this, FrameworkConfiguration())
        Fresco.initialize(this)
    }
}
