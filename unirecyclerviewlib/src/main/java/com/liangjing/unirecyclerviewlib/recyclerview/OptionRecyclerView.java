package com.liangjing.unirecyclerviewlib.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.liangjing.unirecyclerviewlib.R;

/**
 * Created by liangjing on 2017/8/16.
 * <p>
 * function:方便使用的(通用)RecyclerView（默认是纵向列表）
 * <p>
 * 不需要管理LayoutManger，可以通过xml文件或setter方法设置
 */

public class OptionRecyclerView extends RecyclerView {


    /*------------------ 常量 begin ------------------*/
    //类型
    public static final int TYPE_GRID = 0;    //网格类型
    public static final int TYPE_STAGGER = 1; //瀑布式类型
    //方向
    public static final int ORIENTATION_VERTICAL = 0;
    public static final int ORIENTATION_HORIZONTAL = 1;
    /*------------------ 常量 end ------------------*/


    //类型、方向、列数
    private int type = TYPE_GRID;
    private int orientation = ORIENTATION_VERTICAL;
    private int column = 1;

    //分割线
    private int dividerSize = 0;
    private int dividerColor = Color.BLACK;
    private Drawable dividerDrawable;

    //动画(局部刷新动画--Item增删动画)--默认不开启
    private boolean isDefaultAnimatorOpen = false;

    //处理第三种置顶情况(1、position<fistItem 2、firstItem<position<lastItem 3、position>lastItem)
    private boolean move = false;
    //需要移动到的指定的位置的int值(position)
    private int mIndex = 0;

    private Context mContext;
    private LayoutManager mLayoutManager;
    private OptionItemDecoration mItemDecoration;


    public OptionRecyclerView(Context context) {
        super(context);
    }


