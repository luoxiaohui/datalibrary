package com.financeyun.commonlibrary.network;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.gson.DLGRequest;
import com.android.volley.toolbox.gson.GsonVolley;
import com.financeyun.commonlibrary.ui.PrimaryActivity;
import com.financeyun.commonlibrary.util.CLog;
import com.financeyun.commonlibrary.util.NetWorkTools;
import com.financeyun.commonlibrary.util.StringUtil;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 作者：luoxiaohui
 * 日期:2017/4/7 15:53
 * 文件描述: 网络请求管理器
 *          将PrimaryActivity中关于网络请求部分剥离出来，放在此类中，便于后期扩展维护
 */
public class NetworkManager {

    private PrimaryActivity primaryActivity;

    /********
     * 网络请求的队列管理
     */
    private RequestQueue rqQueue = null;

    /*******
     * 网络请求的对象
     */
    private HashMap<String, DLGRequest> dlgHashMap = new HashMap<String, DLGRequest>();

    private static NetworkManager INSTANCE;

    private NetworkManager(PrimaryActivity primaryActivity){
        this.primaryActivity = primaryActivity;
        if (rqQueue == null) {
            rqQueue = GsonVolley.newRequestQueue(primaryActivity);
        }
    }

    public static NetworkManager getInstance(PrimaryActivity primaryActivity){
        if(INSTANCE == null){
            INSTANCE = new NetworkManager(primaryActivity);
        }
        return INSTANCE;
    }

    public void startNetwork(final IConnectNetHelper iConnectNetHelper,boolean cover,int id){
        if (!NetWorkTools.isNetworkAvailable(primaryActivity) && cover) {
            primaryActivity.addNetWorkCoverView("网络连接失败，请检查网络",
                    ErrorInfo.ERROR_CODE_NETERRORWORK, id);
            return;
        }
        if (cover)
            primaryActivity.addNetWorkCoverView("", ErrorInfo.LOADINGDATA, id);
        CLog.d("url", iConnectNetHelper.initServerUrl());
        if (iConnectNetHelper.initParameter() != null) {
            StringBuffer sb = new StringBuffer();
            sb.append("?");
            for (String key : iConnectNetHelper.initParameter().keySet()) {
                sb.append(key + "=" + iConnectNetHelper.initParameter().get(key) + "&");
            }
            CLog.d("parameters", sb.toString());
        } else {
            CLog.d("parameters", "null");
        }
        HashMap<String, String> heads = iConnectNetHelper.getHeaders();
        if (heads != null) {
            StringBuffer sb = new StringBuffer();
            sb.append("?");
            Iterator it = heads.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                sb.append(key + "=" + heads.get(key) + "&");
                it.remove();
            }
            CLog.d("heads", sb.toString());
        } else {
            CLog.d("heads", "null");
        }
        if (iConnectNetHelper.useSimulationData()) {
            String str = StringUtil.readAssertResource(primaryActivity,
                    iConnectNetHelper.simulationData());

            iConnectNetHelper.requestSuccess(str);
            return;
        }
        if (dlgHashMap.get(iConnectNetHelper.initServerUrl()) == null) {
            final DLGRequest dlgRequest = new DLGRequest(iConnectNetHelper,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // TODO Auto-generated method stub

                            DLGRequest dlgRequest = dlgHashMap
                                    .get(iConnectNetHelper.initServerUrl());
                            dlgRequest.cancel();
                            dlgHashMap.remove(iConnectNetHelper.initServerUrl());
                            iConnectNetHelper.requestSuccess(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                    DLGRequest DLGRequest = dlgHashMap
                            .get(iConnectNetHelper.initServerUrl());
                    DLGRequest.cancel();
                    dlgHashMap.remove(iConnectNetHelper.initServerUrl());
                    ErrorInfo errorInfo;
                    if (error.getMessage() != null
                            && error.getMessage().contains(
                            "UnknownHostException")) {
                        errorInfo = new ErrorInfo(
                                ErrorInfo.ERROR_CODE_NETERRORWORK);
                    } else {
                        errorInfo = new ErrorInfo(
                                ErrorInfo.ERROR_CODE_NET);
                    }
                    iConnectNetHelper
                            .defaultRequestNetDataFail(errorInfo);

                }
            });
            dlgHashMap.put(iConnectNetHelper.initServerUrl(), dlgRequest);
            dlgRequest.setTag(primaryActivity);
            dlgRequest.setShouldCache(iConnectNetHelper.isShouldCache());
            rqQueue.add(dlgRequest);

        }
    }

    public void cancelAllRequest(){
        if (rqQueue != null) {
            rqQueue.cancelAll(primaryActivity);
        }
        dlgHashMap.clear();
    }

    public void requestop() {
        if (rqQueue == null)
            return;
        rqQueue.cancelAll(primaryActivity);
        rqQueue.stop();
        rqQueue = null;
    }


}
