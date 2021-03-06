package com.liangjing.unirecyclerviewlib.listener;

import android.view.View;
import android.view.ViewGroup;

import com.liangjing.unirecyclerviewlib.adapter.OptionViewHolder;

/**
 * Created by liangjing on 2017/8/16.
 * <p>
 * function:item的长按回调
 */

public interface OnItemLongClickListener {


    /**
     * @param holder
     * @param parent   如果是RecyclerView的话，parent为空
     * @param itemView
     * @param position
     * @return
     */
    boolean onItemLongClick(OptionViewHolder holder, ViewGroup parent, View itemView, int position);
}
