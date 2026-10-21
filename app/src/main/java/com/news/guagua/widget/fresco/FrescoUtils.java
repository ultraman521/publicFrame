package com.news.guagua.widget.fresco;

import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.Phoenix;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.news.guagua.net.UrlUtils;

import java.io.File;

/**
 * Fresco工具类
 * “压缩图片”
 * Created by Administrator on 2016/12/14.
 */
public class FrescoUtils {
    FrescoUtils mFrescoUtils;

    public FrescoUtils() {
    }

    public static FrescoUtils getInstance() {
        return FrescoUtilsHolder.mFrescoUtils;
    }

    public static class FrescoUtilsHolder {
        private static final FrescoUtils mFrescoUtils = new FrescoUtils();
    }


    /**
     * 加载本地图片
     *
     * @param simpleDraweeView
     * @param file
     */
    public void DisPlaySizeImage(SimpleDraweeView simpleDraweeView, File file) {
        Uri uri = Uri.fromFile(file);
        if (uri == null) return;
        simpleDraweeView.setImageURI(uri);
    }


    public void DisPlaySizeImage(SimpleDraweeView simpleDraweeView, int resId) {
        Phoenix.with(simpleDraweeView).load(resId);
    }

    /**
     * 加载一般的图片
     *
     * @param simpleDraweeView
     * @param ImageUrl
     */
    public void DisPlaySizeImage(SimpleDraweeView simpleDraweeView, String ImageUrl) {
        ImageRequest request;
        if (ImageUrl.contains("http")) {
            request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(ImageUrl))
                    .setResizeOptions(new ResizeOptions(200, 200))
                    .build();
        } else {
            request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(UrlUtils.getInstance().getImage_url() + ImageUrl))
                    .setResizeOptions(new ResizeOptions(200, 200))
                    .build();
        }

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(simpleDraweeView.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        simpleDraweeView.setController(controller);
    }


    /**
     * 暂停加载
     */
    public void pause() {
        Fresco.getImagePipeline().pause();
    }

    /**
     * 开始加载
     */
    public void resume() {
        Fresco.getImagePipeline().resume();
    }

    /**
     * 清除缓存
     */
    public void clearMemoryCaches() {
        Fresco.getImagePipeline().clearMemoryCaches();
    }
}
