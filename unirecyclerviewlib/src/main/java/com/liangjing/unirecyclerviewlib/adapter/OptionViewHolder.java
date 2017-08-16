package com.liangjing.unirecyclerviewlib.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liangjing.unirecyclerviewlib.listener.OnItemClickListener;
import com.liangjing.unirecyclerviewlib.listener.OnItemLongClickListener;
import com.liangjing.unirecyclerviewlib.listener.OnItemTouchListener;

/**
 * Created by liangjing on 2017/8/16.
 * function:ViewHolder的抽象类(是ViewHolderForAbsListView和ViewHolderForRecyclerView的同一父类)
 */

public abstract class OptionViewHolder extends RecyclerView.ViewHolder {

    protected Context mContext;
    protected View mConvertView; //item视图
    protected SparseArray<View> mViews;//存放item布局中的子view(控件)--便于后面将其取出来进行相关操作。(可暴露出一些接口方法给外层使用者来操作控件,比如为TextView设置文字)
    protected int mMyPosition;//ViewHolderForAbsListView专用（另一个自带getPosition方法）

    //监听器
    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;
    protected OnItemTouchListener mOnItemTouchListener;

    public OptionViewHolder(View itemView) {
        super(itemView);
    }


    /**
     * function:根据id得到item布局中的View(使用SparseArray保管，提高效率)
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }



    /*================== 一切有可能的操作控件的方法 begin ==================*/

    /**
     * function:设置TextView文字，并返回this--目的：可链式操作(方便)
     *
     * @param viewId
     * @param text
     * @return
     */
    public OptionViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * 设置TextView的文字颜色，并返回this
     */
    public OptionViewHolder setTextColor(int viewId, int colorId) {
        TextView tv = getView(viewId);
        tv.setTextColor(mContext.getResources().getColor(colorId));
        return this;
    }

    /**
     * 设置ImageView的图片，并返回this
     */
    public OptionViewHolder setImageResource(int viewId, int resId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

    /**
     * 设置ImageView的图片，并返回this
     */
    public OptionViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }

    /**
     * 设置ImageView的图片，并返回this
     */
    public OptionViewHolder setImageFileResource(int viewId, String path) {
        ImageView iv = getView(viewId);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        iv.setImageBitmap(bitmap);
        return this;
    }

    /**
     * 设置背景颜色，并返回this
     */
    public OptionViewHolder setBackgroundColor(int viewId, int colorId) {
        View view = getView(viewId);
        view.setBackgroundColor(mContext.getResources().getColor(colorId));
        return this;
    }


    /**
     * 设置背景资源，并返回this
     */
    public OptionViewHolder setBackgrounResource(int viewId, int resId) {
        View view = getView(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    /**
     * 设置显隐，并返回this
     */
    public OptionViewHolder setViewVisibility(int viewId, int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }

    /**
     * 设置是否可用，并返回this
     */
    public OptionViewHolder setEnabled(int viewId, boolean enabled) {
        View view = getView(viewId);
        view.setEnabled(enabled);
        return this;
    }

    /**
     * 设置是否可获取焦点，并返回this
     */
    public OptionViewHolder setFocusable(int viewId, boolean focusable) {
        View view = getView(viewId);
        view.setFocusable(focusable);
        return this;
    }

    /*================== 一切有可能的操作控件的方法 end ==================*/



    /*================== 一些setter或getter方法 begin ==================*/

    /**
     * 得到当前item对应的View
     */
    public View getConvertView() {
        return mConvertView;
    }

    public int getMyPosition() {
        return mMyPosition;
    }

    public void setMyPosition(int myPosition) {
        mMyPosition = myPosition;
    }

    public OnItemClickListener getmOnItemClickListener() {
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

    public void setOnItemTouchListener(OnItemTouchListener mOnItemTouchListener) {
        this.mOnItemTouchListener = mOnItemTouchListener;
    }
    /*================== 一些setter或getter方法 end ==================*/
}
