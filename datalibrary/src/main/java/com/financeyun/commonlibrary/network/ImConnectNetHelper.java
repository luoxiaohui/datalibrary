package com.financeyun.commonlibrary.network;

import com.financeyun.commonlibrary.ui.InterfaceProgress;
import com.android.volley.Request.Method;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;

public abstract class ImConnectNetHelper<T extends Object> implements IConnectNetHelper {

    protected InterfaceProgress interfaceProgress;

    private boolean islationData = false;

    private String lationDataPath = "";

    protected HashMap<String, String> params;

    protected HashMap<String, String> heads;

    protected String url = "";

    public ImConnectNetHelper(InterfaceProgress interfaceProgress) {

        this.interfaceProgress = interfaceProgress;
    }

    @Override
    public Type getType() {
        Type mySuperClass = getClass().getGenericSuperclass();
        return ((ParameterizedType) mySuperClass).getActualTypeArguments()[0];
    }

    @Override
    public int getMethod() {
        // TODO Auto-generated method stub
        return Method.POST;
    }

    @Override
    public String initServerUrl() {

        return url;
    }

    @Override
    public HashMap<String, String> initParameter() {
        // TODO Auto-generated method stub
        return params;
    }

    @Override
    public HashMap<String, String> getHeaders() {
        // TODO Auto-generated method stub
        return heads;
    }


    public void setHeads(HashMap<String, String> heads) {
        this.heads = heads;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    /********
     * @param result
     * @return 返回true 表示不再处理后面的默认逻辑
     */
    public boolean requestData(T result) {

        return false;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void requestSuccess(String response) {
        // TODO Auto-generated method stub
        try {
//            Gson gson = new Gson();
//            T re = gson.fromJson(response, getType());
//
//            if (requestData(re))
//                return;
            interfaceProgress.removeNetWorkCoverView();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            ErrorInfo errorInfo = new ErrorInfo(ErrorInfo.ERROR_CODE_JSON);
            defaultRequestNetDataFail(errorInfo);
        }
    }

    @Override
    public boolean isShouldCache() {
        return false;
    }

    /************
     * 默认的错误请求的处理
     *
     * @param errorInfo
     */
    @Override
    final public void defaultRequestNetDataFail(ErrorInfo errorInfo) {
        // TODO Auto-generated method stub
        if (requestNetDataFail(errorInfo))
            return;
        switch (errorInfo.errorCode) {
            case ErrorInfo.ERROR_CODE_NETERRORWORK:
                interfaceProgress.addNetWorkCoverView("网络连接失败，请检查网络设置", errorInfo.errorCode);
                break;
            case ErrorInfo.ERROR_CODE_NET:
            case ErrorInfo.ERROR_CODE_JSON:
                interfaceProgress.addNetWorkCoverView("点击重新获取数据", errorInfo.errorCode);
                break;
            default:
                break;
        }
    }

    /**********
     * 自己定义网络错误时的处理
     *
     * @param errorInfo
     * @return 返回true 表示不再处理默认的错误处理
     */
    public boolean requestNetDataFail(ErrorInfo errorInfo) {


        return false;

    }


    public void setIslationData(boolean islationData) {
        this.islationData = islationData;
    }

    public void setLationDataPath(String lationDataPath) {
        this.lationDataPath = lationDataPath;
    }

    @Override
    public Object initParser() {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String simulationData() {
        // TODO Auto-generated method stub
        return lationDataPath;
    }

    @Override
    public boolean useSimulationData() {
        // TODO Auto-generated method stub
        return islationData;
    }

}
