package com.news.guagua.net;

/**
 *
 */
public class BasicResponse<T> {

    private int code;
    private String msg;
    private T data;
//    private int token_flag;
//    private int total;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
//    public String getInfo() {
//        return info;
//    }
//
//    public void setInfo(String info) {
//        this.info = info;
//    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

//    public int getToken_flag() {
//        return token_flag;
//    }
//    public void setToken_flag(int token_flag) {
//        this.token_flag = token_flag;
//    }


    //    public int getTotal() {
//        return total;
//    }
//
//    public void setTotal(int total) {
//        this.total = total;
//    }
    public boolean isOk() {
        return code == 0;
    }
}
