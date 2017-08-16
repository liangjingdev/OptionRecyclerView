package com.liangjing.unirecyclerviewlib.listener;

import android.view.MotionEvent;
import android.view.View;

import com.liangjing.unirecyclerviewlib.adapter.OptionViewHolder;

/**
 * Created by liangjing on 2017/8/16.
 * <p>
 * function: item的触摸回调
 */

public interface OnItemTouchListener {

    boolean onItemTouch(OptionViewHolder holder, View childView, MotionEvent event, int position);
}


