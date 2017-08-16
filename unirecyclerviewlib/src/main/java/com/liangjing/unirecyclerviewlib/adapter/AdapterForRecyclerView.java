package com.liangjing.unirecyclerviewlib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.liangjing.unirecyclerviewlib.listener.OnItemClickListener;
import com.liangjing.unirecyclerviewlib.listener.OnItemLongClickListener;
import com.liangjing.unirecyclerviewlib.listener.OnItemTouchListener;

import java.util.List;

/**
 * Created by liangjing on 2017/8/16.
 *
 * function:RecyclerView通用的适配器（支持多itemType）  --该项目对RecyclerView的适配器Adapter以及RecyclerView的ViewHolder进行了封装
 * 为什么让类名使用泛型？--使用泛型的目的是让用户来指定填充数据的类型，以达到'通用'的目的。(用户在创建该通用Adapter的时候需要指定数据类型)
 */

public abstract class AdapterForRecyclerView<T> extends RecyclerView.Adapter<ViewHolderForRecyclerView> {

    private Context mContext;
    private int mDefaultLayoutId = 0; //如果只有一种itemType时,则让外层在创建该通用Adapter的时候直接传入item布局
    private List<T> mData;
    private HeaderAndFooterAdapter mHeaderAndFooterAdapter;

    //给点击、长按、触摸事件设置接口回调
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnItemTouchListener mOnItemTouchListener;


    /**
     * 当使用多种itemType时，最好使用这种构造方法
     */
    public AdapterForRecyclerView(Context context, List<T> data) {
        mContext = context;
        mData = data;
    }

    /**
     * 当使用一种itemType时，最好使用这种构造方法
     */
    public AdapterForRecyclerView(Context context, List<T> data, int defaultLayoutId) {
        this(context, data);
        mDefaultLayoutId = defaultLayoutId;
    }

    /**
     * 当需要使用多itemType时，请重写该方法，返回值就是对应类型的布局id
     */
    @Override
    public int getItemViewType(int position) {
        if (mDefaultLayoutId == 0) {
            throw new RuntimeException("请在 " + this.getClass().getSimpleName() + " 中重写 getItemViewType 方法返回布局资源 id，或者使用 " + this.getClass().getSimpleName() + " 三个参数的构造方法 LQRAdapterForRecyclerView(Context context, int defaultLayoutId, List<T> data)");
        }
        return mDefaultLayoutId;
    }

    /**
     * function:创建ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolderForRecyclerView onCreateViewHolder(ViewGroup parent, int viewType) {

        //参数viewType的值是通过getItemViewType()方法根据不同的position值来进行返回的
        ViewHolderForRecyclerView holder = new ViewHolderForRecyclerView(mContext, View.inflate(mContext, viewType, null));//这里的第三个参数可能需要改为parent(需要尝试下)
        holder.setOnItemClickListener(mOnItemClickListener);
        holder.setOnItemLongClickListener(mOnItemLongClickListener);
        holder.setOnItemTouchListener(mOnItemTouchListener);
        return holder;
    }


    /**
     * function:给item视图绑定数据--抽象出convert()方法给外层来设置item视图上的相关数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolderForRecyclerView holder, int position) {
        convert(holder, mData.get(position), position);
    }

    public abstract void convert(ViewHolderForRecyclerView holder, T t, int position);


    /**
     * function:普通item总数
     * @return
     */
    @Override
    public int getItemCount() {
        if (mData != null)
            return mData.size();
        return 0;
    }


     /*================== 头部、尾部部相关 begin ==================*/


    //添加头部(外层调用)
    public void addHeaderView(View headerView) {
        getHeaderAndFooterAdapter().addHeaderView(headerView);
    }

    //添加底部(外层调用)
    public void addFooterView(View footerView) {
        getHeaderAndFooterAdapter().addFooterView(footerView);
    }

    //返回FooterView--与position值相对应。position值为0,则返回第一个FooterView
    public View getFooterView(int position){
        return mHeaderAndFooterAdapter.getFooterView(position);
    }

    //返回HeaderView--与position值相对应。position值为0,则返回第一个HeaderView
    public View getHeaderView(int position){
        return mHeaderAndFooterAdapter.getHeaderView(position);
    }


    //头部总数
    public int getHeadersCount() {
        return mHeaderAndFooterAdapter == null ? 0 : mHeaderAndFooterAdapter.getHeadersCount();
    }

    //底部总数
    public int getFootersCount() {
        return mHeaderAndFooterAdapter == null ? 0 : mHeaderAndFooterAdapter.getFootersCount();
    }

    /**
     * 判断是否有头部或尾部
     */
    public boolean isHeaderOrFooter(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition() < getHeadersCount() || viewHolder.getAdapterPosition() >= getHeadersCount() + getItemCount();
    }

    //获取HeaderAndFooterAdapter
    //(如果添加了顶部或者底部的话,不仅需要实例化HeaderAndFooterAdapter该类,而且在当前类中也需要拿到其对象来进行相关操作,比如:数据刷新问题。)
    public HeaderAndFooterAdapter getHeaderAndFooterAdapter() {
        if (mHeaderAndFooterAdapter == null) {
            synchronized (HeaderAndFooterAdapter.class) {
                if (mHeaderAndFooterAdapter == null) {
                    mHeaderAndFooterAdapter = new HeaderAndFooterAdapter(this);
                }
            }
        }
        return mHeaderAndFooterAdapter;
    }

    /*================== 头部、尾部相关 end ==================*/



/*================== 数据操作相关 begin ==================*/

