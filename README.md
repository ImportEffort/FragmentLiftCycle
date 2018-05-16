
![](https://ws2.sinaimg.cn/large/006tNc79ly1fqlv6c6dfyj30zk0k0myz.jpg)

# 多层嵌套后的 Fragment 懒加载


印象中从 Feed 流应用流行开始，`Fragment` 懒加载变成了一个大家都需要关注的开发知识，关于 `Fragment` 的懒加载，网上有很多例子，GitHub 上也有很多例子，就连我自己在一年前也写过相关的文章。但是之前的应用可能最多的是一层 `Activity + ViewPager` 的 UI 层次，但是随着页面越来越复杂，越来越多的应用首页一个页面外层是一个 `ViewPager` 内部可能还嵌套着一层 `ViewPager`，这是之前的懒加载就可能不那么好用了。本文对于多层 `ViewPager` 的嵌套使用过程中，`Fragment` 主要的三个状态：第一次可见，每次可见，每次不可见，提供解决方案。

## 为什么要使用懒加载

在我们开发中经常会使用 `ViewPager + Fragment` 来创建多 tab 的页面，此时在 `ViewPager` 内部默认会帮我们缓存当页面前后两个页面的 `Fragment` 内容，如果使用了 `setOffscreenPageLimit` 方法，那么 `ViewPager` 初始化的时候将会缓存对应参数个 Fragment。为了增加用户体验我们往往会使用该方法来保证加载过的页面不被销毁，并留离开 tab 之前的状态（列表滑动距离等），而我们在使用 `Fragment` 的时候往往在创建完 `View` 后，就会开始网络请求等操作，如果存在上述的需求时，懒加载就显得尤为重要了，不仅可以节省用户流量，还可以在提高应用性能，给用户带来更加的体验。

`ViewPager + Fragment` 的懒加载实质上我们就在做三件事，就可以将上边所说的效果实现，那么就是找到每个 `Fragment` 第一对用户可见的时机，和每次 Fragment 对用户可见时机，以及每次 `Framgment` 对用户不可见的时机，来暴露给实现实现类做对应的网络请求或者网络请求中断时机。下面我们就来从常见的几种 UI 结构上一步步实现无论嵌套多少层，无论开发者使用的 `hide show` 还是 `ViewPager` 嵌套都能准确获取这三种状态的时机的一种懒加载实现方案。

## 单层 ViewPager + Fragment 懒加载

我们都知道 Fragment 生命周期按先后顺序有

> onAttach -> onCreate -> onCreatedView -> onActivityCreated -> onStart -> onResume -> onPause -> onStop -> onDestroyView -> onDestroy -> onDetach

对于 ViewPager + Fragment 的实现我们需要关注的几个生命周期有：
> onCreatedView + onActivityCreated + onResume + onPause + onDestroyView

以及非生命周期函数：

> setUserVisibleHint +  onHiddenChanged

对于单层 ViewPager + Fragment 可能是我们最常用的页面结构了，如网易云音乐的首页顶部的是三个 tab ，我们那网易云音乐作为例子：

![](https://ws2.sinaimg.cn/large/006tNc79ly1fqlvbklsvij30c00lcjrs.jpg)

对于这种 ViewPager + Fragment 结构，我们使用的过程中一般只包含是 3 种情况分别是：

1. 使用 `FragmentPagerAdapter` ，`FragmentPagerStateAdapter`不设置 `setOffscreenPageLimit`
   -  左右滑动页面，每次只缓存下一个 Pager ，和上一个 Pager
   -  间隔的点击 tab 如从位于 tab1 的时候直接选择 tab3 或 tab4

2. 使用 `FragmentPagerAdapter`，`FragmentPagerStateAdapter` 设置  `setOffscreenPageLimit` 为 tab 总数
   - 左右滑动页面，每次只缓存下一个 Pager ，和上一个 Pager
   - 间隔的点击 tab 如从位于 tab1 的时候直接选择 tab3 或 tab4
3. 进入其他页面或者用户按 home 键回到桌面，当前 ViewPager 页面变成不见状态。

对于 `FragmentPagerAdapter` 和 `FragmentPagerStateAdapter` 的区别在于在于，前者在 `Fragment` 不见的时候将不会 `detach` ，而后者将会销毁 `Fragment` 并 `detach` 掉。

实际上这也是所有 `ViewPager` 的操作情况。

- 第一种情况不设置 `setOffscreenPageLimit` 左右滑动页面/或者每次选择相邻 tab 的情况  `FragmentPagerAdapter` 和 `FragmentPagerStateAdapter` 有所区别

```
 BottomTabFragment1  setUserVisibleHint false
 BottomTabFragment2  setUserVisibleHint false
 BottomTabFragment1  setUserVisibleHint true

 BottomTabFragment1  onCreateView
 BottomTabFragment1  onActivityCreated
 BottomTabFragment1  onResume

 BottomTabFragment2  onCreateView
 BottomTabFragment2  onActivityCreated
 BottomTabFragment2  onResume

 //滑动到 Tab 2
 BottomTabFragment3  setUserVisibleHint false
 BottomTabFragment1  setUserVisibleHint false
 BottomTabFragment2  setUserVisibleHint true

 BottomTabFragment3  onCreateView
 BottomTabFragment3  onActivityCreated
 BottomTabFragment3 onResume

 //跳过 Tab3 直接选择 Tab4
 BottomTabFragment4  setUserVisibleHint false
 BottomTabFragment2  setUserVisibleHint false
 BottomTabFragment4  setUserVisibleHint true

 BottomTabFragment4  onCreateView
 BottomTabFragment4  onActivityCreated
 BottomTabFragment4  onResume

 BottomTabFragment2  onPause
 BottomTabFragment2  onDestroyView

 // FragmentPagerStateAdapter 会走一下两个生命周期方法
 BottomTabFragment2  onDestroy
 BottomTabFragment2  onDetach

 BottomTabFragment1  onPause
 BottomTabFragment1  onDestroyView

 // FragmentPagerStateAdapter 会走一下两个生命周期方法
 BottomTabFragment1  onDestroy
 BottomTabFragment1  onDetach

 // 用户回到桌面 再回到当前 APP 打开其他页面当前页面的生命周期也是这样的
 BottomTabFragment3  onPause
 BottomTabFragment4  onPause
 BottomTabFragment3  onStop
 BottomTabFragment4  onStop

 BottomTabFragment3 onResume
 BottomTabFragment4 onResume


```

-  第二种情况设置 setOffscreenPageLimit 为 Pager 的个数时候，左右滑动页面/或者每次选择相邻 tab 的情况  FragmentPagerAdapter 和 FragmentPagerStateAdapter 没有区别

```
  BottomTabFragment1  setUserVisibleHint false
  BottomTabFragment2  setUserVisibleHint false
  BottomTabFragment3  setUserVisibleHint false
  BottomTabFragment4  setUserVisibleHint false

  BottomTabFragment1  setUserVisibleHint true
  BottomTabFragment1  onCreateView
  BottomTabFragment1  onActivityCreated
  BottomTabFragment1 onResume

  BottomTabFragment2  onCreateView
  BottomTabFragment2  onActivityCreated

  BottomTabFragment3  onCreateView
  BottomTabFragment3  onActivityCreated

  BottomTabFragment4  onCreateView
  BottomTabFragment4  onActivityCreated

  BottomTabFragment2 onResume
  BottomTabFragment3 onResume
  BottomTabFragment4 onResume

  //选择 Tab2
  BottomTabFragment1  setUserVisibleHint false
  BottomTabFragment2  setUserVisibleHint true

 //跳过 Tab3 直接选择 Tab4
  BottomTabFragment2  setUserVisibleHint false
  BottomTabFragment4  setUserVisibleHint true

  // 用户回到桌面 再回到当前 APP 打开其他页面当前页面的生命周期也是这样的
 BottomTabFragment1  onPause
 BottomTabFragment2  onPause
 BottomTabFragment3  onPause
 BottomTabFragment4  onPause

 BottomTabFragment1 onResume
 BottomTabFragment2 onResume
 BottomTabFragment3 onResume
 BottomTabFragment4 onResume

```

可以看出第一次执行 `setUserVisibleHint(boolean isVisibleToUser)` 除了可见的 Fragment 外都为 false，还可以看出除了这一点不同以外，所有的 Fragment 都走到了生命周期 onResume 阶段。而选择相邻 tab 的时候已经初始化完成的Fragment 并不再重新走生命周期方法，只是 `setUserVisibleHint(boolean isVisibleToUser)` 为 true。当用户进入其他页面的时候所有 ViewPager 缓存的 Fragment 都会调用 onPause 生命周期函数，当再次回到当前页面的时候都会调用 onResume。

能发现这一点，其实对于单层 ViewPager 嵌套 Fragment 可见状态的把握其实已经很明显了。下面给出我的解决方案：

1. 对于 Fragment 可见状态的判断需要设置两个标志位 ，Fragment View 创建完成的标志位 `isViewCreated` 和 `Fragment` 第一次创建的标志位 `mIsFirstVisible`
2. 为了获得 Fragment 不可见的状态，和再次回到可见状态的判断，我们还需要增加一个 `currentVisibleState` 标志位，该标志位在 onResume 中和 onPause 中结合 `getUserVisibleHint` 的返回值来决定是否应该回调可见和不可见状态函数。



整个可见过程判断逻辑如下图所示

![](https://ws4.sinaimg.cn/large/006tNc79ly1fqlucp8t7ej31am0vy0te.jpg)

![](https://ws1.sinaimg.cn/large/006tNc79ly1fqlufcb5u4j31k60wu3zg.jpg)

接下来我们就来看下具体实现：

```
public abstract class LazyLoadBaseFragment extends BaseLifeCircleFragment {

    protected View rootView = null;


    private boolean mIsFirstVisible = true;

    private boolean isViewCreated = false;

    private boolean currentVisibleState = false;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //走这里分发可见状态情况有两种，1. 已缓存的 Fragment 被展示的时候 2. 当前 Fragment 由可见变成不可见的状态时
        // 对于默认 tab 和 间隔 checked tab 需要等到 isViewCreated = true 后才可以通过此通知用户可见，
        // 这种情况下第一次可见不是在这里通知 因为 isViewCreated = false 成立，可见状态在 onActivityCreated 中分发
        // 对于非默认 tab，View 创建完成  isViewCreated =  true 成立，走这里分发可见状态，mIsFirstVisible 此时还为 false  所以第一次可见状态也将通过这里分发
        if (isViewCreated){
            if (isVisibleToUser && !currentVisibleState) {
                dispatchUserVisibleHint(true);
            }else if (!isVisibleToUser && currentVisibleState){
                dispatchUserVisibleHint(false);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 将 View 创建完成标志位设为 true
        isViewCreated = true;
        // 默认 Tab getUserVisibleHint() = true !isHidden() = true
        // 对于非默认 tab 或者非默认显示的 Fragment 在该生命周期中只做了 isViewCreated 标志位设置 可见状态将不会在这里分发
        if (!isHidden() && getUserVisibleHint()){
            dispatchUserVisibleHint(true);
        }

    }


    /**
     * 统一处理 显示隐藏  做两件事
     * 设置当前 Fragment 可见状态 负责在对应的状态调用第一次可见和可见状态，不可见状态函数
     * @param visible
     */
    private void dispatchUserVisibleHint(boolean visible) {

        currentVisibleState = visible;

        if (visible) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false;
                onFragmentFirstVisible();
            }
            onFragmentResume();
        }else {
            onFragmentPause();
        }
    }

    /**
     * 该方法与 setUserVisibleHint 对应，调用时机是 show，hide 控制 Fragment 隐藏的时候，
     * 注意的是，只有当 Fragment 被创建后再次隐藏显示的时候才会调用，第一次 show 的时候是不会回调的。
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
            dispatchUserVisibleHint(false);
        }else {
            dispatchUserVisibleHint(true);
        }
    }

    /**
     * 需要再 onResume 中通知用户可见状态的情况是在当前页面再次可见的状态 !mIsFirstVisible 可以保证这一点，
     * 而当前页面 Activity 可见时所有缓存的 Fragment 都会回调 onResume
     * 所以我们需要区分那个Fragment 位于可见状态
     * (!isHidden() && !currentVisibleState && getUserVisibleHint()）可条件可以判定哪个 Fragment 位于可见状态
     */
    @Override
    public void onResume() {
        super.onResume();
        if (!mIsFirstVisible){
            if (!isHidden() && !currentVisibleState && getUserVisibleHint()){
                dispatchUserVisibleHint(true);
            }
        }
    }

    /**
     * 当用户进入其他界面的时候所有的缓存的 Fragment 都会 onPause
     * 但是我们想要知道只是当前可见的的 Fragment 不可见状态，
     * currentVisibleState && getUserVisibleHint() 能够限定是当前可见的 Fragment
     */
    @Override
    public void onPause() {
        super.onPause();

        if (currentVisibleState && getUserVisibleHint()){
            dispatchUserVisibleHint(false);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //当 View 被销毁的时候我们需要重新设置 isViewCreated mIsFirstVisible 的状态
        isViewCreated = false;
        mIsFirstVisible = true;
    }

    /**
     * 对用户第一次可见
     */
    public void onFragmentFirstVisible(){
        LogUtils.e(getClass().getSimpleName() + "  ");
    }

    /**
     *   对用户可见
     */
    public void onFragmentResume(){
        LogUtils.e(getClass().getSimpleName() + "  对用户可见");
    }

   /**
     *  对用户不可见
     */
    public void onFragmentPause(){
        LogUtils.e(getClass().getSimpleName() + "  对用户不可见");
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        if (rootView == null) {
            rootView = inflater.inflate(getLayoutRes(), container, false);
        }

        initView(rootView);

        return rootView;
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

我们使之前的 Fragment 改为继承 `LazyLoadBaseFragment` 打印 log 可以看出：


```
//默认选中第一 Tab
 BottomTabFragment1  setUserVisibleHint false
 BottomTabFragment2  setUserVisibleHint false
 BottomTabFragment3  setUserVisibleHint false
 BottomTabFragment4  setUserVisibleHint false
 BottomTabFragment1  setUserVisibleHint true

 BottomTabFragment1  onCreateView
 BottomTabFragment1  onActivityCreated
 BottomTabFragment1  对用户第一次可见
 BottomTabFragment1  对用户可见
 BottomTabFragment1  onResume

 BottomTabFragment2  onCreateView
 BottomTabFragment2  onActivityCreated

 BottomTabFragment3  onCreateView
 BottomTabFragment3  onActivityCreated

 BottomTabFragment4  onCreateView
 BottomTabFragment4  onActivityCreated

 BottomTabFragment2 onResume
 BottomTabFragment3 onResume
 BottomTabFragment4 onResume

 //滑动选中 Tab2
 BottomTabFragment1  setUserVisibleHint false
 BottomTabFragment1  对用户不可见

 BottomTabFragment2  setUserVisibleHint true
 BottomTabFragment2  对用户第一次可见
 BottomTabFragment2  对用户可见

 //间隔选中 Tab4
 BottomTabFragment2  setUserVisibleHint false
 BottomTabFragment2  对用户不可见

 BottomTabFragment4  setUserVisibleHint true
 BottomTabFragment4  对用户第一次可见
 BottomTabFragment4  对用户可见


 // 回退到桌面
 BottomTabFragment1  onPause
 BottomTabFragment2  onPause
 BottomTabFragment3  onPause
 BottomTabFragment4  onPause
 BottomTabFragment4  对用户不可见

 BottomTabFragment1  onStop
 BottomTabFragment2  onStop
 BottomTabFragment3  onStop
 BottomTabFragment4  onStop

 // 再次进入 APP
 BottomTabFragment1 onResume
 BottomTabFragment2 onResume
 BottomTabFragment3 onResume
 BottomTabFragment4 onResume
 BottomTabFragment4  对用户可见
```

上述 log 只演示了如何 ViewPager 中的函数打印，由于 hide show 方法显示隐藏的 Fragment 有人可能认为不需要懒加载这个东西，如果说从创建来说的确是这样的，但是如果说所有的 Fragment 已经 add 进 Activity 中，此时 Activity 退到后台，所有的 Fragment 都会调用 onPause ，并且在其进入前台的前台统一会回调 onResume, 如果我们在 Resume 中做了某些操作，那么不可见的 Fragment 也会执行，势必也是个浪费。所以这里的懒加载吧 hide show 的展示方法也考虑进去。

对于无嵌套的 ViewPager ，懒加载还是相对简单的。但是对于ViewPager 嵌套 ViewPager 的情况可能就出现一些我们意料不到的情况。

## 双层 ViewPager 嵌套的懒加载实现

对于双层  ViewPager 嵌套我们也拿网易云来举例:


![](https://ws1.sinaimg.cn/large/006tNc79ly1fqlv9pop7oj30c00lct9v.jpg)

可以看出顶层的第二 tab 内部又是一个 ViewPager ，那么我们试着按照我们之前的方案打印一下生命周期过程：


```
 BottomTabFragment1  setUserVisibleHint false
 BottomTabFragment2  setUserVisibleHint false

 BottomTabFragment1  setUserVisibleHint true
 BottomTabFragment1  onCreateView
 BottomTabFragment1  onActivityCreated
 BottomTabFragment1  对用户第一次可见
 BottomTabFragment1  对用户可见

 BottomTabFragment1 onResume
 BottomTabFragment2  onCreateView
 BottomTabFragment2  onActivityCreated
 BottomTabFragment2 onResume

 Bottom2InnerFragment1  setUserVisibleHint false
 Bottom2InnerFragment2  setUserVisibleHint false
 Bottom2InnerFragment1  setUserVisibleHint true

 //注意这里 位于第二个Tab 中的 ViewPager 中的第一个 Tab 也走了可见，而它本身并不可见
 Bottom2InnerFragment1  onCreateView
 Bottom2InnerFragment1  onActivityCreated
 Bottom2InnerFragment1  对用户第一次可见
 Bottom2InnerFragment1  对用户可见
 Bottom2InnerFragment1  onResume

 Bottom2InnerFragment2  onCreateView
 Bottom2InnerFragment2  onActivityCreated
 Bottom2InnerFragment2 onResume
```

咦奇怪的事情发生了，对于外层 ViewPager 的第二个 tab 默认是不显示的，为什么内部 ViewPager 中的 Bottom2InnerFragment1 却走了可见了状态回调。是不是 onActivityCreated 中的写法有问题，`!isHidden() && getUserVisibleHint()`  getUserVisibleHint() 方法通过 log 打印发现在 Bottom2InnerFragment1 onActivityCreated 时候， ` Bottom2InnerFragment1  setUserVisibleHint true`的确是 true。所以才会走到分发可见事件中。

我们再回头看下上述的生命周期的打印，可以发现，事实上作为父 Fragment 的 BottomTabFragment2 并没有分发可见事件，他通过 getUserVisibleHint() 得到的结果为 false，首先我想到的是能在负责分发事件的方法中判断一下当前父 fragment 是否可见，如果父 fragment 不可见我们就不进行可见事件的分发，我们试着修改 `dispatchUserVisibleHint` 如下面所示：

```
  private void dispatchUserVisibleHint(boolean visible) {
        //当前 Fragment 是 child 时候 作为缓存 Fragment 的子 fragment getUserVisibleHint = true
        //但当父 fragment 不可见所以 currentVisibleState = false 直接 return 掉
        if (visible && isParentInvisible()) return;

        currentVisibleState = visible;

        if (visible) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false;
                onFragmentFirstVisible();
            }
            onFragmentResume();
        } else {
            onFragmentPause();
        }
    }

 /**
  * 用于分发可见时间的时候父获取 fragment 是否隐藏
  * @return true fragment 不可见， false 父 fragment 可见
  */
 private boolean isParentInvisible() {
    LazyLoadBaseFragment fragment = (LazyLoadBaseFragment) getParentFragment();
    return fragment != null && !fragment.isSupportVisible();

 }

private boolean isSupportVisible() {
    return currentVisibleState;
}
```
通过日志打印我们发现这似乎起作用了：

```
 BottomTabFragment1  setUserVisibleHint false
 BottomTabFragment2  setUserVisibleHint false

 BottomTabFragment1  setUserVisibleHint true
 BottomTabFragment1  onCreateView
 BottomTabFragment1  onActivityCreated
 BottomTabFragment1  对用户第一次可见
 BottomTabFragment1  对用户可见

 BottomTabFragment1 onResume
 BottomTabFragment2  onCreateView
 BottomTabFragment2  onActivityCreated
 BottomTabFragment2 onResume

 Bottom2InnerFragment1  setUserVisibleHint false
 Bottom2InnerFragment2  setUserVisibleHint false
 Bottom2InnerFragment1  setUserVisibleHint true

 Bottom2InnerFragment1  onCreateView
 Bottom2InnerFragment1  onActivityCreated
 Bottom2InnerFragment1  onResume

 Bottom2InnerFragment2  onCreateView
 Bottom2InnerFragment2  onActivityCreated
 Bottom2InnerFragment2 onResume

 //滑动到第二个 tab
 BottomTabFragment3  setUserVisibleHint false
 BottomTabFragment1  setUserVisibleHint false
 BottomTabFragment1  对用户不可见

 BottomTabFragment2  setUserVisibleHint true
 BottomTabFragment2  对用户第一次可见
 BottomTabFragment2  对用户可见

 BottomTabFragment3  onCreateView
 BottomTabFragment3  onActivityCreated
 BottomTabFragment3 onResume
```

但是我们又发现了新的问题，当我们滑动到第二个 tab 时候，无疑我们期望得到第二个 tab 中内层 ViewPager 第一个 tab 中 fragment 状态的可见状态，但是从上边的 log 可以发现我们并没有获得其可见状态的打印，问题出当外层 ViewPager 初始化的时候我们已经经历了 Bottom2InnerFragment1 的初始化，而我们在 dispatchUserVisibleHint 做了拦截，导致其无法分发可见事件，当其真正可见的时候却发现事件函数并不会再次被调用了。

本着坚信一切困难都是纸老虎的社会主义光荣理念，我404了一下，发现网上极少的嵌套 fragment 懒加载的文章中，大多都采用了，**在父 Fragment 可见的时候，分发自己可见状态的同时，把自己的可见状态通知子 Fragment**，对于可见状态的 生命周期调用顺序，父 Fragment总是优先于子 Fragment，所以我们在 Fragment 分发事件的时候，可以在上述拦截子 Fragment 事件分发后，当在父 Fragment 第一可见的时候，通知子 Fragment 你也可见了。所以我再次修改 dispatchUserVisibleHint，在父 Fragment 分发完成自己的可见事件后，让子 Fragment 再次调用自己的可见事件分发方法，这次 `isParentInvisible()` 将会返回 false ，也就是可见状态将会正确分发。

```
private void dispatchUserVisibleHint(boolean visible) {
   //当前 Fragment 是 child 时候 作为缓存 Fragment 的子 fragment getUserVisibleHint = true
   //但当父 fragment 不可见所以 currentVisibleState = false 直接 return 掉
   if (visible && isParentInvisible()) return;

   currentVisibleState = visible;

   if (visible) {
       if (mIsFirstVisible) {
           mIsFirstVisible = false;
           onFragmentFirstVisible();
       }
       onFragmentResume();
       //可见状态的时候内层 fragment 生命周期晚于外层 所以在 onFragmentResume 后分发
       dispatchChildVisibleState(true);
   } else {
       onFragmentPause();
       dispatchChildVisibleState(false);

   }
}

 private void dispatchChildVisibleState(boolean visible) {
       FragmentManager childFragmentManager = getChildFragmentManager();
       List<Fragment> fragments = childFragmentManager.getFragments();
       if (!fragments.isEmpty()) {
           for (Fragment child : fragments) {
               // 如果只有当前 子 fragment getUserVisibleHint() = true 时候分发事件，并将 也就是我们上面说的 Bottom2InnerFragment1
               if (child instanceof LazyLoadBaseFragment && !child.isHidden() && child.getUserVisibleHint()) {
                   ((LazyLoadBaseFragment) child).dispatchUserVisibleHint(visible);
               }
           }
       }
    }
```
 `dispatchChildVisibleState` 方法通过 childFragmentManager 获取当前 Fragment 中所有的子 Fragment 并通过判断 `child.getUserVisibleHint()` 的返回值，判断是否应该通知子 Fragment 不可见，同理在父 Fragment 真正可见的时候，我们也会通过该方法，通知`child.getUserVisibleHint() = true` 的子 Fragment 你可见。

我们再次打印可以看出经过这次调整内层 Fragment 已经可以准确地拿到自己第一次可见状态了。


```
   BottomTabFragment3  setUserVisibleHint false
   BottomTabFragment1  setUserVisibleHint false
   BottomTabFragment1  对用户不可见

   BottomTabFragment2  setUserVisibleHint true
   BottomTabFragment2  对用户第一次可见
   BottomTabFragment2  对用户可见

   Bottom2InnerFragment1  对用户第一次可见
   Bottom2InnerFragment1  对用户可见

   BottomTabFragment3  onCreateView
   BottomTabFragment3  onActivityCreated
   BottomTabFragment3 onResume
```

当我以为纸老虎一进被我大打败的时候，我按了下 home 键看了条微信，然后发现 log 打印如下：


```
 BottomTabFragment1  onPause

 //Bottom2InnerFragment1 第一不可见回调
 Bottom2InnerFragment1  onPause
 Bottom2InnerFragment1  对用户不可见

 Bottom2InnerFragment2  onPause
 BottomTabFragment2  onPause

 BottomTabFragment2  对用户不可见
 //Bottom2InnerFragment1 第二次不可见回调
 Bottom2InnerFragment1  对用户不可见
 BottomTabFragment3  onPause
 BottomTabFragment1  onStop

 Bottom2InnerFragment1  onStop
 Bottom2InnerFragment2  onStop

 BottomTabFragment2  onStop
 BottomTabFragment3  onStop
```

这又是啥情况？ 为啥回调了两次，我连微信都忘了回就开始回忆之前分发可见事件的代码，可见的时候时候没问题，为什么不可见会回调两次？后来发现问题出现在事件分发的顺序上。

通过日志打印我们也可以看出，对于可见状态的生命周期调用顺序，父 Fragment总是优先于子 Fragment，而对于不可见事件，内部的 Fragment 生命周期总是先于外层 Fragment。所以第一的时候 Bottom2InnerFragment1 调用自身的 `dispatchUserVisibleHint` 方法分发了不可见事件，作为父 Fragment 的BottomTabFragment2 分发不可见的时候，又会再次调用 `dispatchChildVisibleState` ，导致子 Fragment 再次调用自己的 `dispatchUserVisibleHint`  再次调用了一次 `onFragmentPause();`。

解决办法也很简单，还记得 `currentVisibleState` 这个变量么？ 表示当前 Fragment 的可见状态，如果当前的 Fragment 要分发的状态与 currentVisibleState 相同我们就没有必要去做分发了。

我们知道子 Fragment 优于父 Fragment回调本方法 currentVisibleState 置位 false，当前不可见，我们可以当父 dispatchChildVisibleState 的时候第二次回调本方法 visible = false 所以此处 visible 将直接返回。

```
private void dispatchUserVisibleHint(boolean visible) {

   if (visible && isParentInvisible())
     return;

    // 此处是对子 Fragment 不可见的限制，因为 子 Fragment 先于父 Fragment回调本方法 currentVisibleState 置位 false
   // 当父 dispatchChildVisibleState 的时候第二次回调本方法 visible = false 所以此处 visible 将直接返回
   if (currentVisibleState == visible) {
       return;
   }

   currentVisibleState = visible;

   if (visible) {
       if (mIsFirstVisible) {
           mIsFirstVisible = false;
           onFragmentFirstVisible();
       }
       onFragmentResume();
       //可见状态的时候内层 fragment 生命周期晚于外层 所以在 onFragmentResume 后分发
       dispatchChildVisibleState(true);
   } else {
       onFragmentPause();
       dispatchChildVisibleState(false);
   }
}
```

对于 Hide And show 方法显示的 Fragment 验证这里讲不在过多赘述，上文也说了，对这种 Fragment 展示方法，我们更需要关注的是 hide 的时候， onPause 和 onResume 再次隐藏显示的的时候。改方法的验证可以通过下载 Demo 查看 log。[Demo 地址](https://github.com/ImportEffort/FragmentLiftCycle)



## 最终的实现方案


下面是完整 `LazyLoadBaseFragment` 实现方案：也可以直接戳此下载文件 [LazyLoadBaseFragment.java](https://github.com/ImportEffort/FragmentLiftCycle/blob/c1b7be301282ed381075575814065e369870911a/app/src/main/java/com/wangshijia/www/fragmentliftcycle/LazyLoadBaseFragment.java)

```
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author wangshijia
 * @date 2018/2/2
 * Fragment 第一次可见状态应该在哪里通知用户 在 onResume 以后？
 */
public abstract class LazyLoadBaseFragment extends BaseLifeCircleFragment {

    protected View rootView = null;


    private boolean mIsFirstVisible = true;

    private boolean isViewCreated = false;

    private boolean currentVisibleState = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (rootView == null) {
            rootView = inflater.inflate(getLayoutRes(), container, false);
        }
        initView(rootView);
        return rootView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 对于默认 tab 和 间隔 checked tab 需要等到 isViewCreated = true 后才可以通过此通知用户可见
        // 这种情况下第一次可见不是在这里通知 因为 isViewCreated = false 成立,等从别的界面回到这里后会使用 onFragmentResume 通知可见
        // 对于非默认 tab mIsFirstVisible = true 会一直保持到选择则这个 tab 的时候，因为在 onActivityCreated 会返回 false
        if (isViewCreated) {
            if (isVisibleToUser && !currentVisibleState) {
                dispatchUserVisibleHint(true);
            } else if (!isVisibleToUser && currentVisibleState) {
                dispatchUserVisibleHint(false);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isViewCreated = true;
        // !isHidden() 默认为 true  在调用 hide show 的时候可以使用
        if (!isHidden() && getUserVisibleHint()) {
            dispatchUserVisibleHint(true);
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtils.e(getClass().getSimpleName() + "  onHiddenChanged dispatchChildVisibleState  hidden " + hidden);

        if (hidden) {
            dispatchUserVisibleHint(false);
        } else {
            dispatchUserVisibleHint(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsFirstVisible) {
            if (!isHidden() && !currentVisibleState && getUserVisibleHint()) {
                dispatchUserVisibleHint(true);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // 当前 Fragment 包含子 Fragment 的时候 dispatchUserVisibleHint 内部本身就会通知子 Fragment 不可见
        // 子 fragment 走到这里的时候自身又会调用一遍 ？
        if (currentVisibleState && getUserVisibleHint()) {
            dispatchUserVisibleHint(false);
        }
    }


    /**
     * 统一处理 显示隐藏
     *
     * @param visible
     */
    private void dispatchUserVisibleHint(boolean visible) {
        //当前 Fragment 是 child 时候 作为缓存 Fragment 的子 fragment getUserVisibleHint = true
        //但当父 fragment 不可见所以 currentVisibleState = false 直接 return 掉
        // 这里限制则可以限制多层嵌套的时候子 Fragment 的分发
        if (visible && isParentInvisible()) return;

       //此处是对子 Fragment 不可见的限制，因为 子 Fragment 先于父 Fragment回调本方法 currentVisibleState 置位 false
       // 当父 dispatchChildVisibleState 的时候第二次回调本方法 visible = false 所以此处 visible 将直接返回
        if (currentVisibleState == visible) {
            return;
        }

        currentVisibleState = visible;

        if (visible) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false;
                onFragmentFirstVisible();
            }
            onFragmentResume();
            dispatchChildVisibleState(true);
        } else {
            dispatchChildVisibleState(false);
            onFragmentPause();
        }
    }

    /**
     * 用于分发可见时间的时候父获取 fragment 是否隐藏
     *
     * @return true fragment 不可见， false 父 fragment 可见
     */
    private boolean isParentInvisible() {
        LazyLoadBaseFragment fragment = (LazyLoadBaseFragment) getParentFragment();
        return fragment != null && !fragment.isSupportVisible();

    }

    private boolean isSupportVisible() {
        return currentVisibleState;
    }

    /**
     * 当前 Fragment 是 child 时候 作为缓存 Fragment 的子 fragment 的唯一或者嵌套 VP 的第一 fragment 时 getUserVisibleHint = true
     * 但是由于父 Fragment 还进入可见状态所以自身也是不可见的， 这个方法可以存在是因为庆幸的是 父 fragment 的生命周期回调总是先于子 Fragment
     * 所以在父 fragment 设置完成当前不可见状态后，需要通知子 Fragment 我不可见，你也不可见，
     * <p>
     * 因为 dispatchUserVisibleHint 中判断了 isParentInvisible 所以当 子 fragment 走到了 onActivityCreated 的时候直接 return 掉了
     * <p>
     * 当真正的外部 Fragment 可见的时候，走 setVisibleHint (VP 中)或者 onActivityCreated (hide show) 的时候
     * 从对应的生命周期入口调用 dispatchChildVisibleState 通知子 Fragment 可见状态
     *
     * @param visible
     */
    private void dispatchChildVisibleState(boolean visible) {
        FragmentManager childFragmentManager = getChildFragmentManager();
        List<Fragment> fragments = childFragmentManager.getFragments();
        if (!fragments.isEmpty()) {
            for (Fragment child : fragments) {
                if (child instanceof LazyLoadBaseFragment && !child.isHidden() && child.getUserVisibleHint()) {
                    ((LazyLoadBaseFragment) child).dispatchUserVisibleHint(visible);
                }
            }
        }
    }

    public void onFragmentFirstVisible() {
        LogUtils.e(getClass().getSimpleName() + "  对用户第一次可见");

    }

    public void onFragmentResume() {
        LogUtils.e(getClass().getSimpleName() + "  对用户可见");
    }

    public void onFragmentPause() {
        LogUtils.e(getClass().getSimpleName() + "  对用户不可见");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
        mIsFirstVisible = true;
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

## 总结

对于 ViewPager Fragment 懒加载网上文章可能已经很多了，但是对于多层 ViewPager + Fragment 嵌套的文章并不是很多，上文还原了我自己对 Fragment 懒加载的探索过程，目前该基类已经应用于公司项目中，相信随着业务的复杂可能有的地方还有可能该方法存在缺陷，如果大家在使用过程中有问题也请给我留言。

> 最后感谢 [YoKeyword](https://github.com/YoKeyword) 大神的[Fragmentation](https://github.com/YoKeyword/Fragmentation) 提供的思路，一个很好的单 Activity 多 Fragment 库。



