package com.intfocus.hx.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.intfocus.hx.R;
import com.intfocus.hx.mode.MeterMode;
import com.intfocus.hx.util.HttpUtil;
import com.intfocus.hx.util.ToastUtils;

/**
 * Created by liuruilin on 2017/5/11.
 */

public abstract class BaseSwipeHomeFragment extends BaseModeFragment<MeterMode> implements SwipeRefreshLayout.OnRefreshListener {
    public SwipeRefreshLayout mSwipeLayout;

    public void initSwipeLayout(View view) {
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeLayout.setDistanceToTriggerSync(300);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
    }

    @Override
    public void onRefresh() {
        if (HttpUtil.isConnected(getContext())) {
            getModel().requestData();
        } else {
            mSwipeLayout.setRefreshing(false);
            ToastUtils.INSTANCE.show(getContext(), "请检查网络");
        }
    }
}
