package com.android.volley.toolbox.gson;

import com.android.volley.Request;

import java.util.Map;

/*****
 * ��ȡMap ���͵�JSon������� �������������һ���߽紦��
 * @author asus
 *
 * @param <T>
 */
public class MapGJsonRequest<T extends Map> extends GsonRequest< T > {
    
	public MapGJsonRequest(int method, String url,RequesListener< T> l) {
		super(method, url,l);
		 
		// TODO Auto-generated constructor stub
	}

	public MapGJsonRequest(String url,RequesListener< T> l) {
		super(Request.Method.GET, url,l);
		 
		// TODO Auto-generated constructor stub
	}
    
}
