# OptionRecyclerView
封装一套通用的RecyclerView,便于快速开发列表。(该项目分别对RecyclerView以及其适配器进行了封装，两者结合使用更佳！)
<br>
# 使用方法:
* 1、
```javascript
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
* 2、
```javascript
dependencies {
	        compile 'com.github.liangjingdev:OptionRecyclerView:6827835788'
	}
```

# 一、对RecyclerView的封装！(让其使用更加简单，得心应手)

## OptionRecyclerView
该控件是对RecyclerView的封装，众所周知，RecyclerView功能十分强大，一个控件只需要给它设置不同的LayoutManager就可以实现ListView、GridView和瀑布流，
实际项目开发每次使用都要创建并设置LayoutManager,略显“麻烦”；除此之外，最让人无语的就是分割线的绘制，相信不少人会为其头痛吧。
故本控件针对上述两个问题对RecyclerView进行了优化，每个的设置只需要一句代码搞定。 如：
```javascript
<com.liangjing.unirecyclerviewlib.recyclerview.OptionRecyclerView
    android:id="@+id/rv"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:rv_divider_color="#f00"				//设置纯色分割线颜色（还可以设置图片分割线）
    app:rv_divider_size="10dp"				//设置分割线大小
    app:rv_orientation="vertical"			//设置列表控件的方向
    app:rv_default_animator_open="true"		//设置是否开启局部刷新动画（不设置默认关闭）
    app:rv_type="grid"/>					//设置列表类型（列表/网格、瀑布流）
```

## 1、设置OptionRecyclerView的控件类型
*因为LinearLayoutManager能做到的，GridLayoutManager也能做到，所以本项目并没有用到LinearLayoutManager.
```javascript
①xml方式（有三种选择：grid、stagger）
app:rv_type="" 

②代码方式(有三种选择：TYPE_GRID、TYPE_STAGGER)
mRv.setType(OptionRecyclerView.TYPE_XX);
```

## 2、设置OptionRecyclerView的控件方向
```javascript
①xml方式（有两种选择：vertical、horizontal）
app:rv_orientation="" 

②代码方式(有两种选择：ORIENTATION_VERTICAL、ORIENTATION_HORIZONTAL)
mRv.setOrientation(OptionRecyclerView.XX);
```

## 3、设置OptionRecyclerView的列数
```javascript
①xml方式
app:rv_column="2"

②代码方式
mRv.setColumn(2);
```

## 4、设置OptionRecyclerView的分割线样式
1)设置纯色分割线
```javascript
①xml方式
app:rv_divider_color="#f00"
app:rv_divider_size="10dp" 

②代码方式
mRv.setDividerColor();
mRv.setDividerSize();
```
2)设置图片分割线
```javascript
①xml方式
app:rv_divider_drawable="@mipmap/ic_launcher"
app:rv_divider_size="10dp" 

②代码方式
mRv.setDividerDrawable();
mRv.setDividerSize();
```

## 5、设置默认局部刷新动画的开启和关闭
*因为开发时很多时候是不需要默认的条目动画的，所以默认设置为不要动画（即为false），注意，如果你的项目中用到了自定义条目动画，
那么要将该属性打开！！！
```javascript
①xml方式
app:rv_default_animator_open="true"

②代码方式
//打开默认局部刷新动画
mRv.openDefaultAnimator();
//关闭默认局部刷新动画
mRv.loseDefaultAnimator();
```
下面分别是app:rv_default_animator_open="true"和"false"的效果演示<br>

![效果图](https://github.com/liangjingdev/OptionRecyclerView/raw/master/img/1.gif)
![效果图](https://github.com/liangjingdev/OptionRecyclerView/raw/master/img/2.gif)

## 6、滑动到指定位置
*使用RecyclerView自带的smoothScrollToPosition方法和scrollToPosition方法实现滑动到指定位置时，不会将对应的条目置顶，
使用以下方法可解决上述问题。
<br>
滚动:
```javascript
mRv.moveToPosition(position);
```
平滑滚动：
```javascript
mRv.smoothMoveToPosition(position);
```

## 7、监听LQRRecyclerView的滚动
因为该控件已经对RecyclerView进行过监听，用于实现平滑滚动条目置顶，故要监听其滚动事件，需要使用以下接口：OnScrollListenerExtension
```javascrip
mRv.setOnScrollListenerExtension(new OnScrollListenerExtension...);
```
* 效果图

![效果图](https://github.com/liangjingdev/OptionRecyclerView/raw/master/img/3.gif)
![效果图](https://github.com/liangjingdev/OptionRecyclerView/raw/master/img/4.gif)

# 二、对适配器Adapter(包括ViewHolder)的封装！
(万能适配器－－RecyclerView、ListView、GridView)

* AdapterForAbsListView
## 1、在convert方法中对item进行数据设置(例子)
```javascrip
private List<String> mData = new ArrayList<>();
for (int i = 0; i < 100; i++) {
    mData.add("item " + i);
}

//ListView
mLv.setAdapter(new AdapterForAbsListView<String>(this, mData, R.layout.item_tv_list) {
    @Override
    public void convert(ViewHolderForAbsListView holder, String item, int position) {
        holder.setText(R.id.tv, item);
    }

});

