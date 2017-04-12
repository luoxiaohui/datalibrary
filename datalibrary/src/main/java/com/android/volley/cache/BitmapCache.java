package com.android.volley.cache;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/******
 * 
 * @author asus
 * {@link #getLowInstance(Context)}}   
 * {@link #getHightInstance(Context)}  
 */
public class BitmapCache implements ImageCache{
	private LruCache<String, Bitmap> mCache;
    
    private static BitmapCache bitmapCache;            
    private static int memClass = 8; 
    
    private static BitmapCache hightBitmapCache;      
    
    
     
    /******
     *  
     */
    public static BitmapCache getLowInstance(Context context){
    	if(bitmapCache ==null){
    		memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass(); 
    		bitmapCache = new BitmapCache();
     	}
    	
    	return bitmapCache;
    }
    
    /*******
     *  
     * @param context
     * @return
     */
    
    public static BitmapCache getHightInstance(Context context){
    	if(hightBitmapCache ==null){
    		memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass(); 
    		 
    		hightBitmapCache = new BitmapCache();
    	}
    	
    	return hightBitmapCache;
    }
	private  BitmapCache() {
		final int cacheSize = (1024 * 1024 * memClass / 16); 
		mCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}

			@Override
			protected void entryRemoved(boolean evicted, String key,
					Bitmap oldValue, Bitmap newValue) {
				// TODO Auto-generated method stub
				super.entryRemoved(evicted, key, oldValue, newValue);
				if (evicted && oldValue !=null && !oldValue.isRecycled()) {  
//                     oldValue.recycle();  
                    oldValue = null;  
                    
                }  
			}
			
			
            
		};
	}
	
 

	@Override
	public Bitmap getBitmap(String url) {
		Bitmap bitmap =mCache.get(url);
		return bitmap;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		mCache.put(url, bitmap);
	}
}
