package com.liangjing.unirecyclerview.sample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import com.liangjing.unirecyclerview.R;
import com.liangjing.unirecyclerviewlib.adapter.AdapterForRecyclerView;
import com.liangjing.unirecyclerviewlib.adapter.ViewHolderForRecyclerView;
import com.liangjing.unirecyclerviewlib.recyclerview.OptionRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangjing on 2017/8/16.
 * <p>
 * function:实现：RecyclerVIew  列表类型--Grid 列表控件方向--垂直  列数--1列
 * 增加一个顶部和一个底部Item(实现上拉加载下拉刷新功能)
 */
public class Demo1 extends AppCompatActivity {

    private List<String> mData = new ArrayList<>();
    private OptionRecyclerView mRv;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Handler handler;
    private AdapterForRecyclerView mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo1_activity_layout);
        init();
        initView();
        initData();
        initEvents();
    }

    private void init() {
        handler = new Handler();
    }

    private void initView() {
        mRv = (OptionRecyclerView) findViewById(R.id.rv);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
    }


    private void initData() {
        for (int i = 0; i < 20; i++) {
            mData.add("item" + i);
        }
    }


    private void initEvents() {

        mAdapter = new AdapterForRecyclerView<String>(this, mData, R.layout.item) {
            @Override
            public void convert(ViewHolderForRecyclerView holder, String s, int position) {
                holder.setText(R.id.tv, s);
            }
        };
        mRv.setAdapter(mAdapter);

        //设置刷新时动画的颜色，可以设置4个
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));


        //设置下拉刷新功能 -- SwipeRefreshLayout
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //可进行网络请求获取最新数据
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> list = new ArrayList<String>();
                        for (int i = 0; i < 5; i++) {
                            list.add("顶部0000" + i);
                        }
                        mAdapter.setData(list);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 5000);
            }
        });
    }
}