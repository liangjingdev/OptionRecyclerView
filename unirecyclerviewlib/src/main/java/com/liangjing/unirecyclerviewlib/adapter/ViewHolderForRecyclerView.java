package com.liangjing.unirecyclerviewlib.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by liangjing on 2017/8/16.
 * <p>
 * function:RecyclerView通用的ViewHolder(如果想要实现对item的监听事件，则应该在ViewHolder中进行下手)
 */

public class ViewHolderForRecyclerView extends OptionViewHolder {

    public ViewHolderForRecyclerView(Context context, View itemView) {
        super(itemView);

        //初始化父类中的成员变量。(子类可以初始化父类的成员变量,可以给父类的成员变量赋值,可以调用父类中定义的一些方法,还可以使用父类的成员变量)
        mContext = context;
        mConvertView = itemView;
        mViews = new SparseArray<>();

        mConvertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    //触发回调
                    mOnItemClickListener.onItemClick(ViewHolderForRecyclerView.this, null, v, getPosition());
                }
            }
        });

        mConvertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    //触发回调
                    mOnItemLongClickListener.onItemLongClick(ViewHolderForRecyclerView.this, null, v, getPosition());
                }
                return false;
            }
        });

        mConvertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mOnItemTouchListener != null) {
                    mOnItemTouchListener.onItemTouch(ViewHolderForRecyclerView.this, v, event, getPosition());
                }
                return false;
            }
        });
    }


}
