package com.news.guagua.net;

import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.news.guagua.R;
import com.news.guagua.net.dialog.LoadDialog;
import com.news.guagua.utils.ToastUtils;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.base.AppManager;
import me.goldze.mvvmhabit.utils.KLog;
import retrofit2.HttpException;


/**
 * Created by zhpan on 2017/4/18.
 */

public abstract class NewDefaultObserver<T> implements Observer<T> {
    //    private Activity activity;
    //  Activity 是否在执行onStop()时取消订阅
    private boolean isAddInStop = false;
    //    private MultiStateView multiStateView;
    private int pageIndex;

    public NewDefaultObserver() {
    }
//    public DefaultObserver(Activity activity) {
//        this.activity = activity;
//    }

    public NewDefaultObserver(boolean isShowLoading) {
//        this.activity = activity;
        if (isShowLoading) {
            if (AppManager.getAppManager().currentActivity() != null) {
                LoadDialog.show(AppManager.getAppManager().currentActivity(), "加载中...");
            }

        }
    }

    public NewDefaultObserver(String meesage) {
        if (AppManager.getAppManager().currentActivity() != null) {
            LoadDialog.show(AppManager.getAppManager().currentActivity(), meesage);
        }

    }

//    public DefaultObserver(Activity activity, MultiStateView multiStateView, int pageIndex) {
//        this.activity = activity;
//        this.multiStateView = multiStateView;
//        this.pageIndex = pageIndex;
//    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T response) {
        dismissProgress();
//        if (response.getToken_flag() == 2){
//            ToastUtils.normal("检测到其他设备登陆此账号，请重新登陆");
//            AppUtils.jumpLogin(UiUtils.getContext());
//            Intent intent = new Intent(activity, LoginActivity.class);
//            intent.putExtra("offline","offline");
//            SharePreUtils.getInstance().put("loginInfo","");
//            Single.getInstants().setmLoginInfo(null);
//            activity.startActivity(intent);
//            ActivityStack.getInstance().finishOthersActivity(LoginActivity.class);
//        }else {
        BasicResponse r = (BasicResponse) response;
        if (r.getCode() == Result.OK) {
            onSuccess(response);
        } else {
            String message = r.getMsg();
            if (TextUtils.isEmpty(message)) {
                ToastUtils.show(R.string.response_return_error);
            } else {
//                ToastUtils.normal(message);
            }
            onFail(new Exception(message));
        }
//        }

    }

    private void dismissProgress() {
        if (AppManager.getAppManager().currentActivity() != null) {
            LoadDialog.dismiss(AppManager.getAppManager().currentActivity());
        }

    }

    @Override
    public void onError(Throwable e) {
        KLog.e(e.getMessage());
        onFail(e);
        dismissProgress();
        if (e instanceof HttpException) {     //   HTTP错误
            onException(ExceptionReason.BAD_NETWORK);
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {   //   连接错误
            onException(ExceptionReason.CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {   //  连接超时
            onException(ExceptionReason.CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {   //  解析错误
            onException(ExceptionReason.PARSE_ERROR);
        } else {
            onException(ExceptionReason.UNKNOWN_ERROR);
        }
    }

    @Override
    public void onComplete() {
    }

    /**
     * 请求成功
     *
     * @param response 服务器返回的数据
     */
    abstract public void onSuccess(T response);

    /**
     * 服务器返回数据，但响应码不为200
     *
     * @param response 服务器返回的数据
     */
    public abstract void onFail(Throwable e);

    /**
     * 请求异常
     *
     * @param reason
     */
    public void onException(ExceptionReason reason) {
        switch (reason) {
            case CONNECT_ERROR:
                ToastUtils.show(R.string.connect_error);
                break;

            case CONNECT_TIMEOUT:
                ToastUtils.show(R.string.connect_timeout);
                break;

            case BAD_NETWORK:
                ToastUtils.show(R.string.bad_network);
                break;

            case PARSE_ERROR:
                ToastUtils.show(R.string.parse_error);
                break;

            case UNKNOWN_ERROR:
            default:
                ToastUtils.show(R.string.unknown_error);
                break;
        }
    }

    /**
     * 请求网络失败原因
     */
    public enum ExceptionReason {
        /**
         * 解析数据失败
         */
        PARSE_ERROR,
        /**
         * 网络问题
         */
        BAD_NETWORK,
        /**
         * 连接错误
         */
        CONNECT_ERROR,
        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,
        /**
         * 未知错误
         */
        UNKNOWN_ERROR,
    }
}
