package com.news.guagua.utils;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import com.news.guagua.utils.constant.RefreshStatus;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import me.goldze.mvvmhabit.base.AppManager;


/**
 * 初始化相关
 *
 * @author shirwee
 */
public class UiUtils {
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    /**
     * 做成全局静态的
     */
    private static Handler handler;

    private UiUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(@Nullable final Context context) {
        UiUtils.context = context.getApplicationContext();
//        UiUtils.handler = new Handler(){};
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) {
            return context;
        }
        throw new NullPointerException("u should init first");
    }

    public static Handler getHandler() {
        if (handler != null) {
            return handler;
        }
        throw new NullPointerException("u should init first");
    }

    // 开始执行一个任务
    public static void post(Runnable runnable) {
        handler.post(runnable);
    }

    // 延迟执行一个任务
    public static void postDelay(Runnable task, long delay) {
        handler.postDelayed(task, delay);
    }

    // 移除一个任务
    public static void remove(Runnable task) {
        handler.removeCallbacks(task);
    }

    /**
     * 得到resources对象
     *
     * @return
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 得到string.xml中的字符串
     *
     * @param resId
     * @return
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 得到string.xml中的字符串，带点位符
     */
    public static String getString(int id, Object... formatArgs) {
        return getResources().getString(id, formatArgs);
    }

    /**
     * 得到string.xml中和字符串数组
     *
     * @param resId
     * @return
     */
    public static String[] getStringArr(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 得到colors.xml中的颜色
     *
     * @param colorId
     * @return
     */
    public static int getColor(int colorId) {
        return getResources().getColor(colorId);
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources()
                    .getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 晃动动画
     *
     * @param counts 半秒钟晃动多少下
     * @return
     */
    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(500);
        return translateAnimation;
    }

    /**
     * textView颜色渐变
     *
     * @param string
     * @param startColor
     * @param endColor
     * @return
     */
    public static SpannableStringBuilder getRadiusGradientSpan(String string, int startColor, int endColor) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
        LinearGradientFontSpan span = new LinearGradientFontSpan(startColor, endColor);
        spannableStringBuilder.setSpan(span, 0, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;

    }

    /**
     * 创建一张渐变图片，支持韵脚。
     *
     * @param startColor 渐变开始色
     * @param endColor   渐变结束色
     * @param radius     圆角大小
     * @param centerX    渐变中心点 X 轴坐标
     * @param centerY    渐变中心点 Y 轴坐标
     * @return 返回所创建的渐变图片。
     */
    @TargetApi(16)
    public static GradientDrawable createCircleGradientDrawable(@ColorInt int startColor,
                                                                @ColorInt int endColor, int radius,
                                                                @FloatRange(from = 0f, to = 1f) float centerX,
                                                                @FloatRange(from = 0f, to = 1f) float centerY) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColors(new int[]{
                startColor,
                endColor
        });
        gradientDrawable.setGradientType(GradientDrawable.RECTANGLE);
//        gradientDrawable.set
//        gradientDrawable.setGradientRadius(radius);
        gradientDrawable.setGradientCenter(centerX, centerY);
        return gradientDrawable;
    }

    public static int dip2Px(float dpValue) {
        final float scale = UiUtils.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int dip2Px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * convert sp to its equivalent px
     * <p>
     * 将sp转换为px
     */
    public static int sp2px(float spValue) {
        final float fontScale = UiUtils.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * 获取版本
     *
     * @return
     */
    public static int getVersionCode() {
        PackageManager packageManager = UiUtils.getContext().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(UiUtils.getContext().getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

//    /**
//     * 请求错误，停止下拉刷新或上拉加载更多
//     *
//     * @param refreshLayout
//     * @return
//     */
//    public static void stopErrorRL(RefreshLayout refreshLayout, MultiStateView multiStateView) {
//        if (refreshLayout.isRefreshing()) {
//            refreshLayout.finishRefresh(false);
//            return;
//        }
//        if (refreshLayout.isLoading()) {
//            refreshLayout.finishLoadmore(false);
//            return;
//        }
//
//        multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
//    }
//
//    /**
//     * 请求成功，停止下拉刷新或上拉加载更多
//     *
//     * @param refreshLayout
//     * @return
//     */
//    public static void stopSuccessRL(RefreshLayout refreshLayout, MultiStateView multiStateView) {
//        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
//
//        if (refreshLayout.isRefreshing()) {
//            refreshLayout.finishRefresh(true);
//        }
//        if (refreshLayout.isLoading()) {
//            refreshLayout.finishLoadmore(true);
//        }
//    }

    /**
     * 请求成功，停止下拉刷新或上拉加载更多
     *
     * @param refreshLayout
     * @param refreshStatus 刷新的状态
     * @return
     */
    public static void stopRefreshLayout(SmartRefreshLayout refreshLayout, int refreshStatus) {
        if (refreshStatus == RefreshStatus.STOP_REFRESH_AND_LOADMORE) {
            refreshLayout.finishRefresh(true);
            refreshLayout.finishLoadMore(true);
        }

    }


    /**
     * 变亮dimBackground(0.4f,1.0f);
     * 变暗 dimBackground(1.0f,0.4f);
     *
     * @param from
     * @param to
     */
    public static void dimBackground(final float from, final float to) {
        final Window window = AppManager.getAppManager().currentActivity().getWindow();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.alpha = (Float) animation.getAnimatedValue();
                window.setAttributes(params);
            }
        });

        valueAnimator.start();
    }
}