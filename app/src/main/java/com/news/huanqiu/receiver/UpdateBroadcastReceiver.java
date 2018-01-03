package com.news.huanqiu.receiver;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by hdingmin on 2017/10/23.
 */

public class UpdateBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        Long key = preference.getLong("downloadKey", 0);
        Long id = preference.getLong("downloadId", 0);
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(id);
        DownloadManager downloadManager = ((DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE));
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst())
        {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status)
            {
                case DownloadManager.STATUS_SUCCESSFUL:
                    final Uri uri = downloadManager.getUriForDownloadedFile(id);
                    if(key ==999) {
                        install(context,uri);
                    }else if(key==666) {
                        EventBus.getDefault().post(uri);
                    }
                    break;
                case DownloadManager.STATUS_FAILED:
                    preference.edit().clear();
                    downloadManager.remove(id);
                    break;
            }
        }
    }
    private void install(Context context,Uri uri)
    {
        Intent intentO = new Intent();
        intentO.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentO.setAction(Intent.ACTION_VIEW);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            // 适配android7.0 ，不能直接访问原路径
            // 需要对intent 授权
            intentO.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intentO.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intentO);
    }
}
