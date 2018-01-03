package com.news.huanqiu.service;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.news.huanqiu.Config;
import com.news.huanqiu.receiver.UpdateBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

/**
 * Created by hdingmin on 2017/10/23.
 */

public class Update {
    private Context mContext;
    public Update(Context context)
    {
        mContext = context;
    }
    public void checkUpdate()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                update();
            }
        }).start();
    }
    private void update()
    {
        String formatStr="appid=%s&platform=%s";
        String body =  String.format(formatStr, Config.APPID,"Android");
        String jsonString = httpPost(Config.CHECK_UPDATE,body);
        try {
            JSONObject object  = new JSONObject(jsonString.toLowerCase());
            boolean isBeta =object.getBoolean("isbeta");
            String version = object.getString("version");
            if("null".equals(version)) return;
            final String downloadUrl = object.getString("downloadurl");
            String changeLog = object.getString("changelog");
            String thisVersion = getVersion();
            int diff = compareVersion(version.replace("-beta", "."),thisVersion.replace("-beta", "."));
            final String fileName = String.format(Config.APK_FILE_NAME,version);
            //只有beta版本才能升级beta版本, 并且升级是强制的
            if (thisVersion.contains("beta") && version.contains("beta") &&diff > 0)
            {
                alertUpdate(changeLog,downloadUrl,fileName,true);
            }
            //如果有正式版本更新
            if (!version.contains("beta"))
            {
                if ((thisVersion.contains("beta") && diff >= 0) || (!thisVersion.contains("beta") && diff> 0))
                {
                    //beta版本强制升级到正式版本
                    if (getVersion().contains("beta"))
                    {
                        alertUpdate(changeLog,downloadUrl,fileName,true);
                    }
                    else
                    {
                        alertUpdate(changeLog,downloadUrl,fileName,false);
                    }
                }
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void download(String url,String fileName,int downloadKey){
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        mContext.registerReceiver(new UpdateBroadcastReceiver(), filter);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(false);
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        request.setTitle(fileName);

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
        request.setMimeType(mimeString);

        DownloadManager downloadManager = (DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        long id = downloadManager.enqueue(request);
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(mContext);
        preference.edit().putLong("downloadKey", downloadKey).commit();
        preference.edit().putLong("downloadId", id).commit();
    }
    protected String getVersion()
    {
        String version=null;
        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            version = info.versionName;

        } catch (Exception e) {
            e.printStackTrace();
            version = "1.0.0";
        }
        return version;
    }
    protected static String httpPost(String url,String body)
    {
        try
        {
            byte[] bytes = body.toString().getBytes();
            URL urlPost = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlPost.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setFixedLengthStreamingMode(bytes.length);
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = connection.getOutputStream();
            try{
                out.write(bytes);
            }finally {
                close(out);
            }
            InputStream in =  connection.getInputStream();
            return getString(in);
        }catch (Exception ex)
        {
            return "";
        }
    }
    protected static String getString(InputStream stream) throws IOException {
        String newLine;
        if (stream == null) {
            return "";
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder content = new StringBuilder();
        do {
            newLine = reader.readLine();
            if (newLine != null)
                content.append(newLine).append('\n');
        } while (newLine != null);
        if (content.length() > 0) {
            content.setLength(content.length() - 1);
        }
        return content.toString();
    }
    protected static void close(Closeable closeable) {
        if (closeable == null)
            return;
        try {
            closeable.close();
        } catch (IOException e) {

        }
    }
    protected int compareVersion(String thisVersion,String version)
    {
        if (TextUtils.isEmpty(thisVersion) || TextUtils.isEmpty(version))
        {
            return 0;
            //throw new ArgumentException("无效的参数");
        }
        String[] versionArray1 = thisVersion.split("\\.");//注意此处为正则匹配，不能用.；
        String[] versionArray2 = version.split("\\.");
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
        int diff = 0;
        while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0)
        {//再比较字符
            ++idx;
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }
    private void alertUpdate(final String msg,final String downloadUrl,final String fileName,final boolean compulsion)
    {
        Looper.prepare();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("新版本通知")
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        download(downloadUrl,fileName,999);
                    }
                }).setNegativeButton("忽略", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(compulsion){

                }else {
                    dialog.dismiss();
                }
            }
        });
        TextView textView = new TextView(mContext);
        int padding = dp2px(mContext,15);
        textView.setPadding(padding,padding,padding,padding);
        textView.setText(msg.replace("\\n", "\n"));
        builder.setView(textView);
        builder.create().show();
        Looper.loop();
    }
    public  static  int dp2px(Context context,float dpValue)
    {
        return  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue,context.getResources().getDisplayMetrics());
    }
}
