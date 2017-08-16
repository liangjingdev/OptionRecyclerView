package com.liangjing.unirecyclerview.note;

/**
 * Created by liangjing on 2017/8/16.
 *
 * function:笔记
 */

public class note {

/*

    (1--GridLayoutManager)
    GridLayoutManager(如果需要设置水平方向的网格类型时，需要用到该含有四个参数的构造器)

    GridLayoutManager（Context Context，
    int spanCount，
    int orientation，
    boolean reverseLayout）
    参数
    context	Context：当前上下文，将被用来访问资源。
    spanCount	int：网格中的列或行数
    orientation	int：布局方向。应该是HORIZONTAL或VERTICAL。
    reverseLayout	boolean：设置为true时，布局从end到start。(第四个参数表示你当前是否要改变布局)




    (2--ViewGroup.LayoutParams)
    getLayoutParams

    ViewGroup.LayoutParams getLayoutParams（）

    获取与此视图关联的LayoutParams。所有视图应具有布局参数。这些参数提供给此视图的父级，指定如何排列。ViewGroup.LayoutParams有许多子类，它们对应于ViewGroup的不同子类，负责安排他们的子代。
    如果此视图未附加到父ViewGroup或未setLayoutParams(android.view.ViewGroup.LayoutParams) 成功调用，则此方法可能返回null 。当View附加到父ViewGroup时，此方法不能返回null。

    返回
    ViewGroup.LayoutParams	与此视图相关联的LayoutParams，如果尚未设置参数，则为null



    (3--Drawable)
    getIntrinsicWidth

    int getIntrinsicWidth（）
    返回drawable的内在宽度。

    本征宽度是可绘制的布局宽度，包括任何固有的填充。如果drawable没有固有宽度，例如纯色，则此方法返回-1。

    返回
    int	固有宽度，如果没有固有宽度，则为-1




    (3--Drawable)
    getIntrinsicHeight

    int getIntrinsicHeight（）
    返回drawable的内在高度。

    内在高度是可绘制的高度，包括任何固有的填充。如果drawable没有固有的高度，例如纯色，则此方法返回-1。

    返回
    int	内在高度，或-1如果没有内在高度



    (4--Drawable)
    setBounds

    void setBounds（Rect bounds）
    为Drawable指定一个边界矩形。这是drawable在draw（）方法被调用时绘制的。

    参数
    bounds	Rect



    (5--Drawable)
    draw

    void draw（Canvas canvas）
    绘制其边界（通过setBounds设置），可以使用alpha（通过setAlpha设置）和彩色滤镜（通过setColorFilter设置）等可选效果。

    参数
    canvas	Canvas 画布




    (6--RecyclerView)
    getItemAnimator

    RecyclerView.ItemAnimator getItemAnimator（）
    获取此RecyclerView的当前ItemAnimator。null返回值表示没有动画，并且没有任何动画将会发生项目更改。默认情况下，RecyclerView实例化并使用一个实例DefaultItemAnimator。

    返回
    RecyclerView.ItemAnimator	ItemAnimator当前ItemAnimator。如果为null，则在RecyclerView中的项目发生更改时，不会出现任何动画。




    (6--SimpleItemAnimator)
    setSupportsChangeAnimations
            (如果你有自定义ItemAnimator继承自SimpleItemAnimator,并且此时你想要使用自己所定义的动画效果的话，那么这里需要设置boolean值为true,如果你不想使用自己定义的动画，则设置为false)
    void setSupportsChangeAnimations（boolean supportsChangeAnimations）
    设置此ItemAnimator是否支持项目更改事件的动画。如果将此属性设置为false，则更改项目内容的数据集上的操作将不会被动画化。这些动画是在ItemAnimator子类的 animateChange(ViewHolder, ViewHolder, int, int, int, int)实现中作出的。默认情况下，此属性的值为true。

    参数
    supportsChangeAnimations	boolean：如果此ItemAnimator支持更改动画，则为true，否则为false。如果属性为false，则ItemAnimator将不会animateChange(ViewHolder, ViewHolder, int, int, int, int)在发生更改时收到调用 。




    (7--RecyclerView)
    getAdapter

    Adapter getAdapter ()
    检索以前设置的适配器，如果没有设置适配器，则为null。

    返回
    Adapter	先前设置的适配器




    (8--RecyclerView)
    setAdapter

    void setAdapter (Adapter adapter)
    设置一个新的适配器以根据需要提供子视图。

    当适配器更改时，所有现有的视图都会被回收到池中。如果池只有一个适配器，它将被清除。

    参数
    adapter	Adapter：设置新的适配器，或设置无适配器为null。



    (9--RecyclerView)
    stopScroll

    void stopScroll（）
    停止正在进行的任何当前的滚动，比如smoothScrollBy(int, int)，fling(int, int)或 a touch-initiated fling。



    (10--RecyclerView)
    smoothScrollBy

    void smoothScrollBy（int dx，int dy）
    沿着任一轴给定量的像素动画滚动。

    参数
    dx	int：水平滚动的像素
    dy	int：垂直滚动的像素
    dx: 水平方向滑动的距离，大于0会使滚动向左滚动
    dy: 垂直方向滑动的距离，大于0会使滚动向上滚动
*/


}
