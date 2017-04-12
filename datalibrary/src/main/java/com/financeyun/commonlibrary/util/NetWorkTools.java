package com.financeyun.commonlibrary.util;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkTools {
	
	public static boolean isNetworkAvailable(Context context) {   
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);   
        if (cm == null) {   
        	return false;
        } else {
        	NetworkInfo neInfo =cm.getActiveNetworkInfo();
        	if (neInfo== null) { 
        		  return false; 
        		}else{
        		  return neInfo.isAvailable();
        		}
        } 
         
    } 

	

}
