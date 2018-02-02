package com.wangshijia.www.fragmentliftcycle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author wangshijia
 * @date 2018/2/2
 * 懒加载 Fragment 提供两个发送请求的方法：
 * requestData 当且仅当 Fragment 第一可见的时候自动刷新
 * requestDataAutoRefresh 每次 Fragment 对用户可见的时候都自动刷新
 */
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
//        Fragment parentFragment = getParentFragment();
//        if (parentFragment != null) {
//             = parentFragment.getUserVisibleHint();
//        }
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


    //增加一份需求就是当 Fragment 对用户不可见的时候时候停止自动刷新


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
