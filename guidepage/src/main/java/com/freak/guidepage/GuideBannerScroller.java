package com.freak.guidepage;

import android.content.Context;
import android.widget.Scroller;

/**
 * @author Freak
 * @date 2019/11/12.
 */
public class GuideBannerScroller extends Scroller {
    private int mDuration = 1000;

    public GuideBannerScroller(Context context, int duration) {
        super(context);
        mDuration = duration;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}