# ViewPager 中 Fragment的生命周期 与 网络请求懒加载

## FragmentPagerAdapter VS FragmentStatePagerAdapter

- **FragmentPagerAdapter**

> Adapter 每页都是一个Fragment，并且所有的Fragment实例一直保存在Fragment manager中。所以它适用于少量固定的fragment，比如一组用于分页显示的标签。除了当Fragment不可见时，它的视图层有可能被销毁外，每页的Fragment都会被保存在内存中。

经过测试我们可以发现，当使用 FragmentPagerAdapter 的时候已经被创建的页面变为不可见的时候生命周期为： 

> onPause -> onStop -> onDestroyView

- **FragmentStatePagerAdapter**

> 每页都是一个Fragment，当Fragment不被需要时（比如不可见），整个Fragment都会被销毁，除了saved state被保存外（保存下来的bundle用于恢复Fragment实例）。所以它适用于很多页的情况。

经过测试我们可以发现，当使用 FragmentPagerAdapter 的时候已经被创建的页面变为不可见的时候生命周期为：

> onPause -> onStop -> onDestoryView -> onDestroy -> onDetach

可以看出当使用 FragmentStatePagerAdapter 的时候，不会保留不可见（不相邻的）页面在内存中，而是直接销毁了，等下次在可见（相邻页面可见）的时候再次创建。

-------

## FragmentPagerAdapter 中 Fragment 的生命周期（ setUserVisibleHint 乱入）

我们知道 Fragment 生命周期函数有  :

> setUserVisibleHint(乱入的不用在意我) -> onAttach —> onCreate -> onCreateView -> onActivityCreated -> onStart -> onResume -> onPause -> onStop -> onDestroyView -> onDetach -> onDestroy


Fragment 本身作为片段存在于 Activity中，当 Fragment 单独使用的时候其生命周期是伴随着 Activity 的生命周期的，即 Activity onResume 的时候 内部所有的 Fragment 也就走到了 onResume 这点初学者可能不会太在意，在这里作为题外话敲下黑板。

