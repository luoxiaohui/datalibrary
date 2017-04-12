package com.android.volley.toolbox.gson;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class RequesListener<T> implements Response.Listener<T>,ErrorListener{
	
	/****
	 *  
	 * @return
	 */
	public Type getType(){
		Type mySuperClass = getClass().getGenericSuperclass();
		
		return  ((ParameterizedType) mySuperClass).getActualTypeArguments()[0];
	}

	@Override
	public void onErrorResponse(VolleyError arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onResponse(T arg0) {
		// TODO Auto-generated method stub
		
	}

	 
	
	
	
}
