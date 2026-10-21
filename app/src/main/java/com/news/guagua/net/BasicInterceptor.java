package com.news.guagua.net;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import me.goldze.mvvmhabit.utils.KLog;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;


/**
 * 添加公共参数
 */

public class BasicInterceptor implements Interceptor {
    //
    public static String TAG = "LogInterceptor";
    private boolean isLast = false;

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        Request.Builder newRequestBuild = null;
        String method = oldRequest.method();
        String postBodyString = "";
        if ("POST".equals(method)) {
            RequestBody oldBody = oldRequest.body();
            if (oldBody instanceof FormBody) {
                FormBody formBody = (FormBody) oldRequest.body();

                FormBody.Builder builder = new FormBody.Builder();
                Map<String, Object> param = new HashMap<>();
                for (int i = 0; i < formBody.size(); i++) {
                    //@FieldMap 注解 Map元素中 key 与 value 皆不能为null,否则会出现NullPointerException
                    if (formBody.value(i) != null) {
                        builder.add(formBody.name(i), formBody.value(i));
                        param.put(formBody.name(i), formBody.value(i));
                    }
                }

                RequestBody requestBody = oldRequest.body();
                if (requestBody != null) {
                    Buffer buffer = new Buffer();
                    requestBody.writeTo(buffer);
                    Charset charset = Charset.forName("UTF-8");
                    MediaType contentType = requestBody.contentType();
                    if (contentType != null) {
                        charset = contentType.charset(charset);
                    }
                }

                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                for (int i = 0; i < formBody.size(); i++) {
                    //@FieldMap 注解 Map元素中 key 与 value 皆不能为null,否则会出现NullPointerException
                    if (formBody.value(i) != null) {
                        formBodyBuilder.add(formBody.name(i), formBody.value(i));
                    }
                }
//                formBodyBuilder.add("sign", str);//KennyTest  str
                newRequestBuild = oldRequest.newBuilder();

                RequestBody formBody2 = formBodyBuilder.build();
                postBodyString = bodyToString(oldRequest.body());
//                postBodyString += ((postBodyString.length() > 0) ? "&" : "") + bodyToString(formBody2);
                newRequestBuild.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), bodyToString(formBody2)));
            } else if (oldBody instanceof MultipartBody) {
                MultipartBody oldBodyMultipart = (MultipartBody) oldBody;
                List<MultipartBody.Part> oldPartList = oldBodyMultipart.parts();
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                String _time = String.valueOf(System.currentTimeMillis() / 1000);
                RequestBody requestBody1 = RequestBody.create(MediaType.parse("text/plain"), _time);
//                RequestBody requestBody2 = RequestBody.create(MediaType.parse("text/plain"), MD5.getMessageDigest(new StringBuilder().append(_time).append(
//                        Constant.KEY).toString()));
//                RequestBody requestBody3 = RequestBody.create(MediaType.parse("text/plain"), GetLoginData.getOnlyToken());
                Map<String, Object> param = new HashMap<>();
                String fileName = "";
                for (MultipartBody.Part part : oldPartList) {


                    if (part.body().contentType() != null) {
                        builder.addPart(part);
                        postBodyString += (bodyToString(part.body()) + "\n");
                        Headers headers = part.headers();
                        KLog.i(headers.toString());
                        headers.names().size();
                        fileName = headers.toString();
                        fileName = fileName.substring(fileName.lastIndexOf("=") + 2, fileName.length() - 2);
                        fileName.replace("\"", "");
                        if (part.body().contentType().type().equals("image")) {
                        }
//                            Headers headers=part.headers();
//                            for (int i = 0; i <headers.names().size() ; i++) {
//                                Log.d("ParameterInterceptor","headers======value="+headers.value(i));
//                                String value=headers.value(i);//valueform-data; name="article_type"
//                                String replaceValue="form-data; name=";//这段在MultipartBody.Part源码中看到
//                                if(value.contains(replaceValue)){
//                                    String key=value.replace(replaceValue,"").replaceAll("\"","");;
//                                    Log.d("ParameterInterceptor","MultipartBody======key="+key);
//                                    param.put(key,bodyToString(part.body()));
//                                    builder.addPart(part);
//                                    break;
//                                }
//                            }
//                        }
                    }
                }
                postBodyString += (bodyToString(requestBody1) + "\n");
//                postBodyString += (bodyToString(requestBody2) + "\n");
//                postBodyString += (bodyToString(requestBody3) + "\n");
//              builder.addPart(oldBody);  //不能用这个方法，因为不知道oldBody的类型，可能是PartMap过来的，也可能是多个Part过来的，所以需要重新逐个加载进去
              /*  builder.addPart(requestBody1);
                builder.addPart(requestBody2);*/
                final File file = new File(Environment.getExternalStorageDirectory() + "/qimeng/image/" + fileName);
                RequestBody requestBody2 = RequestBody.create(MediaType.parse("image/jpeg"), file);
                builder.addPart(requestBody2);
//                builder.addFormDataPart("userid", GetLoginData.getUserId());
//                builder.addFormDataPart("sign", md);
//                builder.addFormDataPart("sign", MD5.getMessageDigest(new StringBuilder().append(_time).append(
//                        Constant.KEY).toString()));
//                builder.addFormDataPart("only_token", GetLoginData.getOnlyToken());

                newRequestBuild = oldRequest.newBuilder();
                newRequestBuild.post(builder.build());
                Log.e(TAG, "MultipartBody," + oldRequest.url());
            } else {
                newRequestBuild = oldRequest.newBuilder();
            }
        } else {

            String _time = String.valueOf(System.currentTimeMillis() / 1000);
            // 添加新的参数
            HttpUrl.Builder commonParamsUrlBuilder = oldRequest.url()
                    .newBuilder()
                    .scheme(oldRequest.url().scheme())
                    .host(oldRequest.url().host())
                    .addQueryParameter("_time", _time);
//                    .addQueryParameter("sign", MD5.getMessageDigest(new StringBuilder().append(_time).append(
//                            Constant.KEY).toString()))
//                    .addQueryParameter("only_token", GetLoginData.getOnlyToken());
            newRequestBuild = oldRequest.newBuilder()
                    .method(oldRequest.method(), oldRequest.body())
                    .url(commonParamsUrlBuilder.build());
        }
        Request newRequest = newRequestBuild
                .addHeader("Accept", "application/json")
                .addHeader("Accept-Language", "zh")
                .build();

        long startTime = System.currentTimeMillis();
        okhttp3.Response response = chain.proceed(newRequest);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        int httpStatus = response.code();
        StringBuilder logSB = new StringBuilder();

        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, Object> sortMap = new TreeMap<String, Object>(new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;

    }

    static class MapKeyComparator implements Comparator<String> {

        @Override
        public int compare(String str1, String str2) {

            return str1.compareTo(str2);
        }
    }
}