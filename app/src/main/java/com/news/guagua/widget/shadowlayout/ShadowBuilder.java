package com.news.guagua.widget.shadowlayout;

import com.news.guagua.widget.shadowlayout.shadowcolor.ShadowColors;

import me.goldze.mvvmhabit.base.AppManager;

public class ShadowBuilder {
//    QMUILinearLayout qmuiLinearLayout;
//    int radius = 5;
//    int shadowElevation = 15;
//    float shadowAlpha = 0.5f;
//    int shadowColor = Color.parseColor("#eeeeee");

    public ShadowBuilder(Builder mBuilder) {
        mBuilder.qmuiLinearLayout.setRadiusAndShadow(
                QMUIDisplayHelper.dp2px(AppManager.getAppManager().currentActivity(), mBuilder.radius),
                QMUIDisplayHelper.dp2px(AppManager.getAppManager().currentActivity(), mBuilder.shadowElevation),
                mBuilder.shadowAlpha);
        mBuilder.qmuiLinearLayout.setShadowColor(mBuilder.shadowColor);
    }

    public static class Builder {
        QMUILinearLayout qmuiLinearLayout;
        int radius = 0;
        int shadowElevation = 3;
        float shadowAlpha = 0.5f;
        int shadowColor = ShadowColors.black;


        public Builder setRadius(int radius) {
            this.radius = radius;
            return this;
        }

        public Builder setShadowElevation(int shadowElevation) {
            this.shadowElevation = shadowElevation;
            return this;
        }

        public Builder setShadowAlpha(float shadowAlpha) {
            this.shadowAlpha = shadowAlpha;
            return this;
        }

        public Builder setShadowColor(int shadowColor) {
            this.shadowColor = shadowColor;
            return this;
        }

        //返回外部对象
        public ShadowBuilder build(QMUILinearLayout qmuiLinearLayout) {
            this.qmuiLinearLayout = qmuiLinearLayout;
            return new ShadowBuilder(this);
        }
    }
}