    public OptionRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        /*================== 获取自定义属性 begin ==================*/
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.OptionRecyclerView);
        for (int i = 0; i < array.getIndexCount(); i++) {
            int attr = array.getIndex(i);
            if (attr == R.styleable.OptionRecyclerView_rv_type) {
                type = array.getInt(attr, 0);  //不设置默认为网格类型
            } else if (attr == R.styleable.OptionRecyclerView_rv_orientation) {
                orientation = array.getInt(attr, 0); //不设置默认为垂直类型
            } else if (attr == R.styleable.OptionRecyclerView_rv_column) {
                column = array.getInt(attr, 1); //不设置默认为1列
            } else if (attr == R.styleable.OptionRecyclerView_rv_divider_size) {
                dividerSize = (int) array.getDimension(attr, 0f);//不设置默认为不存在分割线
            } else if (attr == R.styleable.OptionRecyclerView_rv_divider_drawable) {
                dividerDrawable = array.getDrawable(attr);
            } else if (attr == R.styleable.OptionRecyclerView_rv_divider_color) {
                dividerColor = array.getColor(attr, Color.BLACK);  //不设置默认为黑色
            } else if (attr == R.styleable.OptionRecyclerView_rv_default_animator_open) {
                isDefaultAnimatorOpen = array.getBoolean(attr, false);  //不设置默认为不开启
            }
        }
        array.recycle();
        /*================== 获取自定义属性 end ==================*/

        init();
    }


    /**
     * function:根据属性初始化RecyclerView
     */
    private void init() {
        //1、设置RecyclerView的类型和方向(根据类型和方向的结合来设置相对应的LayoutManager)
        switch (type) {
            case TYPE_GRID:
                switch (orientation) {
                    case ORIENTATION_VERTICAL:
                        mLayoutManager = new GridLayoutManager(mContext, column);
                        break;
                    case ORIENTATION_HORIZONTAL:
                        mLayoutManager = new GridLayoutManager(mContext, column, HORIZONTAL, false);
                        break;
                }
                break;
            case TYPE_STAGGER:
                switch (orientation) {
                    case ORIENTATION_VERTICAL:
                        mLayoutManager = new StaggeredGridLayoutManager(column, StaggeredGridLayoutManager.VERTICAL);
                        break;
                    case ORIENTATION_HORIZONTAL:
                        mLayoutManager = new StaggeredGridLayoutManager(column, StaggeredGridLayoutManager.HORIZONTAL);
                        break;
                }
                break;
        }
        this.setLayoutManager(mLayoutManager); //为RecyclerView设置LayoutManager

        //2、设置RecyclerView的分割线样式
        this.removeItemDecoration(mItemDecoration);
        mItemDecoration = new OptionItemDecoration(mContext, orientation, dividerSize, dividerColor, dividerDrawable);
        this.addItemDecoration(mItemDecoration);

        //3、设置(局部刷新)默认动画是否开启
        if (!isDefaultAnimatorOpen) {
            closeItemAnimator();
        } else {
            openItemAnimator();
        }

        //4、设置滚动监听（用于平滑滚动）
        addOnScrollListener(new RecyclerViewListener());
    }

    /**
     * function:提醒OptionRecyclerView，当前的列表样式已经更改（一般是用代码动态修改了type和orientation后调用，如果修改了分割线样式，也需要调用该方法进行刷新）
     */
    public void notifyViewChanged() {

        init();
        //重新设置布局管理器后需要设置适配器
        Adapter adapter = this.getAdapter();
        if (adapter != null) {
            this.setAdapter(adapter);
        }

    }

    /**
     * function:开启(局部刷新)默认动画
     */
    private void openItemAnimator() {
        isDefaultAnimatorOpen = true;
        this.getItemAnimator().setAddDuration(120);
        this.getItemAnimator().setChangeDuration(250);
        this.getItemAnimator().setMoveDuration(250);
        this.getItemAnimator().setRemoveDuration(120);
        ((SimpleItemAnimator) this.getItemAnimator()).setSupportsChangeAnimations(true);
    }


    /**
     * function:关闭(局部刷新)默认动画
     */
    private void closeItemAnimator() {
        isDefaultAnimatorOpen = false;
        this.getItemAnimator().setAddDuration(0);
        this.getItemAnimator().setChangeDuration(0);
        this.getItemAnimator().setMoveDuration(0);
        this.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) this.getItemAnimator()).setSupportsChangeAnimations(false);
    }


    /**
     * function:自定义分割线
     * <p>
     * 当同时设置了颜色和图片时，以图片为主
     * 当不设置size时，分割线以图片的厚度为标准或不显示分割线（size默认为0）。
     */
    class OptionItemDecoration extends ItemDecoration {

        private Context mContext;
        private int mOrientation;
        private int mDividerSize = 0;
        private int mDividerColor = Color.BLACK;
        private Drawable mDividerDrawable;
        private Paint mPaint;

        private OptionItemDecoration(Context context, int orientation, int dividerSize, int dividerColor, Drawable dividerDrawable) {

            mContext = context;
            mOrientation = orientation;
            mDividerColor = dividerColor;
            mDividerSize = dividerSize;
            mDividerDrawable = dividerDrawable;

            //绘制纯色分割线
            if (mDividerDrawable == null) {
                //初始化画笔(抗锯齿)并设置画笔颜色和画笔样式为填充
                mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaint.setColor(mDividerColor);
                mPaint.setStyle(Paint.Style.FILL);
            } else {
                //绘制图片分割线--如果没有指定分割线的size，则默认是图片的厚度
                if (mDividerSize == 0) {
                    if (mOrientation == ORIENTATION_VERTICAL) {
                        mDividerSize = mDividerDrawable.getIntrinsicHeight();
                    } else {
                        mDividerSize = mDividerDrawable.getIntrinsicWidth();
                    }
                }
            }
        }


        /**
         * function:设置每个item视图的偏移量(注意列表item的排布方向--垂直还是水平)
         *
         * @param outRect
         * @param view
         * @param parent
         * @param state
         */
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
            if (mOrientation == ORIENTATION_VERTICAL) {
                outRect.set(0, 0, 0, mDividerSize);
            } else {
                outRect.set(0, 0, mDividerSize, 0);
            }
        }

        /**
         * function:绘制item分割线(需要根据列表的横纵向来绘制相对应方向的分割线)
         *
         * @param c
         * @param parent
         * @param state
         */
        @Override
        public void onDraw(Canvas c, RecyclerView parent, State state) {
            //纵向列表画横线，横向列表画竖线
            if (mOrientation == ORIENTATION_VERTICAL) {
                drawHorizontalDivider(c, parent, state);
            } else {
                drawVerticalDivider(c, parent, state);
            }
        }

        /**
         * function:画横线--首先需要得到(确定)分割线的四个点：左、上、右、下
         *
         * @param c
         * @param parent
         * @param state
         */
        private void drawVerticalDivider(Canvas c, RecyclerView parent, State state) {

            //画横线时左右可以根据parent得到
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            //上下需要根据每个item(视图)来计算
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                LayoutParams params = (LayoutParams) child.getLayoutParams();
                //params.bottomMargin--表示该视图(item)底部的额外空间,因为item可能设置了margin属性。
                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDividerSize;

                //得到四个点后开始画
                if (mDividerDrawable == null) {
                    c.drawRect(left, top, right, bottom, mPaint);
                } else {
                    mDividerDrawable.setBounds(left, top, right, bottom);
                    mDividerDrawable.draw(c);
                }
            }
        }


        /**
         * function:画竖线--首先需要得到(确定)分割线的四个点：左、上、右、下
         *
         * @param c
         * @param parent
         * @param state
         */
        private void drawHorizontalDivider(Canvas c, RecyclerView parent, State state) {

            //画竖线时上下可以根据parent得到
            int top = parent.getTop();
            int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();

            //左右需要根据每个item(视图)计算
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                LayoutParams params = (LayoutParams) child.getLayoutParams();
                int left = child.getRight() + params.rightMargin;
                int right = left + mDividerSize;

                //得到四个点后开始画
                if (mDividerDrawable == null) {
                    c.drawRect(left, top, right, bottom, mPaint);
                } else {
                    mDividerDrawable.setBounds(left, top, right, bottom);
                    mDividerDrawable.draw(c);
                }
            }
        }
    }


    /**
     * function:滚动到指定位置（注意：对瀑布流无效果）--供外层调用
     */
    public void moveToPosition(int position) {
        if (type != TYPE_GRID) {
            return;
        }

        if (position < 0 || position >= getAdapter().getItemCount()) {
            Log.e("注意：", "超出范围了");
            return;
        }
        mIndex = position;
        //因为此时需要让RecyclerView其自动滚动到指定位置，所以必须先停止正在进行的任何当前的滚动。
        stopScroll();

        GridLayoutManager glm = (GridLayoutManager) mLayoutManager;
        int firstItem = glm.findFirstVisibleItemPosition();
        int lastItem = glm.findLastVisibleItemPosition();

        //如果传入的position值小于firstItem的话,就调用this.scrollToPosition(position),那么列表(整个可视区域)将会向上移动到position值所在的位置,
        //此时position值所对应的item视图也将会位于顶部。scrollToPosition()--只要能够见到该item视图，列表就立即停止移动
        if (position <= firstItem) {
            this.scrollToPosition(position);
        } else if (position <= lastItem) {
            //如果传入的position值小于lastItem并大于firstItem的话,就调用this.scrollBy(0, top),那么列表将会移动到position值所在的位置,
            //此时position值所对应的item视图也将会位于顶部(置顶)。
            int top = this.getChildAt(position - firstItem).getTop();
            this.scrollBy(0, top);
        } else {
            //如果传入的position值所对应的item是视图并没有在列表的可视区域内的话，又由于调用scrollToPosition()方法,position值所对应的item视图一旦进入列表的可见区域内，
            //那么此时列表将会停止移动,此时该item视图不能够置顶,所以还需要调用完scrollToPosition()方法后还要进一步处理.
            this.scrollToPosition(position);
            move = true;
        }

    }

    /**
     * function:平滑滚动到指定位置（注意：对瀑布流无效果）---供外层调用
     */
    public void smoothMoveToPosition(int position) {
        if (type != TYPE_GRID) {
            return;
        }

        if (position < 0 || position >= getAdapter().getItemCount()) {
            Log.e("注意：", "超出范围了");
            return;
        }
        mIndex = position;
        stopScroll();

        GridLayoutManager glm = (GridLayoutManager) mLayoutManager;
        int firstItem = glm.findFirstVisibleItemPosition();
        int lastItem = glm.findLastVisibleItemPosition();
        if (position <= firstItem) {
            this.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            int top = this.getChildAt(position - firstItem).getTop();
            this.smoothScrollBy(0, top);
        } else {
            this.smoothScrollToPosition(position);
            move = true;
        }

    }

    /**
     * function: RecyclerView的滚动监听, 利用其实现平滑滚动条目置顶
     */
    class RecyclerViewListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (mOnScrollListenerExtension != null) {
                //目前RecyclerView内部已经进行过滚动事件监听(用于实现平滑滚动条目置顶)，
                // 但是考虑到外界可能也还需要进行监听滚动事件，所以此处提供了滚动事件监听回调接口。
                mOnScrollListenerExtension.onScrollStateChanged(recyclerView, newState);
            }

            if (type != TYPE_GRID) {
                return;
            }

            //可通过LayoutManager来获取item所在的position(LayoutManager--负责测量和定位RecyclerView的item视图，以及确定何时回收用户不再可见的项目视图的策略。)
            GridLayoutManager glm = (GridLayoutManager) mLayoutManager;

            //newState : 当前滚动状态.  RecyclerView.SCROLL_STATE_IDLE--静止,没有滚动(该if条件语句成立之时也就是position值所对应的item视图已经进入了列表的可见区域中咯)
            if (move && newState == RecyclerView.SCROLL_STATE_IDLE) {
                move = false;
                int n = mIndex - glm.findFirstVisibleItemPosition();
                if (0 <= n && n < OptionRecyclerView.this.getChildCount()) {
                    int top = OptionRecyclerView.this.getChildAt(n).getTop();
                    OptionRecyclerView.this.scrollBy(0, top);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (mOnScrollListenerExtension != null) {
                mOnScrollListenerExtension.onScrolled(recyclerView, dx, dy);
            }

            if (type != TYPE_GRID) {
                return;
            }
            GridLayoutManager glm = (GridLayoutManager) mLayoutManager;
            if (move) {
                move = false;
                int n = mIndex - glm.findFirstVisibleItemPosition();
                if (0 <= n && n < OptionRecyclerView.this.getChildCount()) {
                    int top = OptionRecyclerView.this.getChildAt(n).getTop();
                    OptionRecyclerView.this.scrollBy(0, top);
                }
            }
        }
    }


    /*================== OptionRecyclerView的滚动事件拓展 begin ==================*/
    private OnScrollListenerExtension mOnScrollListenerExtension;

    public OnScrollListenerExtension getOnScrollListenerExtension() {
        return mOnScrollListenerExtension;
    }

    public void setOnScrollListenerExtension(OnScrollListenerExtension onScrollListenerExtension) {
        mOnScrollListenerExtension = onScrollListenerExtension;
    }

    /**
     * OptionRecyclerView的滚动事件拓展（原滚动事件被用于平滑滚动）
     */
    public interface OnScrollListenerExtension {
        void onScrollStateChanged(RecyclerView recyclerView, int newState);

        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }
    /*================== OptionRecyclerView的滚动事件拓展 end ==================*/



    /*================== OptionRecyclerView利用代码方式设置相关属性和获取相关属性 begin ==================*/

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getDividerSize() {
        return dividerSize;
    }

    public void setDividerSize(int dividerSize) {
        this.dividerSize = dividerSize;
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
    }

    public Drawable getDividerDrawable() {
        return dividerDrawable;
    }

    public void setDividerDrawable(Drawable dividerDrawable) {
        this.dividerDrawable = dividerDrawable;
    }

    public boolean isDefaultAnimatorOpen() {
        return isDefaultAnimatorOpen;
    }

    @Override
    public LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    /*================== OptionRecyclerView利用代码方式设置相关属性和获取相关属性 end ==================*/

}

