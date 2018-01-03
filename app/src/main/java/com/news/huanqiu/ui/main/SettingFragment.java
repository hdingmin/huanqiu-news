package com.news.huanqiu.ui.main;

import android.widget.CompoundButton;
import android.widget.Switch;


import com.news.huanqiu.R;
import com.news.huanqiu.ui.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by hdingmin on 2017/11/25.
 */

public class SettingFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_setting;
    }

    @Override
    protected void init() {

    }
    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
