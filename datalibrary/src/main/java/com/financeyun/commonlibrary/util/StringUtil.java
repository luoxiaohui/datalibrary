package com.financeyun.commonlibrary.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	
	
	/********
	 * 重Assert目录中读取到文件的数据
	 * @param context
	 * @param strAssertFileName
	 * @return
	 */
	public static String readAssertResource(Context context, String strAssertFileName) {
        AssetManager assetManager = context.getAssets();
        String strResponse = "";
        try {
            InputStream ims = assetManager.open(strAssertFileName);
            strResponse = getStringFromInputStream(ims);
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(strResponse);
            strResponse = m.replaceAll("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strResponse;
    }
	
	private static String getStringFromInputStream(InputStream a_is) {
	        BufferedReader br = null;
	        StringBuilder sb = new StringBuilder();
	        String line;
	        try {
	            br = new BufferedReader(new InputStreamReader(a_is));
	            while ((line = br.readLine()) != null) {
	                sb.append(line);
	            }
	        } catch (IOException e) {
	        } finally {
	            if (br != null) {
	                try {
	                    br.close();
	                } catch (IOException e) {
	                }
	            }
	        }
	        return sb.toString();
	    }

}