> 想要进一步了解的请参照： [与 Activity 生命周期协调一致](https://developer.android.com/guide/components/fragments.html?hl=zh-cn#Lifecycle)


![ Fragment于 Activity 的关系 ](https://developer.android.com/images/activity_fragment_lifecycle.png?hl=zh-cn)

下面说用 FragmentPagerAdapter 承载页面的 Fragment 时候对应的生命周期。这里说的情况都是没有设置 setOffscreenPageLimit 属性的时候。这里假设 ViewPager 中有3个 Fragment ：OutAFragment ，OutBFragment，OutCFragment 。当首次进入 Activity 时候 ViewPager 会初始化 OutAFragment，并预加载 OutBFragment，生命周期如下所示：


```
 // 首次进入界面 A 
 E/TAG: OutAFragment  unVisibleToUser
 E/TAG: OutBFragment  unVisibleToUser
 E/TAG: OutAFragment  isVisibleToUser
 
 E/TAG: OutAFragment  onAttach
 E/TAG: OutAFragment  onCreate
 
 E/TAG: OutBFragment  onAttach
 E/TAG: OutBFragment  onCreate
 
 E/TAG: OutAFragment  onCreateView
 E/TAG: OutAFragment  onActivityCreated
 E/TAG: OutAFragment  onStart
 E/TAG: OutAFragment  onResume
 
 E/TAG: OutBFragment  onCreateView
 E/TAG: OutBFragment  onActivityCreated
 E/TAG: OutBFragment  onStart
 E/TAG: OutBFragment  onResume
```
通过上方的日志打印我们可以清楚的看到，`OutAFragment`，`OutBFragment` 都伴随着 Activity 的生命周期走了对应的生命周期方法。

接下来我们依次滑动到 OutBFragment 和 OutCFragment 看下生命周期方法是怎么走的：

```
 // 首次进入界面 B  因为 ViewPager 对界面进行了缓存造成了

 E/TAG: OutCFragment  unVisibleToUser
 E/TAG: OutAFragment  unVisibleToUser
 E/TAG: OutBFragment  isVisibleToUser
 E/TAG: OutCFragment  onAttach
 E/TAG: OutCFragment  onCreate
 E/TAG: OutCFragment  onCreateView
 E/TAG: OutCFragment  onActivityCreated
 E/TAG: OutCFragment  onStart
 E/TAG: OutCFragment  onResume


 //首次进入界面 C
 
E/TAG: OutBFragment  unVisibleToUser
E/TAG: OutCFragment  isVisibleToUser
E/TAG: OutAFragment  onPause
E/TAG: OutAFragment  onStop
E/TAG: OutAFragment  onDestroyView
```
不知道这里大家注意到了没有由于 ViewPager 对相邻页面做了预加载处理，造成我们切换到 B 页面的时候 `OutBFragment` 没有走任何生命周期函数。只调用了 `setUserVisibleHint` 并传入 true 正如上述 Log 打印一样。 当我们滑到 C 的时候 OutAFragment View 被回收，但 Fragment 对象本身并没有被回收。

上述只是说明了当我们顺序滑动的时候片段的生命周期调用，如果我们以 A —>C 的方式进行点击切换生命周期就会发生变化。此时我们可以理解为 C 并没被预加载而是直接初始化了，所以他不会像先前的一样只调用 `setUserVisibleHint` 而是：


```
E/TAG: OutCFragment  unVisibleToUser
E/TAG: OutAFragment  unVisibleToUser
E/TAG: OutCFragment  isVisibleToUser

E/TAG: OutCFragment  onAttach
E/TAG: OutCFragment  onCreate
E/TAG: OutCFragment  onCreateView
E/TAG: OutCFragment  onActivityCreated
E/TAG: OutCFragment  onStart
E/TAG: OutCFragment  onResume

E/TAG: OutAFragment  onPause
E/TAG: OutAFragment  onStop
E/TAG: OutAFragment  onDestroyView
```
这样生命周期大概就解释完了。那么我们仔细想一下，我们是不是不能像 Activity 一样简单的在 onResume 中调用网络请求就可以实现界面可见的时候刷新了？ 而对于产品来说这种要求并不过分，相信一部分人已经想到了，那么就是 Fragment 与 ViewPager 连用时的 网络数据懒加载。

-------

## Fragment 与 ViewPager 连用时的 网络数据懒加载

关于在 ViewPager 中为什么需要懒加载，当然是因为 ViewPager 自身会帮我们缓存相邻 pager 的 Fragment ，Android 本身为我们做了这件事，是为了提高 ViewPager 滑动的流畅度，给用户带来更好的体验。那么懒加载应用的场景是什么呢？

> 当 ViewPager 中的 Fragment 内部有网络请求的时候，展示数据依赖于网络请求结果的时候，我们就应该考虑使用懒加载方案。

试想一下，你现在有多个页面包含在 Viewpager 中，并且页面中有大量数据需要展示，提前加载这些数据势必会对用户造成不必要的流量损失，也会损失一部分内存。

网上懒加载的例子很多，但是思想都是一样的利用 「setUserVisibleHint」来完成的，下边简单说下我的实现方法。

-------

###创建 BaseLazyLoadFragment 

创建 BaseLoadFragment 子类只需要实现对应网络请求方法，Base 类负责请求执行的时机。上文说到懒加载主要通过 「setUserVisibleHint」方法来判断对应的 Fragment 是否是用户正在交互的片段。

1. 对于 FragmentPagerAdapter 中所有 Fragment ，setUserVisibleHint 调用时机是在所有的生命周期之前，对于当前所处的 pager ，`isVisibleToUser = true`，预加载的 pager `isVisibleToUser = false`。

2. 由于 setUserVisibleHint 调用在生命周期之前，所以贸然在`isVisibleToUser = true`时候去请求数据，当网络回调回来的时候可能会导致页面没初始化完毕，而造成设置数据的时候空指针的现象。 

3. 解决方法当然是有的我们可以添加 view 是否创建完成的判断，如下面所示，我们在 inflate 完成 view 后将 `onCreatedView` 标志位置位，那么被缓存的页面下次在进入的时候就可以在 `setUserVisibleHint` 中开始网络请求。

4. 那么对于没有被缓存的页面就会在 `onCreatedView` 中进行网络请求。


你以为我现在肯定要放代码了，to naive ！ 对于懒加载我们还应该考虑到实际需求，如有些界面我们只需要在页面第一次显示的时候请求数据，当 Fragment 下次在可见的时候需要用户手动去刷新界面，当然这个需求是最常见的，也能满足大部分 Feed 流应用的需求。可是有些实时要求比较高的页面，比如股票类应用的持仓页面，钱数每分每秒都在变化那么只是首次进入刷新是远远不够的，这时候就需要每次用户可见的时候都刷新一次，当然这是举个例子，实际上在真正的股票类应用持仓页是 socket 刷新的。所以面对千变万化的需求就需要我们的懒加载基类能够支撑的起这两种方案，我们可以暴露两个方法给子类 Fragment 

>   * 1. 如果需求仅仅是想要在第一可见的时候自动刷新 就调用 requestData
>   * 2. 如果用户想要每次可见的时候都刷新，那么就调用 requestDataAutoRefresh

这样这两种需求都满足了。只需要根据指定页面复写满足需求的方法就好了。下面来看具体代码：


```
public abstract class LazyLoadBaseFragment extends Fragment {

    public static final String TAG = "Fragment";

    private View rootView = null;
    private boolean isViewCreated;
    private boolean isFirstVisible = true;
    private boolean isFragmentVisible;

    /**
     * 在 FragmentPageAdapter 中的 Fragment 都会走这里两次， 且比任何生命周期都要先走
     * 1. fragment 被预加载 此时为 false ，切换到此 Fragment  再次走这里赋值为 true
     * 2. 跨 tab 切换时候 首先也会走一次 false 然后去执行跳转之前 tab 的 setUserVisibleHint(false) 后去执行临近(左右) fragment 的
     * setUserVisibleHint(false) 最后会在调用一次当前 tab 的 setUserVisibleHint(true)
     * <p>
     * 懒加载是为了用户能在页面可见的时候在再去请求数据
     * <p>
     * 实际需求有：
     * 1. 用户第一可见自动加载数据，之后需要用户手动刷新去加载数据
     * 2. 用户每次可见的时候去自动刷新最新数据
     * <p>
     * <p>
     * 分析；
     * 我们可能认为Adapter 中的 Fragment 和 Activity 一样每次用户可见去调用 onResume 事实上并不是这样的，
     * 由于 FragmentPagerAdapter 存在预加载，onResume 事件当预加载的时候已经执行了，如果仅考虑两个 tab 之间相互
     * 切换那么 这两个 Fragment 之后只会调用 setUserVisibleHint 当参数为 true 的时候 Fragment 可见，false 的时候不可见。
     * <p>
     * 那么回到上述需求：我们可以提供两个方法 requestData requestDataEveryTime
     * <p>
     * 1. 如果需求仅仅是想要在第一可见的时候自动刷新 就调用 requestData
     * 2. 如果用户想要每次可见的时候都刷新，那么就调用 requestDataAutoRefresh
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        isFragmentVisible = isVisibleToUser;

        //当 View 创建完成切 用户可见的时候请求 且仅当是第一次对用户可见的时候请求自动数据
        if (isVisibleToUser && isViewCreated && isFirstVisible) {
            Log.e(TAG, "只有自动请求一次数据  requestData");
            requestData();
            isFirstVisible = false;

        }

        // 由于每次可见都需要刷新所以我们只需要判断  Fragment 展示在用户面面前了，view 初始化完成了 然后即可以请求数据了
        if (isVisibleToUser && isViewCreated) {
            // Log.e(TAG, "每次都可见数据  requestDataAutoRefresh");
            requestDataAutoRefresh();
        }

        if (!isVisibleToUser && isViewCreated) {
            stopRefresh();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(getLayoutRes(), container, false);
        }

        isViewCreated = true;

        initView(rootView);

        //Adapter 默认展示的那个 Fragment ，或者隔 tab 选中的时候，
        //由于没有预加载tab 在 setUserVisibleHint 中 isVisibleToUser = true 的时候 view 还没创建成功
        //即 isViewCreated = false 所以该 tab 的请求延迟到了 view 创建完成了
        //但是已经缓存的 Fragment 就不可以走这里了因为预加载 Fragment 在 onCreateView 中 isFragmentVisible 为 false
        if (isFragmentVisible && isFirstVisible) {
            Log.e(TAG, "Adapter 默认展示的那个 Fragment ，或者隔 tab 选中的时候  requestData 推迟到 onCreateView 后 ");
            requestData();
            isFirstVisible = false;
        }

        return rootView;
    }


    /**
     * 只有在 Fragment 第一次对用户可见的时候才去请求
     */
    protected void requestData() {
    }

    /**
     * 每次 Fragment 对用户可见都会去请求
     */
    protected void requestDataAutoRefresh() {

    }

    /**
     * 当 Fragment 不可见的时候停止某些轮询请求的时候调用该方法停止请求
     */
    protected void stopRefresh() {

    }

    /**
     * 返回布局 resId
     *
     * @return layoutId
     */
    protected abstract int getLayoutRes();


    /**
     * 初始化view
     *
     * @param rootView
     */
    protected abstract void initView(View rootView);

}
```



## 小结
我们这篇文章分享了 ViewPager 中 Fragment 的生命周期，以及如何实现 Fragment 与 ViewPager 连用时的 网络数据懒加载，下一篇我将会说明 Fragment 嵌套，以及 FragmentAdapter 嵌套 FragmentAdapter 时的生命周期与注意事项。 本文 github 链接





