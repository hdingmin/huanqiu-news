package com.news.huanqiu.ui;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by hdingmin on 2017/11/25.
 */

public abstract class BaseFragment extends Fragment {
    protected View rootView;
    private Toast mToast;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
            mToast = Toast.makeText(this.getContext(),"",Toast.LENGTH_SHORT);
            init();
        }else{
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if(parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }
    protected abstract int getLayoutId();
    protected abstract void init();

    protected <T extends View> T findViewById(@IdRes int id)
    {
        return  (T)rootView.findViewById(id);
    }
    protected void toast(String msg){
        mToast.setText(msg);
        mToast.show();
    }
}
