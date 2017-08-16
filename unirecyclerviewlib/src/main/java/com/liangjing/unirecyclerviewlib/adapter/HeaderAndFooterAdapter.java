package com.liangjing.unirecyclerviewlib.adapter;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by liangjing on 2017/8/16.
 * <p>
 * function:头部尾部适配器(RecyclerView专用)
 * <p>
 * 该适配器只是对AdapterForRecyclerView进行了一个包装，在保证不修改AdapterForRecyclerView的getItemType方法的情况下，
 * 给AdapterForRecyclerView增加头部和尾部，所以如果有添加头部或尾部的话，在setAdapter时，一定是使用当前类的实例，而非AdapterForRecyclerView的实例！！
 * <p>
 * 如：
 * AdapterForRecyclerView adapter = new AdapterForRecyclerView();
 * recyclerView.setAdapter(adapter.getHeaderAndFooterAdapter());
 */

public class HeaderAndFooterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int BASE_ITEM_TYPE_HEADER = 1024;
    private static final int BASE_ITEM_TYPE_FOOTER = 2048;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();//存放HeaderView
    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();//存放FootView

    private RecyclerView.Adapter mInnerAdapter; //指的是AdapterForRecyclerView

    //传入创建普通item的通用RecyclerView Adapter
    public HeaderAndFooterAdapter(RecyclerView.Adapter innerAdapter) {
        mInnerAdapter = innerAdapter;
    }

    public RecyclerView.Adapter getInnerAdapter() {
        return mInnerAdapter;
    }

    @Override
    public int getItemViewType(int position) {

        //根据position值来判断当前位置应该是HeaderView还是FootView还是普通item。如果是HeaderView或者FootView,则返回当前位置所对应的键,通过键就可以取到相对应的View.
        if (isHeaderView(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterView(position)) {
            return mFooterViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }

        //注意:因为需要保证AdapterForRecyclerView中的position的值是从0开始的，所以这里传回去的值应是HeaderAndFooterAdapter当前的position - getHeadersCount()
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            return new RecyclerView.ViewHolder(mHeaderViews.get(viewType)) {
            };
        } else if (mFooterViews.get(viewType) != null) {
            return new RecyclerView.ViewHolder(mFooterViews.get(viewType)) {
            };
        } else {
            //如果接收到的是普通item的ItemType,则由AdapterForRecyclerView自身来创建ViewHolder即可
            return mInnerAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderView(position)) {
            return;
        }
        if (isFooterView(position)) {
            return;
        }

        //如果接收到的是普通item的ItemType,则由AdapterForRecyclerView自身来为item添加相关数据即可
        mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount());
    }

    /**
     * function:返回item的总数(包括顶部或者尾部)
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }


    //添加头部--key对应当前HearerView的ItemType
    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    //添加底部--key对应当前FootView的ItemType
    public void addFooterView(View view) {
        mFooterViews.put(mFooterViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    //判断当前位置上是否存在HeaderView
    private boolean isHeaderView(int position) {
        return position < getHeadersCount();
    }

    //判断当前位置上是否存在FootView
    private boolean isFooterView(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFooterViews.size();
    }

    //获取普通item的数目
    private int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }

    /**
     * function:返回FooterView
     * @param position
     * @return
     */
    public View getFooterView(int position){
        return mFooterViews.valueAt(position);
    }

    /**
     * function:返回FooterView
     * @param position
     * @return
     */
    public View getHeaderView(int position){
        return mHeaderViews.valueAt(position);
    }
}
