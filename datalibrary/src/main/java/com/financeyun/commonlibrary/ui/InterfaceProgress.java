package com.financeyun.commonlibrary.ui;

import android.view.View;

public interface InterfaceProgress {

    void addNetWorkCoverView(String mesg, int flage);

    /*********
     * 网络不可用时调用是否覆盖一层VIew 相关的图片自己用图片替代
     *
     * @param mesg
     * @param flage 100  表示网络链接失败  101 表示数据正在加载中  其它是数据请求时返回的resultCode 的自定义处理
     * @param id    覆盖网络加载进度条时在相对布局对应的below
     *
     */
    void addNetWorkCoverView(String mesg, int flage, int id);

    /**********
     * 移除网络加载视图的
     */
    void removeNetWorkCoverView();

    /*********
     * 处理数据加载失败时 或者特殊处理一些点击事件
     * @param v
     * @param flage
     */
    void onNetWorkClick(View v, int flage);


}
