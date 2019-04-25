# [TzjRecyclerView](https://github.com/tzjandroid/TzjRecyclerView)

思想：以实体对象为中心

## 功能
这里adapter 进行了 嵌套，也就是 adapter 嵌套 adapter,

目的是实现不同视图的切换，如无网，空数据等

### Adapter
adapter 被影藏了，并不需要设置 adapter,

需要设置 LayoutManager,设置时他将在内部设置 adapter
- setLineLayoutManager(int orientation)
- setGridLayoutManager(int spanCount)

    setGridLayoutManager(int spanCount, boolean canSpan)
- setStaggeredGridLayoutManager(int spanCount, int orientation)


### 加载中
默认提供了一个 textView 加载中 的 LoadingAdapter

当调用 TzjRecyclerView.notifyDataSetChanged(),时切换到其他 adapter

### 空数据
当调用 TzjRecyclerView.notifyDataSetChanged()时并且没数据时就会切换
adapter 为 EmptyAdapter

### 网络异常
内部监听了网络，当没网是会切换为 NetErrAdapter

### 间隔
真正的间隔并没有实现，这里其实是背景，看到的间隔其实是背景颜色

通过
```text
TzjRecyclerView.setDivider(int px)

TzjRecyclerView.setDivider(boolean leftRight, boolean topBottom)
```

## 示例

### 统一样式
```text
List<Object> list = new ArrayList();
TzjRecyclerView mRecyclerView;

//设置 LayoutManager
mRecyclerView.setLineLayoutManager();

//间隔
mRecyclerView.setDivider(5);

/**
*  这个一点要放到 setLayout 的后面调用
*  Holder 这里的Holder 要继承 TzjHolder<item>
*  构造方法里 初始化，onBind 里设置
*/
mRecyclerView.setViewType(R.layout.xxx,XXXHolder.class);

//可以调用网络返回后调用
mRecyclerView.setList(list);
mRecyclerView.notifyDataSetChanged();
```

### 不同样式(多 View 类型)
与上面一样。
不同的是：
```text
不需要设置 
mRecyclerView.setViewType(R.layout.xxx,XXXHolder.class);

Holder 是通过 item 取的，
所以 List 中的 数据要实现接口 IViewType

    /**
     * 这里直接给 布局
     */
    int type();

    /**
     * 这里提供布局对应的 Holder
     */
    Class<? extends TzjViewHolder> holder();
```

## 问题？
### click 事件
```text
Holder中调用
setOnClickListener(XXXView,position);

然后
mRecyclerView.setClickListener(new TzjAdapter.OnClickIndexListener() {
            @Override
            public void onClick(View v, int index) {
                if (v.getId() == R.id.XXX) {
                    //这里处理点击事件
                }
            }
        });
```

### itemClick/itemLongClcik 事件

```text
mRecyclerView.setItemClickListener(new TzjAdapter.OnItemClickListener<Object>() {
            @Override
            public void onItemClick(TzjAdapter adapter, View v, int index, Object obj) {
                //处理item的 点击事件
            }
        });
mRecyclerView.setItemClickListener(new TzjAdapter.OnItemLongClickListener<Object>() {
            @Override
            public void onItemLongClick(TzjAdapter adapter, View v, int index, Object obj) {
                //处理item的 常按事件
            }
        });
```

### 嵌套问题
RecyclerView 嵌套会导致 adapter 为null,
所以要判断 adapter
```java
public class DemoHolder extends TzjViewHolder<String> {
    
    private TzjRecyclerView childRecyclerView;
    private List<String> list = new ArrayList<>();
    private TzjAdapter adapter;
    
    public DemoHolder(View itemView) {
        super(itemView);
        childRecyclerView = bind(R.id.recyclerView);
    }
    
    @Override
    public void onBind(TzjAdapter adapter, String s, int position) {
        super.onBind(adapter, s, position);
        notifyDataSetChanged();
    }

    /**
     * 刷新数据
     */
    private void notifyDataSetChanged(){
        if (adapter == null){
            childRecyclerView.setLineLayoutManager();
            childRecyclerView.setViewType(R.layout.view_empty, EmptyHolder.class);
            childRecyclerView.setList(list);
            adapter = childRecyclerView.getTzjAdapter();
        }
        childRecyclerView.notifyDataSetChanged();
    }
}
```
### 侧滑菜单问题
内部应用了AndroidSwipeLayout

item点击不了 注意2点：
1. setViewType(int r, int swipe, Class<? extends TzjViewHolder> clzz)，要调用这个方法
2. 或者实现接口改为 ISwipeViewType

你还可以 在 item 外面嵌套一层

### 常按排序问题
通过类 DrawCallBack 实现

## 引入
项目 [TzjRecyclerView](https://github.com/tzjandroid/TzjRecyclerView)

[jitpack](https://www.jitpack.io/#tzjandroid/TzjRecyclerView)
