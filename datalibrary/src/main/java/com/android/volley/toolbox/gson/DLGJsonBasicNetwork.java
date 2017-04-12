package com.android.volley.toolbox.gson;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.ByteArrayPool;
import com.android.volley.toolbox.HttpStack;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class DLGJsonBasicNetwork extends  BasicNetwork {

	public DLGJsonBasicNetwork(HttpStack httpStack) {
		super(httpStack);
		// TODO Auto-generated constructor stub
	}

	public DLGJsonBasicNetwork(HttpStack httpStack, ByteArrayPool pool) {
		super(httpStack, pool);
		// TODO Auto-generated constructor stub
	}

	@Override
	public NetworkResponse performRequest(Request<?> request)  throws VolleyError {
		// TODO Auto-generated method stub
		if(request instanceof GsonRequest){
			GsonRequest<?> gsonRequest =(GsonRequest<?>)request;
			if(gsonRequest.getIstest()){ //是否是模拟数据的测试
				Map<String, String> responseHeaders = new HashMap<String, String>();
				byte responseContents[] =gsonRequest.getTestString().getBytes();
				responseHeaders.put("Charset", "utf-8");
				return new NetworkResponse(HttpStatus.SC_CONFLICT, responseContents, responseHeaders, false);
			}
		} 
		return super.performRequest(request);
	}
	
	
	

}