//GridView
mGv = (GridView) findViewById(R.id.gv);
mGv.setAdapter(new AdapterForAbsListView<String>(this, mData, R.layout.item_tv_list) {
    @Override
    public void convert(ViewHolderForAbsListView holder, String item, int position) {
        holder.setText(R.id.tv, item);
    }
});
```

## 2、holder的使用
ViewHolderForAbsListView中提供了许多常规用的控件操作，如设置文字、文字颜色、背景、显隐等，同时每个方法都是返回this，
这意味着可以链式操作，方便快速开发。

* AdapterForRecyclerView
## 1、创建适配器
AdapterForRecyclerView<数据类型>（上下文，数据集合，item的布局引用）

## 2、在convert方法中对item进行数据设置(例子)
```javascrip
//RecyclerView
mRv.setAdapter(new AdapterForRecyclerView<String>(this, mData, R.layout.item_tv_list) {
    @Override
    //此处的‘item‘’是当前位置所对应的给其视图填充数据的对象。
    public void convert(LQRViewHolderForRecyclerView holder, String item, int position) {
        holder.setText(R.id.tv, item);
    }
});
```

## 3、holder的使用
ViewHolderForRecyclerView中提供了许多常规用的控件操作，如设置文字、文字颜色、背景、显隐等，同时每个方法都是返回this，
这意味着可以链式操作，方便快速开发。

## 4、添加头部、尾部
```javascrip
AdapterForRecyclerView<String>  mAdapter = new AdapterForRecyclerView(...);
//必须使用HeaderAndFooterAdapter作为RecyclerView的适配器
mRv.setAdapter(mAdapter.getHeaderAndFooterAdapter());

//添加头部
private void testAddHeaderView() {
    TextView tv = new TextView(this);
    ...
    tv.setText("heaer");
    mAdapter.addHeaderView(tv);
}

//添加尾部
private void testAddFooterView() {
    TextView tv = new TextView(this);
    ...
    tv.setText("footer");
    mAdapter.addFooterView(tv);
}
```
![效果图](https://github.com/liangjingdev/OptionRecyclerView/raw/master/img/5.gif)

## 5、多视图类型支持（viewType）
若项目中需要用到多种条目视图类型，则需要重新getItemViewType方法，其返回值是不同类型对应的视图布局资源id，如：
(参考)
```javascrip
private static final int NOTIFICATION = R.layout.item_notification;
private static final int SEND_TEXT = R.layout.item_text_send;
private static final int RECEIVE_TEXT = R.layout.item_text_receive;
private static final int SEND_STICKER = R.layout.item_sticker_send;
private static final int RECEIVE_STICKER = R.layout.item_sticker_receive;
private static final int SEND_IMAGE = R.layout.item_image_send;
private static final int RECEIVE_IMAGE = R.layout.item_image_receive;
private static final int SEND_VIDEO = R.layout.item_video_send;
private static final int RECEIVE_VIDEO = R.layout.item_video_receive;
private static final int SEND_LOCATION = R.layout.item_location_send;
private static final int RECEIVE_LOCATION = R.layout.item_location_receive;

@Override
public int getItemViewType(int position) {
    IMMessage msg = getData().get(position);
    MsgTypeEnum msgType = msg.getMsgType();
    if (msgType == MsgTypeEnum.notification) {
        return NOTIFICATION;
    }
    if (msgType == MsgTypeEnum.text) {
        if (msg.getDirect() == MsgDirectionEnum.Out) {
            return SEND_TEXT;
        } else {
            return RECEIVE_TEXT;
        }
    }
    if (msgType == MsgTypeEnum.custom) {
        if (msg.getDirect() == MsgDirectionEnum.Out) {
            return SEND_STICKER;
        } else {
            return RECEIVE_STICKER;
        }
    }
    if (msgType == MsgTypeEnum.image) {
        if (msg.getDirect() == MsgDirectionEnum.Out) {
            return SEND_IMAGE;
        } else {
            return RECEIVE_IMAGE;
        }
    }
    if (msgType == MsgTypeEnum.video) {
        if (msg.getDirect() == MsgDirectionEnum.Out) {
            return SEND_VIDEO;
        } else {
            return RECEIVE_VIDEO;
        }
    }
    if (msgType == MsgTypeEnum.location) {
        if (msg.getDirect() == MsgDirectionEnum.Out) {
            return SEND_LOCATION;
        } else {
            return RECEIVE_LOCATION;
        }
    }
    return super.getItemViewType(position);
}
```

## 6、其他
```javascrip
与OptionRecyclerView一起使用，不需要考虑LayoutManager和分割线的情况，开发效率大大提高。
```

* 事件控制
不管是AdapterForAbsListView还是AdapterForRecyclerView，都可以通过使用适配器对item进行事件监听，代码如下：
```javascrip
mAdapter.setOnItemClickListener(new OnItemClickListener() {
    @Override
    public void onItemClick(OptionViewHolder helper, ViewGroup parent, View itemView, int position) {
        holder.setText(R.id.tv, "我被点击了");
    }
});
mAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
    @Override
    public boolean onItemLongClick(OptionViewHolder helper, ViewGroup parent, View itemView, int position) {
        holder.setText(R.id.tv, "我被长按了");
        return false;
    }
});
mAdapter.setOnItemTouchListener(new OnItemTouchListener() {
    @Override
    public boolean onItemTouch(OptionViewHolder helper, View childView, MotionEvent event, int position) {
        holder.setText(R.id.tv, "我被触摸了");
        return false;
    }
});
```
