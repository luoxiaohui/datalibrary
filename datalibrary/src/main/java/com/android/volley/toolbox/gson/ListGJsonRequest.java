package com.android.volley.toolbox.gson;

import com.android.volley.Request;

import java.util.List;

/* 
 * 
 * @author asus
 
 * @param <T>
 */
public  class ListGJsonRequest< T extends List > extends  GsonRequest< T> {
	
	public ListGJsonRequest(int method, String url,RequesListener<T> l) {
		super(method, url, l);
    }
    /******
     * Ĭ����get����
     * @param url
     * @param l
     */
	public ListGJsonRequest(String url,RequesListener<T> l) {
		super(Request.Method.GET, url, l);
	  
		// TODO Auto-generated constructor stub
	}
	 
}
