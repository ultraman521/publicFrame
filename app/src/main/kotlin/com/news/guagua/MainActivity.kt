package com.news.guagua

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.news.guagua.utils.ToastUtils
import com.publicframe.framework.PublicFrame

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val status = if (PublicFrame.isInitialized()) "ready" else "not initialized"
        ToastUtils.show("PublicFrame $status")
    }
}
