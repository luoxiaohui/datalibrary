package com.financeyun.commonlibrary.network;

/**
 * 作者：luoxiaohui
 * 日期:2017/4/7
 * 错误信息类
 */
public class ErrorInfo {

    /**
     * 网络请求IO异常错误码
     */
    public static final int ERROR_CODE_IO = 104;
    /**
     * 网络请求数据json解析异常
     */
    public static final int ERROR_CODE_JSON = 102;
    /**
     * 网络请求无法连接异常错误码
     */
    public static final int ERROR_CODE_NET = 103;
    /**
     * 网络请求未定义错误码
     */
    public static final int ERROR_CODE_COMMAND = 4;

    /**************
     * 网络没有链接
     */
    public static final int ERROR_CODE_NETERRORWORK = 100;

    /**********
     * 网络数据正在加载
     */
    public static final int LOADINGDATA = 101;

    /**
     * 错误码
     */
    public int errorCode = 0;
    /**
     * 错误信息
     */
    public String errorMsg = "";

    public ErrorInfo(int code) {
        this.errorCode = code;
    }

    public ErrorInfo(int code, String msg) {
        this.errorCode = code;
        this.errorMsg = msg;

    }


}
