package com.intfocus.hx.dashboard.mine.model;

import android.content.Context;

/**
 * ****************************************************
 * author: JamesWong
 * created on: 17/08/01 下午4:57
 * e-mail: PassionateWsj@outlook.com
 * name:
 * desc:
 * ****************************************************
 */

public interface PushMessageModel {
    void loadData(Context context, OnPushMessageDataResultListener listener,int userId);
}
