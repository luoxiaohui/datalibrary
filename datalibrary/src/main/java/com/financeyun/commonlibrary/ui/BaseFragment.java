package com.financeyun.commonlibrary.ui;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.financeyun.commonlibrary.R;
import com.financeyun.commonlibrary.network.NetworkManager;
import com.financeyun.commonlibrary.ui.animation.RemoveCoverViewAnimationListener;
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

import java.util.HashMap;

import butterknife.ButterKnife;
/*
 * @author luoxiaohui
 * desc 初级的fragment
 * 用于代理网络请求和处理，和对activity的一些基本设置
 */
public abstract class BaseFragment extends Fragment implements InterfaceProgress {

    /**
     * ******
     * 视图
     */
    protected View contentView;

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

    /**
     * ****
     * 页面加载时的背景及加载结束时的处理
     */
    private View networkCoverView;

    /**
     * ****
     * 网络加载时的各种情况的处理
     */

    private int netflage;

    /**
     * ****
     * activity 的顶级父类的试图必须ReLativeLayout
     */
    protected RelativeLayout rl;

    /**
     * ***
     * 没有网络的时候的加载图标
     */
    private int network = R.drawable.icon_netfalure;

    /**
     * ****
     * 没有数据的时候的加载显示
     */
    private int nodata = R.drawable.icon_falure;

    /**
     * ********
     * 加载覆盖背景图的颜色值
     */
    private int networkbackresouce = R.color.main_background;

    private int loadingResource = R.drawable.loadinganimation;

    /********
     * 网络请求管理器
     */
    private NetworkManager networkManager;

    /**
     * ****
     * 网络请求的对象
     */

    private HashMap<String, DLGRequest> wcHashMap = new HashMap<String, DLGRequest>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        contentView = inflater.inflate(initPageLayoutID(), container, false);
        rl = (RelativeLayout) contentView.findViewById(R.id.parent);
        ButterKnife.bind(this, contentView);
        initPageView();
        Bundle mBundle = getArguments();
        process(mBundle);
        return contentView;
    }

    public PrimaryActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (PrimaryActivity) activity;
    }

    /**
     * *********
     * 网络请求
     *
     * @param iConnectNetHelper
     * @param primaryActivity
     */
    public void startNetWork(final IConnectNetHelper iConnectNetHelper, PrimaryActivity primaryActivity) {
        startNetWork(iConnectNetHelper, primaryActivity, true);
    }


    /**
     * *********
     * 网络请求的数据
     *
     * @param iConnectNetHelper
     * @param primaryActivity
     * @param cover             为true 表示处理网络加载的覆盖图
     */
    public void startNetWork(final IConnectNetHelper iConnectNetHelper, final PrimaryActivity primaryActivity, boolean cover) {
        networkManager = NetworkManager.getInstance(primaryActivity);
        networkManager.startNetwork(iConnectNetHelper, cover, R.id.top);
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        networkManager.cancelAllRequest();
    }

    /**
     * *****
     * 添加覆盖的加载页面过度图
     *
     * @param mesg
     * @param flage
     */
    public void addNetWorkCoverView(String mesg, int flage) {
        addNetWorkCoverView(mesg, flage, R.id.top);
    }

    @Override
    public void addNetWorkCoverView(String mesg, int flage, int id) {
        // TODO Auto-generated method stub
        if (rl == null)
            return;
        this.netflage = flage;
        if (networkCoverView == null) {
            networkCoverView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_network_cover, null);
            networkCoverView.setBackgroundResource(networkbackresouce);
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            ImageView imageView = (ImageView) networkCoverView.findViewById(R.id.image);
            if (flage == ErrorInfo.ERROR_CODE_NETERRORWORK) {
                imageView.setBackgroundResource(network);
            } else if (flage == ErrorInfo.LOADINGDATA) {

                imageView.setBackgroundResource(loadingResource);
                AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
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
            TextView msg = (TextView) networkCoverView.findViewById(R.id.txt_msg);
            msg.setText("" + mesg);
            rl.addView(networkCoverView, rlp);
        } else {
            rl.setBackgroundResource(networkbackresouce);
            TextView msg = (TextView) networkCoverView.findViewById(R.id.txt_msg);
            ImageView imageView = (ImageView) networkCoverView.findViewById(R.id.image);
            if (flage == ErrorInfo.ERROR_CODE_NETERRORWORK) {
                imageView.setBackgroundResource(network);
            } else if (flage == ErrorInfo.LOADINGDATA) {
                imageView.setBackgroundResource(R.drawable.loadinganimation);
                AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
                animationDrawable.start();

            } else {
                imageView.setBackgroundResource(nodata);
            }

            msg.setText("" + mesg);
        }
    }


    /**
     * ******
     * 是否需要添加移除动画
     *
     * @return
     */
    public boolean isRemoveNetWorkCoverViewAnimation() {

        return true;
    }

    /**
     * ****
     * 移除加载视图
     */
    @Override
    public void removeNetWorkCoverView() {

        if (networkCoverView != null && isRemoveNetWorkCoverViewAnimation()) {
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

    /**
     * *****
     * 设置没有网络时的图片
     *
     * @param network
     */
    public void setNetworkIconResouce(int network) {
        this.network = network;
    }

    /**
     * ****
     * 数据加载失败的图片
     *
     * @param nodata
     */
    public void setNodataIconResouce(int nodata) {
        this.nodata = nodata;
    }

    /**
     * ******
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

    /**
     * ******
     *
     * @param flage = 0 表示网咯不连接 = 1 表示数据加载失败 2表示正在请求数据
     */

    private boolean onNetWork(View v, int flage) {
        // TODO Auto-generated method stub

        // 网络连接失败的处理
        if (flage == ErrorInfo.ERROR_CODE_NETERRORWORK && !NetWorkTools.isNetworkAvailable(getActivity())) {
            startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), PrimaryActivity.NETREQUEST);
            return true;
        } else if (flage == ErrorInfo.LOADINGDATA) {

            return true;
        }
        this.netflage = 2;
        ImageView imageView = (ImageView) networkCoverView.findViewById(R.id.image);
        onNetWorkClick(imageView, flage);
        return false;
    }

    /**
     * *****
     * 处理数据加载失败的点击事件
     *
     * @param v
     * @param flage
     */
    public void onNetWorkClick(View v, int flage) {
        ImageView imageView = (ImageView) v;
        imageView.setBackgroundResource(R.drawable.loadinganimation);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
        TextView msg = (TextView) networkCoverView.findViewById(R.id.txt_msg);
        msg.setText("");
    }

}
