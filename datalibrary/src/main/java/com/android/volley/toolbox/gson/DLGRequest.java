package com.android.volley.toolbox.gson;

import com.financeyun.commonlibrary.network.IConnectNetHelper;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;


public class DLGRequest extends StringRequest {
	
	/**
	 * 网络请求辅助类
	 */
	private IConnectNetHelper connectNetHelper;
	
	public DLGRequest(IConnectNetHelper connectNetHelper, Listener<String> listener, ErrorListener errorListener) {
		super(connectNetHelper.getMethod(), connectNetHelper.initServerUrl(),listener, errorListener);
		this.connectNetHelper = connectNetHelper;
		setShouldCache(false);
		RetryPolicy retryPolicy = new DefaultRetryPolicy(15 * 1000, 0, 1.0f);
		setRetryPolicy(retryPolicy);
	}
	
	
	/**
	 * 获取网络请求参数
	 */
	@Override
	protected Map<String, String> getParams() {
		if (connectNetHelper != null
				&& connectNetHelper.initParameter() != null
				&& connectNetHelper.initParameter().size() > 0) {
			if (VolleyLog.DEBUG) {
				StringBuffer sb = new StringBuffer();
				for (String key : connectNetHelper.initParameter().keySet()) {
					sb.append(key + ":"
							+ connectNetHelper.initParameter().get(key) + "\n");
				}
				 
			}
			return connectNetHelper.initParameter();
		}
		return null;
	}

	/**
	 * 获取头信息
	 */
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		if (connectNetHelper != null && connectNetHelper.getHeaders() != null
				&& connectNetHelper.getHeaders().size() > 0) {
			return connectNetHelper.getHeaders();
		} else {
			return super.getHeaders();
		}
	}


}
