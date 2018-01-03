package com.news.huanqiu.ui.widget;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by hdingmin on 2017/11/1.
 */

public class XTextView extends TextView {
    public XTextView(Context context) {
        super(context);
        init();
    }

    public XTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init()
    {
        String fontFamily = "iconfont/iconfont.ttf";
        Typeface iconfont = Typeface.createFromAsset(getContext().getAssets(), fontFamily);
        this.setTypeface(iconfont);
    }
}
