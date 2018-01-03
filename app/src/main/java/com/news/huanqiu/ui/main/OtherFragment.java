package com.news.huanqiu.ui.main;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.news.huanqiu.R;
import com.news.huanqiu.bean.DemoInfoBean;
import com.news.huanqiu.service.Update;
import com.news.huanqiu.ui.BaseFragment;
import com.news.huanqiu.ui.widget.SwipeRefreshLayout;
import com.news.huanqiu.utils.Api;
import com.news.huanqiu.utils.PageResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by hdingmin on 2017/11/25.
 */

public class OtherFragment extends BaseFragment {
    private ListView mListView;
    private List<DemoInfoBean> mDemoList;
    private LayoutInflater layoutInflater;
    private MyAdapter myAdapter;
    private Uri downloadApkUri  = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_other;
    }

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        mListView = findViewById(R.id.demoList);
        mSwipeRefreshLayout= findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        myAdapter = new MyAdapter();
        mListView.setAdapter(myAdapter);
        mListView.setOnItemClickListener(onItemClickListener);
        layoutInflater = (LayoutInflater)this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        loadData();
    }
    private void loadData()
    {
        mSwipeRefreshLayout.setRefreshing(true);
        Api.getDemoList(1, 20, new Api.Callback<PageResult<DemoInfoBean>>() {
            @Override
            public void onSuccess(PageResult<DemoInfoBean> result) {
                mDemoList = result.getRows();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myAdapter.notifyDataSetChanged();
                    }
                });
                setRefreshing(false);
            }

            @Override
            public void onFailed(String reason) {
                setRefreshing(false);
            }
        });
    }

    private void setRefreshing(final boolean refreshing){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(refreshing);
            }
        });
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DemoInfoBean demoInfoBean = (DemoInfoBean)myAdapter.getItem(position);
            if(!TextUtils.isEmpty(demoInfoBean.getDownloadUrl()))
            {
                toast("正在下载，请稍后...");
                downloadApkUri = null;
                new Update(getActivity()).download(demoInfoBean.getDownloadUrl(),demoInfoBean.getAppName()+".apk",666);
            }
        }
    };
    private android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener onRefreshListener = new android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadData();
        }
    };
    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDemoList==null?0:mDemoList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDemoList==null?null:mDemoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHodeler;
            if(convertView == null)
            {
                viewHodeler = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.demo_item,parent,false);
                TextView titleTextView = (TextView)convertView.findViewById(R.id.title);
                TextView subTextView = (TextView)convertView.findViewById(R.id.subTitle);
                TextView timeTextView =(TextView)convertView.findViewById(R.id.time);
                viewHodeler.mTitle =titleTextView ;
                viewHodeler.mSubTitle = subTextView;
                viewHodeler.mTime = timeTextView;
                convertView.setTag(viewHodeler);
            }else
            {
                viewHodeler = (ViewHolder)convertView.getTag();
            }
            DemoInfoBean demoInfoBean = (DemoInfoBean)getItem(position);
            viewHodeler.mTitle.setText(demoInfoBean.getTitle());
            viewHodeler.mSubTitle.setText(demoInfoBean.getSubTitle());
            SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd");
            try {
                viewHodeler.mTime.setText(format.format(format.parse(demoInfoBean.getTime())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return  convertView;
        }

    }
    static class ViewHolder {
        public TextView mTitle;
        public TextView mSubTitle;
        public TextView mTime;
    }


    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
