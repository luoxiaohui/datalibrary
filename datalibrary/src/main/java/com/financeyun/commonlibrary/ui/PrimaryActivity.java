package com.financeyun.commonlibrary.ui;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.financeyun.commonlibrary.R;
import com.financeyun.commonlibrary.network.NetworkManager;
import com.financeyun.commonlibrary.ui.animation.RemoveCoverViewAnimationListener;
import com.financeyun.commonlibrary.ui.autolayout.AutoLayoutActivity;
import com.financeyun.commonlibrary.network.ErrorInfo;
import com.financeyun.commonlibrary.network.IConnectNetHelper;
import com.financeyun.commonlibrary.util.CLog;
import com.financeyun.commonlibrary.util.NetWorkTools;
import com.financeyun.commonlibrary.util.StringUtil;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.gson.DLGRequest;
import com.android.volley.toolbox.gson.GsonVolley;

import java.util.HashMap;
import java.util.Iterator;

import SystemBarTintManager.SystemBarTintManager;
import butterknife.ButterKnife;

/**
 * @author luoxiaohui
 * desc 最初级的activity
 * 用于代理网络请求和处理，和对activity的一些基本设置
 */

public abstract class PrimaryActivity extends AutoLayoutActivity implements
        InterfaceProgress {

    private static final String TAG = "PrimaryActivity";
    PrimaryActivity context;

    public final static int NETREQUEST = 10;

    /*******
     * 页面加载时的背景及加载结束时的处理
     */
    protected View networkCoverView;

    /*******
     * 网络加载时的各种情况的处理
     */
    private int netflage;

    /*******
     * activity 的顶级父类的试图必须ReLativeLayout
     */
    protected RelativeLayout rl;

    /**
     * 屏幕参数
     */
    public DisplayMetrics mDisplayMetrics = null;

    /******************************************************************************************************/
    /**************************************
     * 抽象方法申明，子类必须实现
     *******************************************/
    /******************************************************************************************************/

    /**
     * 抽象方法 ，子类必须实现，初始化页面控件。
     */
    protected abstract void initPageView();

    /**
     * 抽象方法 ，子类必须实现，生成主文件布局ID
     */
    protected abstract int initPageLayoutID();

    /**
     * 抽象方法 ，子类必须实现，逻辑处理
     */
    protected abstract void process(Bundle savedInstanceState);

    /********
     * 网络请求管理器
     */
    private NetworkManager networkManager;

    /******
     * 没有网络的时候的加载图标
     */
    private int network = R.drawable.icon_netfalure;

    /*******
     * 没有数据的时候的加载显示
     */
    private int nodata = R.drawable.icon_falure;

    /***********
     * 加载覆盖背景图的颜色值
     */
    private int networkbackresouce = R.color.main_background;

    private int loadingResource = R.drawable.loadinganimation;

    /*****************************************************************************************************/
    /**
     * 执行流程：<br>
     * 设置程序异常统一处理机制
     * 设置是否需要activity 自带title栏
     * 设置activity是否全屏
     */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        // 判断是否要acitivity 标题栏 true 为不要Title ，false 为title栏。默认true不要Title栏。
        if (IsNoActivityTitle()) {
            //requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        // 判断是否全屏 true为全屏 false为不全屏
        if (setIsFullScreen()) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        /** 初始化设备屏幕参数 */
        mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        // 视图初始化及逻辑处理
        init(savedInstanceState);
    }

    /**
     * 视图初始化及逻辑处理 ，方便后续子类重写设置布局及相关控件或逻辑处理
     *
     * @param savedInstanceState
     */
    public void init(Bundle savedInstanceState) {
        // 设置layout布局
        setContentView(initPageLayoutID());

        rl = (RelativeLayout) findViewById(R.id.parent);
        ButterKnife.bind(this);
        // 初始化页面控件
        initPageView();
        // 业务逻辑处理
        process(savedInstanceState);

    }

    public void canceAllRequest() {
        if (networkManager != null) networkManager.cancelAllRequest();
    }

    public void requestop() {
        if (networkManager != null) networkManager.requestop();
    }


    public void startNetWork(final IConnectNetHelper iConnectNetHelper) {
        startNetWork(iConnectNetHelper, true, R.id.top);
    }

    public void startNetWork(final IConnectNetHelper iConnectNetHelper, int id) {
        startNetWork(iConnectNetHelper, true, id);
    }

    public void startNetWork(final IConnectNetHelper iConnectNetHelper,
                             boolean cover) {
        startNetWork(iConnectNetHelper, cover, R.id.top);
    }

    /***********
     * @param iConnectNetHelper 加载的补助类
     * @param cover             是否显示加载的进度条
     * @param id
     */
    public void startNetWork(final IConnectNetHelper iConnectNetHelper,
                             boolean cover, int id) {
        networkManager = NetworkManager.getInstance(this);
        networkManager.startNetwork(iConnectNetHelper, cover, id);
    }

    /**
     * 设置全屏
     *
     * @return 是否全屏 true为全屏 false为不全屏
     */
    public boolean setIsFullScreen() {
        return false;
    }

    /**
     * 设置是否要acitivity title栏
     *
     * @return true/false 是否显示隐藏title
     */
    public boolean IsNoActivityTitle() {
        return true;
    }

    @Override
    public void addNetWorkCoverView(String mesg, int flage) {
        addNetWorkCoverView(mesg, flage, R.id.top);
    }

    /********
     * 设置没有网络时的图片
     *
     * @param network
     */
    public void setNetworkIconResouce(int network) {
        this.network = network;
    }

    /*******
     * 数据加载失败的图片
     *
     * @param nodata
     */
    public void setNodataIconResouce(int nodata) {
        this.nodata = nodata;
    }

    /*********
     * 设置加载背景图的颜色
     *
     * @param networkbackresouce
     */
    public void setCovebgResouce(int networkbackresouce) {
        this.networkbackresouce = networkbackresouce;
    }

    public void setLoadingResource(int loadingResource) {
        this.loadingResource = loadingResource;
    }

    @Override
    public void addNetWorkCoverView(String mesg, int flage, int id) {
        this.netflage = flage;
        if (rl == null)
            return;

        if (networkCoverView == null) {
            networkCoverView = LayoutInflater.from(this).inflate(
                    R.layout.activity_network_cover, null);
            networkCoverView.setBackgroundResource(networkbackresouce);
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            ImageView imageView = (ImageView) networkCoverView
                    .findViewById(R.id.image);
            if (flage == ErrorInfo.ERROR_CODE_NETERRORWORK) {
                imageView.setBackgroundResource(network);
            } else if (flage == ErrorInfo.LOADINGDATA) {

                imageView.setBackgroundResource(loadingResource);
                AnimationDrawable animationDrawable = (AnimationDrawable) imageView
                        .getBackground();
                animationDrawable.start();
            } else {
                imageView.setBackgroundResource(nodata);
            }
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    onNetWork(v, netflage);
                }
            });
            rlp.addRule(RelativeLayout.BELOW, id);
            TextView msg = (TextView) networkCoverView
                    .findViewById(R.id.txt_msg);
            msg.setText("" + mesg);
            rl.addView(networkCoverView, rlp);
        } else {
            rl.setBackgroundResource(networkbackresouce);
            TextView msg = (TextView) networkCoverView
                    .findViewById(R.id.txt_msg);
            ImageView imageView = (ImageView) networkCoverView
                    .findViewById(R.id.image);
            if (flage == ErrorInfo.ERROR_CODE_NETERRORWORK) {
                imageView.setBackgroundResource(network);
            } else if (flage == ErrorInfo.LOADINGDATA) {
                imageView.setBackgroundResource(R.drawable.loadinganimation);
                AnimationDrawable animationDrawable = (AnimationDrawable) imageView
                        .getBackground();
                animationDrawable.start();

            } else {
                imageView.setBackgroundResource(nodata);
            }

            msg.setText("" + mesg);
        }
    }

    /*********
     * 是否需要添加移除过度页面动画
     *
     * @return
     */
    public boolean isRemoveNetWorkCoverViewAnimation() {

        return true;
    }

    /*******
     * 移除过度加载视图
     */
    @Override
    public void removeNetWorkCoverView() {

        removeNetWorkCoverView(isRemoveNetWorkCoverViewAnimation());
    }

    /*******
     * 移除过度加载视图
     */
    public void removeNetWorkCoverView(boolean isanimation) {

        if (networkCoverView != null && isanimation) {
            Animation animation = new AlphaAnimation(1, 0);
            animation.setDuration(300);
            animation.setAnimationListener(new RemoveCoverViewAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    // TODO Auto-generated method stub
                    super.onAnimationEnd(animation);
                    rl.removeView(networkCoverView);
                    networkCoverView = null;
                }
            });
            networkCoverView.startAnimation(animation);

        } else if (networkCoverView != null) {
            rl.removeView(networkCoverView);
            networkCoverView = null;
        }
    }

    private boolean onNetWork(View v, int flage) {
        // TODO Auto-generated method stub

        // 网络连接失败的处理
        if (flage == ErrorInfo.ERROR_CODE_NETERRORWORK
                && !NetWorkTools.isNetworkAvailable(this)) {
            startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS),
                    NETREQUEST);
            return true;
        } else if (flage == ErrorInfo.LOADINGDATA) {

            return true;
        }
        this.netflage = 2;
        ImageView imageView = (ImageView) networkCoverView
                .findViewById(R.id.image);
        imageView.setBackgroundResource(loadingResource);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView
                .getBackground();
        animationDrawable.start();
        TextView msg = (TextView) networkCoverView.findViewById(R.id.txt_msg);
        msg.setText("");
        onNetWorkClick(v, flage);
        return false;
    }

    /********
     * 处理数据加载失败的点击事件
     *
     * @param v
     * @param flage
     */

    @Override
    public void onNetWorkClick(View v, int flage) {

    }

    /***********
     * 获取顶部通知栏的颜色
     *
     * @return
     */
    public int getBarTintResource() {
        return R.color.black;
    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    protected void setStatusStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(getBarTintResource());
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        canceAllRequest();
    }

}
