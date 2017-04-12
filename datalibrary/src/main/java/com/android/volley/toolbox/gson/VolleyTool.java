package com.android.volley.toolbox.gson;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;


public class VolleyTool {
	
	/*******
	 * 从模拟数据中的需要的数据
	 * @param key
	 * @return
	 */
	//private static Properties p = new Properties();
	private static VolleyTool volleyTool;
	
	 
	public  HashMap<String, String> hashMap = new HashMap<String, String>();
	private VolleyTool(Context context){
		InputStreamReader in = null;
        BufferedReader bf = null;
		
		try {
			in = new InputStreamReader(context.getResources().getAssets().open("sloth.txt"),"gbk");
			bf =new BufferedReader(in); 
			String line = null;
			while((line = bf.readLine()) != null){
				String [] temp =line.split("=");
				hashMap.put(temp[0], temp[1]);
			}
			bf.close();
			in.close();
			
		} catch(Exception e) {
			 
			in = null;
		}
	}
	
	
	public static String  get(String key ,Context context,String path){
		 if(volleyTool ==null)
			 volleyTool =new VolleyTool(context);
		 String result = null;
 
				
		 try {
			result=volleyTool.hashMap.get(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//这一句是重
		 
		 return result;
	}
}
