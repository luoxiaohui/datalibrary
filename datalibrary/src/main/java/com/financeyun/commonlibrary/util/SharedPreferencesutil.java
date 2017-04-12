package com.financeyun.commonlibrary.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesutil {

	public static  final String MAC="mac";
	
	public static void saveString(Context context,String value,String key){
		SharedPreferences preference = context.getSharedPreferences("DATA", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preference.edit(); 
		editor.putString(key,value);
		editor.apply();
	}
	
	public static String getString(Context context,String key) {
		SharedPreferences preference = context.getSharedPreferences("DATA", Context.MODE_PRIVATE);
		return preference.getString(key, "");
		
	}

    public static void saveInt(Context context,int value,String key){
        SharedPreferences preference = context.getSharedPreferences("DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(key,value);
        editor.apply();
    }

    public static int getInt(Context context,String key) {
        SharedPreferences preference = context.getSharedPreferences("DATA", Context.MODE_PRIVATE);
        return preference.getInt(key, 0);

    }
	
	
	public static void saveBoolean(Context context,Boolean value,String key){
		SharedPreferences preference = context.getSharedPreferences("DATA", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preference.edit(); 
		editor.putBoolean(key,value);
		editor.apply();
	}
	
	public static Boolean getBoolean(Context context,String key) {
		SharedPreferences preference = context.getSharedPreferences("DATA", Context.MODE_PRIVATE);
		return preference.getBoolean(key,true);
		
	}
	
	public static Boolean getLoadImagBoolean(Context context) {
		SharedPreferences preference = context.getSharedPreferences("DATA", Context.MODE_PRIVATE);
		return preference.getBoolean("IMAGE",false);
		
	}
	
	
}