    /**
     * 获取指定索引位置的数据模型
     */
    public T getItem(int position) {
        return mData.get(position);
    }

    /**
     * 获取数据集合
     */
    public List<T> getData() {
        return mData;
    }

    /**
     * 数据局部刷新
     */
    public final void notifyItemRangeInsertedWrapper(int positionStart, int itemCount) {
        if (mHeaderAndFooterAdapter == null) {
            notifyItemRangeInserted(positionStart, itemCount);
        } else {
            mHeaderAndFooterAdapter.notifyItemRangeInserted(mHeaderAndFooterAdapter.getHeadersCount() + positionStart, itemCount);
        }
    }

    /**
     * 在集合头部添加新的数据集合（下拉从服务器获取最新的数据集合）
     */
    public void addNewData(List<T> data) {
        if (data != null) {
            mData.addAll(0, data);
            notifyItemRangeInsertedWrapper(0, data.size());
        }
    }



    /**
     * 在集合尾部添加更多数据集合（上拉从服务器获取更多的数据集合）
     */
    public void addMoreData(List<T> data) {
        if (data != null) {
            mData.addAll(mData.size(), data);
            notifyItemRangeInsertedWrapper(mData.size(), data.size());
        }
    }

    /**
     * 数据全局刷新
     */
    public final void notifyDataSetChangedWrapper() {
        if (mHeaderAndFooterAdapter == null) {
            notifyDataSetChanged();
        } else {
            mHeaderAndFooterAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置全新的数据集合，如果传入null，则清空数据列表（第一次从服务器加载数据，或者下拉刷新当前界面数据列表）
     */
    public void setData(List<T> data) {
        if (data != null) {
            mData = data;
        } else {
            mData.clear();
        }
        notifyDataSetChangedWrapper();
    }

    /**
     * 清空数据列表
     */
    public void clearData() {
        mData.clear();
        notifyDataSetChangedWrapper();
    }

    /**
     * 数据移除刷新
     */
    public final void notifyItemRemoveWrapper(int position) {
        if (mHeaderAndFooterAdapter == null) {
            notifyItemRemoved(position);
        } else {
            mHeaderAndFooterAdapter.notifyItemRemoved(mHeaderAndFooterAdapter.getHeadersCount() + position);
        }
    }

    /**
     * 删除指定索引数据条目
     */
    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoveWrapper(position);
    }

    /**
     * 删除指定数据条目
     */
    public void removeItem(T model) {
        removeItem(mData.indexOf(model));
    }

    /**
     * 数据添加刷新
     */
    public final void notifyItemInsertedWrapper(int position) {
        if (mHeaderAndFooterAdapter == null) {
            notifyItemInserted(position);
        } else {
            mHeaderAndFooterAdapter.notifyItemInserted(mHeaderAndFooterAdapter.getHeadersCount() + position);
        }
    }

    /**
     * 在指定位置添加数据条目
     */
    public void addItem(int position, T model) {
        mData.add(position, model);
        notifyItemInsertedWrapper(position);
    }

    /**
     * 在集合头部添加数据条目
     */
    public void addFirstItem(T model) {
        addItem(0, model);
    }

    /**
     * 在集合末尾添加数据条目
     */
    public void addLastItem(T model) {
        addItem(mData.size(), model);
    }

    /**
     * 数据变化刷新
     */
    public final void notifyItemChangedWrapper(int position) {
        if (mHeaderAndFooterAdapter == null) {
            notifyItemChanged(position);
        } else {
            mHeaderAndFooterAdapter.notifyItemChanged(mHeaderAndFooterAdapter.getHeadersCount() + position);
        }
    }

    /**
     * 替换指定索引的数据条目
     */
    public void setItem(int position, T newModel) {
        mData.set(position, newModel);
        notifyItemChangedWrapper(position);
    }

    /**
     * 替换指定数据条目
     */
    public void setItem(T oldModel, T newModel) {
        setItem(mData.indexOf(oldModel), newModel);
    }

    /**
     * 数据移动刷新
     */
    public final void notifyItemMoveWrapper(int fromPosition, int toPosition) {
        if (mHeaderAndFooterAdapter == null) {
            notifyItemMoved(fromPosition, toPosition);
        } else {
            mHeaderAndFooterAdapter.notifyItemMoved(mHeaderAndFooterAdapter.getHeadersCount() + fromPosition, mHeaderAndFooterAdapter.getHeadersCount() + toPosition);
        }
    }

    /**
     * 移动数据条目的位置
     */
    public void moveItem(int fromPosition, int toPosition) {
        notifyItemChangedWrapper(fromPosition);
        notifyItemChangedWrapper(toPosition);

        //要先执行上面的notifyItemChanged，然后再执行下面的moveItem事件

        mData.add(toPosition, mData.remove(fromPosition));
        notifyItemMoveWrapper(fromPosition, toPosition);
    }

    /**
     * 获取第一个数据模型
     */
    public T getFirstItem() {
        return getItemCount() > 0 ? getItem(0) : null;
    }

    /**
     * 得到最后一个数据模型
     */
    public T getLastItem() {
        return getItemCount() > 0 ? getItem(getItemCount() - 1) : null;
    }

    /*================== 数据操作相关 end ==================*/



    /*================== setter或getter方法 begin ==================*/

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public OnItemTouchListener getOnItemTouchListener() {
        return mOnItemTouchListener;
    }

    public void setmOnItemTouchListener(OnItemTouchListener mOnItemTouchListener) {
        this.mOnItemTouchListener = mOnItemTouchListener;
    }

    /*================== setter或getter方法 end ==================*/
}
