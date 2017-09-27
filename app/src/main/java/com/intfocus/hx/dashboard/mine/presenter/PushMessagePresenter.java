package com.intfocus.hx.dashboard.mine.presenter;

import android.content.Context;

import com.intfocus.hx.dashboard.mine.bean.PushMessageBean;
import com.intfocus.hx.dashboard.mine.model.OnPushMessageDataResultListener;
import com.intfocus.hx.dashboard.mine.model.PushMessageModelImpl;
import com.intfocus.hx.dashboard.mine.view.PushMessageView;

import java.util.List;

/**
 * ****************************************************
 * author: JamesWong
 * created on: 17/08/01 下午4:55
 * e-mail: PassionateWsj@outlook.com
 * name: 推送消息 presenter 类
 * desc: 暴露 loadData 方法，通知 model 加载数据
 * ****************************************************
 */

public class PushMessagePresenter {
    /**
     * 回调给 ShowPushMessageActivity 的接口
     */
    private PushMessageView mView;
    /**
     * 推送消息数据处理类
     */
    private PushMessageModelImpl mModel;
    private Context mContext;
    /**
     * 当前用户的id
     */
    private int mUserId;

    public PushMessagePresenter(Context context, PushMessageView pushMessageView,int userId) {
        mContext = context;
        mView = pushMessageView;
        mModel = new PushMessageModelImpl();
        mUserId = userId;
    }

    /**
     * 通知 model 请求数据
     */
    public void loadData() {
        mModel.loadData(mContext,new OnPushMessageDataResultListener() {
            @Override
            public void onPushMessageDataResultSuccess(List<PushMessageBean> data) {
                mView.onResultSuccess(data);
            }

            @Override
            public void onPushMessageDataResultFailure() {
                mView.onResultFailure();
            }

        },mUserId);
    }
}
