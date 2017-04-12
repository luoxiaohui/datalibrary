package com.android.volley.toolbox.gson;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.google.gson.Gson;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/*****
 * 锟斤拷锟斤拷锟斤拷Gson锟斤拷锟� 锟斤拷锟斤拷锟斤拷应锟侥凤拷锟斤拷模锟斤拷
 * @author asus
 *
 * @param <T>
 */
public  class GsonRequest <T> extends Request<T>{
    
	/** Default on-disk cache directory. */
    private static final String DEFAULT_CACHE_DIR = "gsonvolley";
	
	protected RequesListener<T> listener ;
    protected Map<String, String> mapParams;
    protected Map<String, String> mapheads;
    private   Boolean    istest = false; 
    private   String     testString ;
    
    public static String weblogid;
    
	public GsonRequest(int method, String url ,RequesListener<T> l) {
	     super(method, url, null);
         this.listener = l;
         this.setShouldCache(false);
          
          
	// TODO Auto-generated constructor stub
    }
    /******
     * 
     * @param url
     * @param l
     */
	public GsonRequest(String url,RequesListener<T> l) {
		this(Request.Method.POST, url,l);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		// TODO Auto-generated method stub
		String jsonString;
		try {
			//jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
		 
			if(response.headers.get("weblogid")!=null){
				weblogid = response.headers.get("weblogid");
			}
			
			jsonString = new String(response.data, "utf-8");
			Log.v("WC",weblogid+"KKKKK:"+jsonString);
			Gson gson = new Gson();
			T rs;  
		    rs =gson.fromJson(jsonString,listener.getType());
		 // TODO Auto-generated constructor stub
			return Response.success(rs , HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		   return Response.error(new ParseError(e));
		}catch(Exception  exception){
			 return Response.error(new ParseError(exception));
		}
	}

	@Override
	protected void deliverResponse(T response) {
		// TODO Auto-generated method stub
		listener.onResponse(response);
	}
     
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		// TODO Auto-generated method stub
		if(mapParams==null){
			return Collections.emptyMap();
		}
		return mapParams;
	}
    
	
	
	 
	  
   public void setMapheads(Map<String, String> mapheads) {
		this.mapheads = mapheads;
	}
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		// TODO Auto-generated method stub
    	 if(mapheads!=null){
    		 return mapheads;
    	 }
		return super.getHeaders();
	}
	/**********
      * post 璇锋��浼���������版��
      * @param hashmap  
      */
	public void setHashmap(HashMap hashmap) {
		this.mapParams = hashmap;
	}
	
	
	
	public void setIstest(Boolean istest) {
		this.istest = istest;
	}
	public Boolean getIstest() {
		return istest;
	}
	public String getTestString() {
		return testString;
	}
	/********
	 * 娴�璇������������ㄦā�����版��璇锋��
	 * @param testString
	 * @param isBoolean
	 */
	public void setTestString(String testString,Boolean isBoolean) {
		this.testString = testString;
		this.istest     =isBoolean;
	}
	
	@Override
	public void deliverError(VolleyError error) {
		// TODO Auto-generated method stub
		super.deliverError(error);
		if(this.listener !=null)
			this.listener.onErrorResponse(error);
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		super.cancel();
		listener = null;
	}
    
	 
	 
	/**
     * Creates a default instance of the worker pool and calls {@link RequestQueue#start()} on it.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @return A started {@link RequestQueue} instance.
     */
    public static RequestQueue newRequestQueue(Context context) {
        return newRequestQueue(context, null);
    }
    
    /**
     * Creates a default instance of the worker pool and calls {@link RequestQueue#start()} on it.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @param stack An {@link HttpStack} to use for the network, or null for default.
     * @return A started {@link RequestQueue} instance.
     */
    public static RequestQueue newRequestQueue(Context context, HttpStack stack) {
        File cacheDir = new File(context.getExternalCacheDir(), DEFAULT_CACHE_DIR);

        String userAgent = "volley/0";
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            userAgent = packageName + "/" + info.versionCode;
        } catch (NameNotFoundException e) {
        }

        if (stack == null) {
            if (Build.VERSION.SDK_INT >= 9) {
                stack = new HurlStack();
            } else {
                // Prior to Gingerbread, HttpUrlConnection was unreliable.
                // See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
                stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
            }
        }
        Network network = new DLGJsonBasicNetwork(stack);
        RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir), network);
        return queue;
    }
	 
}
