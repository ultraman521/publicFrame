package com.news.guagua.utils;

/**
 * 动画名称
 */
public class ObjectAnimatorName {
    /*****透明****/
    public final static String alpha = "alpha";
    /**
     * 改变旋转度数 rotation、rotationX、rotationY
     * float rotationX：表示围绕 X 轴旋转，rotationX 表示旋转度数
     * float rotationY:表示围绕 Y 轴旋转，rotationY 表示旋转度数
     * float rotation:表示围绕 Z 旋转,rotation 表示旋转度数
     *
     * ObjectAnimator animator = ObjectAnimator.ofFloat(view,"rotationX",0,270,50);
     * animator.setDuration(2000);
     * animator.start();
     *
     */
    public final static String rotation = "rotation";
    public final static String rotationY = "rotationY";
    public final static String rotationX = "rotationX";
    /**
     * .移动 translationX、translationY
     * <p>
     * float translationX :表示在 X 轴上的平移距离,以当前控件为原点，向右为正方向，参数 translationX 表示移动的距离。
     * float translationY :表示在 Y 轴上的平移距离，以当前控件为原点，向下为正方向，参数 translationY 表示移动的距离。
     *
     * ObjectAnimator animator = ObjectAnimator.ofFloat(tv, "translationX", 0, 200, -200,0);
     * animator.setDuration(2000);
     * animator.start();
     *
     */
    public final static String translationX = "translationX";
    public final static String translationY = "translationY";
    /**
     * .缩放 scaleX、scaleY
     * <p>
     * float scaleX:在 X 轴上缩放，scaleX 表示缩放倍数
     * float scaleY:在 Y 轴上缩放，scaleY 表示缩放倍数
     */
    public final static String scaleX = "scaleX";
    public final static String scaleY = "scaleY";


}
