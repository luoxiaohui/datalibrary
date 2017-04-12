package com.financeyun.commonlibrary.network;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * 作者：luoxiaohui
 * 日期:2017/4/7
 * 通讯辅助类接口
 *
 */
public interface IConnectNetHelper{
	
	public Type getType();
	
	/*******
	 * 获取请求的方式
	 */
	
	public int getMethod();
	
	
	/**
	 * 初始化服务端接口URL
	 * 
	 * @return String 服务端接口URL
	 * */
	public String initServerUrl();

	/**
	 * 初始化协议参数信息
	 * 
	 * @return HashMap 协议接口参数键值对
	 **/
	public HashMap<String, String> initParameter();

	/**
	 * 获取头信息
	 * 
	 * @return HashMap 头信息键值对
	 **/
	public HashMap<String, String> getHeaders();

	/**
	 * 请求服务器成功时调用的方法,重写此方法可以自定义解析
	 * 
	 * @param response
	 *            数据解析对象
	 * */
	public void requestSuccess(String response);

	/**
	 * 请求服务器失败或数据获取、解析失败时调用的方法
	 * 
	 * @param errorInfo
	 *            错误信息
	 * */
	public void defaultRequestNetDataFail(ErrorInfo errorInfo);

	/**
	 * 初始化协议解析类
	 * 
	 * @return parser 协议解析类
	 */
	public Object initParser();

	/**
	 * 当服务器的还没有完成接口的时候，我们需要用到模拟数据进行功能流程上的开发，保证项目顺利上线
	 * 
	 * @return 放在assets文件夹下模拟数据文件的名称(不带路径),此处xml与json通用
	 */
	public String simulationData();

	/**
	 * 是否使用伪数据开关
	 * 
	 * @return true or false
	 */
	public boolean useSimulationData();

    /*******
     * 是否保存缓存
     */

    public boolean isShouldCache();

	 
}
