package com.publicframe.demo

import android.app.Application
import com.publicframe.framework.FrameworkConfiguration
import com.publicframe.framework.PublicFrame

class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PublicFrame.initialize(
            this,
            FrameworkConfiguration(
                // 示例接口：wanandroid 开放 API（正式项目替换成自己的服务地址）
                apiBaseUrl = "https://www.wanandroid.com/",
                // 发布版请置为 false（或接入 BuildConfig，需开启 buildConfig 特性）
                enableLogging = true,
                commonHeaders = mapOf(
                    "Accept-Language" to "zh-CN"
                )
            )
        )
    }
}
