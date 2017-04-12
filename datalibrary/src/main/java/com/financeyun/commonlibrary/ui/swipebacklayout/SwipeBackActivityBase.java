package com.financeyun.commonlibrary.ui.swipebacklayout;

import com.financeyun.commonlibrary.ui.swipebacklayout.SwipeBackLayout;

/**
 * Created by wuchu on 16/7/21.
 */


public interface SwipeBackActivityBase{
    /**
     * @return the SwipeBackLayout associated with this activity.
     */
    public abstract SwipeBackLayout getSwipeBackLayout();

    public abstract void setSwipeBackEnable(boolean enable);

    /**
     * Scroll out contentView and finish the activity
     */
    public abstract void scrollToFinishActivity();

}
