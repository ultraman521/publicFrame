package com.publicframe.framework.utils

import com.publicframe.framework.PublicFrame

/** 常用尺寸换算。 */
object DensityUtils {

    @JvmStatic
    fun dp2px(dp: Float): Int =
        (dp * PublicFrame.appContext.resources.displayMetrics.density + 0.5f).toInt()

    @JvmStatic
    fun px2dp(px: Float): Int =
        (px / PublicFrame.appContext.resources.displayMetrics.density + 0.5f).toInt()

    @JvmStatic
    fun sp2px(sp: Float): Int =
        (sp * PublicFrame.appContext.resources.displayMetrics.scaledDensity + 0.5f).toInt()

    @JvmStatic
    fun screenWidthPx(): Int = PublicFrame.appContext.resources.displayMetrics.widthPixels

    @JvmStatic
    fun screenHeightPx(): Int = PublicFrame.appContext.resources.displayMetrics.heightPixels
}
