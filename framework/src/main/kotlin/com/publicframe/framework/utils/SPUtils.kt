package com.publicframe.framework.utils

import android.content.Context
import android.content.SharedPreferences
import com.publicframe.framework.PublicFrame

/**
 * 轻量键值存储。简单配置场景够用；复杂数据请换 DataStore/Room。
 *
 * ```kotlin
 * val prefs = SPUtils.default()      // 文件名 public_frame
 * prefs.put("key", "value")
 * val v = prefs.getString("key", "")
 * ```
 */
class SPUtils(private val prefs: SharedPreferences) {

    fun put(key: String, value: String) = prefs.edit().putString(key, value).apply()

    fun put(key: String, value: Int) = prefs.edit().putInt(key, value).apply()

    fun put(key: String, value: Long) = prefs.edit().putLong(key, value).apply()

    fun put(key: String, value: Boolean) = prefs.edit().putBoolean(key, value).apply()

    fun put(key: String, value: Float) = prefs.edit().putFloat(key, value).apply()

    fun getString(key: String, default: String = ""): String = prefs.getString(key, default) ?: default

    fun getInt(key: String, default: Int = 0): Int = prefs.getInt(key, default)

    fun getLong(key: String, default: Long = 0L): Long = prefs.getLong(key, default)

    fun getBoolean(key: String, default: Boolean = false): Boolean = prefs.getBoolean(key, default)

    fun getFloat(key: String, default: Float = 0f): Float = prefs.getFloat(key, default)

    fun remove(key: String) = prefs.edit().remove(key).apply()

    fun clear() = prefs.edit().clear().apply()

    companion object {
        private const val DEFAULT_NAME = "public_frame"

        @JvmStatic
        fun default(): SPUtils = of(DEFAULT_NAME)

        @JvmStatic
        fun of(name: String): SPUtils =
            SPUtils(PublicFrame.appContext.getSharedPreferences(name, Context.MODE_PRIVATE))
    }
}
