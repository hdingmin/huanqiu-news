package com.news.huanqiu.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.news.huanqiu.R;
import com.news.huanqiu.ui.BaseActivity;


public class ShowNotifyActivity extends BaseActivity {

    public static final String TITLE_EXTRA="title_extra";
    public static final String CONTENT_EXTRA="content_extra";
    private TextView titleTextView;
    private TextView contentTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_notify_activity);
        titleTextView = (TextView)findViewById(R.id.titleTv);
        contentTextView = (TextView)findViewById(R.id.contentTv);
        init();
    }
    private void init()
    {
        Intent intent = getIntent();
        if(intent!=null){
            String title = intent.getStringExtra(TITLE_EXTRA);
            String content = intent.getStringExtra(CONTENT_EXTRA);
            titleTextView.setText(title);
            contentTextView.setText(content);
        }
    }
}
