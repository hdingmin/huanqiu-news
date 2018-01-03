package com.news.huanqiu.ui.main;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.TabHost;

import com.news.huanqiu.R;
import com.news.huanqiu.service.Update;
import com.news.huanqiu.ui.BaseFragmentActivity;


public class MainActivity extends BaseFragmentActivity {
    private Update mUpdate;
    private FragmentTabHost mTabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTabHost = (FragmentTabHost)findViewById(R.id.tabHost);
        initTabView();
        mUpdate = new Update(MainActivity.this);
        mUpdate.checkUpdate();
    }
    private void initTabView()
    {
        mTabHost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        View tab1 = View.inflate(this, R.layout.tab_1,null);
        View tab2 = View.inflate(this,R.layout.tab_2,null);
        View tab3 = View.inflate(this,R.layout.tab_3,null);
        View tab4 = View.inflate(this,R.layout.tab_4,null);
        TabHost.TabSpec spec1=mTabHost.newTabSpec("tab1").setIndicator(tab1);
        TabHost.TabSpec spec2=mTabHost.newTabSpec("tab2").setIndicator(tab2);
        TabHost.TabSpec spec3=mTabHost.newTabSpec("tab3").setIndicator(tab3);
        TabHost.TabSpec spec4=mTabHost.newTabSpec("tab4").setIndicator(tab4);
        mTabHost.addTab(spec1,HomeFragment.class,null);
        mTabHost.addTab(spec2,MyFragment.class,null);
        mTabHost.addTab(spec3,OtherFragment.class,null);
        mTabHost.addTab(spec4,SettingFragment.class,null);
    }
}
