package com.news.huanqiu.ui.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.news.huanqiu.R;

/**
 * Created by Administrator on 2017/3/30.
 */

public class SwipeRefreshLayout extends  android.support.v4.widget.SwipeRefreshLayout {
    float mPrevX;
    int mTouchSlop;

    public SwipeRefreshLayout(Context context) {
        this(context,null);
    }
    public SwipeRefreshLayout(Context context, AttributeSet attributes)
    {
        super(context, attributes);
        setColorSchemeColors(ContextCompat.getColor(context, R.color.app_theme_color));
        setProgressViewOffset(false,0,100);
        setRefreshing(true);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mPrevX = MotionEvent.obtain(ev).getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float eventX = ev.getX();
                float xDiff = Math.abs(eventX - mPrevX);
                if (xDiff>mTouchSlop)
                {
                    return false;
                }
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
