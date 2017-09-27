package com.intfocus.hx.util;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intfocus.hx.R;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

/**
 * Created by CANC on 2017/7/24.
 */

public class ErrorUtils {

    /**
     * 数据空/网络错误 等情况得处理
     *
     * @param refreshLayout
     * @param llErrorView        错误提示view
     * @param llRetryView        是否可以点击刷新
     * @param tvErrorMsg         错误类型
     * @param ivError            错误展示图片
     * @param isEmpty            界面数据是否为空
     * @param isNetworkConnected 网络连接是否可用
     * @param emptyStr           数据为空时得提示
     */
    public static void viewProcessing(TwinklingRefreshLayout refreshLayout, LinearLayout llErrorView, LinearLayout llRetryView,
                                      String emptyStr, TextView tvErrorMsg, ImageView ivError, boolean isEmpty,
                                      final boolean isNetworkConnected, final int imageId, final ErrorLisenter lisenter) {
        if (!isNetworkConnected) {
            refreshLayout.finishRefreshing();
            refreshLayout.finishLoadmore();
        }
        if (refreshLayout != null) {
            refreshLayout.setVisibility(isEmpty ? View.GONE : View.VISIBLE);//只有界面数据为空的情况下才隐藏当前界面数据
        }
        if (llErrorView != null) {
            llErrorView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        }
        if (llRetryView != null) {
            llRetryView.setVisibility(isNetworkConnected ? View.GONE : View.GONE);//网络错误显示重试按钮
        }
        if (ivError != null) {
            ivError.setImageResource(isNetworkConnected ? imageId : R.drawable.icon_netbug);
        }
        if (tvErrorMsg != null) {
            tvErrorMsg.setText(isNetworkConnected ? emptyStr : "网络异常,点击屏幕重试");
        }
        if (lisenter != null) {
            llErrorView.setEnabled(true);
            if (!isNetworkConnected) {
                llErrorView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lisenter.retry();
                    }
                });
            }
        } else {
            llErrorView.setEnabled(false);
        }
    }

    public interface ErrorLisenter {
        void retry();
    }
}
