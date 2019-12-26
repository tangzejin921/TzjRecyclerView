# RecyclerView
重写了下


####  包名


### 功能
- holder 继承了 BaseRecyclerViewAdapterHelper 里的 Holder
- 不用写 Adapter,固定用 WLAdapter,因为ViewHolder的构造方法基本固定
- item 的布局根据 IViewType
    - 实现多种布局的简单化
    - 布局的数据化，也就是根据数据显示不同布局
- 当设置了GridLayoutManager时，可以在Item的数据上加SpanSize接口
- DrawCallBack 实现了长按交换


### TODO 
- 加载中界面有问题