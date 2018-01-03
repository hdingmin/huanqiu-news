package com.news.huanqiu.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.FragmentActivity;

/**
 * Created by hdingmin on 2017/11/1.
 */

public class BaseFragmentActivity extends FragmentActivity {
    protected void getPermission()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED)
            {
                requestPermissions(new String[]{
                        android.Manifest.permission.READ_PHONE_STATE},999);
            }
        }
    }
}
