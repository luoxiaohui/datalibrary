package com.financeyun.commonlibrary.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.financeyun.commonlibrary.R;
import com.financeyun.commonlibrary.ui.swipebacklayout.SwipeBackActivityBase;
import com.financeyun.commonlibrary.ui.swipebacklayout.SwipeBackActivityHelper;
import com.financeyun.commonlibrary.ui.swipebacklayout.SwipeBackLayout;
import com.financeyun.commonlibrary.ui.swipebacklayout.Utils;
import com.financeyun.commonlibrary.ui.view.DLGViewPager;

import java.util.ArrayList;

/**
 * @author luoxiaohui
 * desc 主要作用用于滑动关闭activity,继承PrimaryActivity,上层activity继承此activity即可
 */
public abstract class BaseActivity extends PrimaryActivity implements SwipeBackActivityBase {

    private SwipeBackActivityHelper mHelper;

    private DLGViewPager dlgViewPager;
    private ArrayList<View> viewList;
    private View parentview;

    @Override
    protected void initPageView() {
        if (isParentView()) {
            initParentView();
        } else {
            initSwipeBack();
        }
    }

    private void initSwipeBack() {
        if (isSliding()) {
            mHelper = new SwipeBackActivityHelper(this);
            mHelper.onActivityCreate();
            mHelper.getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        }
    }


    private void initParentView() {
        if (!isSliding()) {
            rl = (RelativeLayout) findViewById(R.id.parent);
            return;
        }
        dlgViewPager = (DLGViewPager) findViewById(R.id.page);
        dlgViewPager.setOnPageChangeListener(new DLGViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                if (arg2 == 0 && arg0 == 0) {
                    finish();
                    overridePendingTransition(0, 0);
                }
                if (softKeyboardBackgroundResource() != -1) {
                    if (arg0 == 0 && arg1 >= 0.99f) {
                        int r = softKeyboardBackgroundResource();
                        getWindow().getDecorView().setBackgroundResource(r);
                    } else if (arg1 > 0.6f)
                        getWindow().getDecorView().setBackgroundResource(R.color.transparent);
                }
            }

            @Override
            public void onPageSelected(int arg0) {
                if (softKeyboardBackgroundResource() != -1 && arg0 == 1) {
                    getWindow().getDecorView().setBackgroundResource(softKeyboardBackgroundResource());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewList = new ArrayList<View>();
        View textView = new View(this);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
                overridePendingTransition(0, 0);
            }
        });
        textView.setBackgroundColor(getResources().getColor(R.color.back_black));
        parentview = LayoutInflater.from(this).inflate(initContentLayoutID(), null);
        initparentView(parentview);
    }

    private void initparentView(View view) {
        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {

                return arg0 == arg1;
            }

            @Override
            public int getCount() {

                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(viewList.get(position));

            }

            @Override
            public int getItemPosition(Object object) {

                return super.getItemPosition(object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));

                return viewList.get(position);
            }

        };
        viewList.add(view);
        this.parentview = view;
        dlgViewPager.setAdapter(pagerAdapter);
        dlgViewPager.setCurrentItem(1, false);
        rl = (RelativeLayout) view.findViewById(R.id.parent);
    }

    @Override
    protected final int initPageLayoutID() {

        if(isParentView()){
            return R.layout.activity_parent_main;
        }
        return initContentLayoutID();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (isSliding() && !isParentView())
            mHelper.onPostCreate();
        setStatusStyle();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        if(parentview!=null&&isParentView())
            return  parentview.findViewById(id);

        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    public boolean isParentView() {

        return false;
    }

    public boolean isSliding() {
        return true;
    }

    protected abstract int initContentLayoutID();


    public int softKeyboardBackgroundResource() {
        return -1;
    }
}
