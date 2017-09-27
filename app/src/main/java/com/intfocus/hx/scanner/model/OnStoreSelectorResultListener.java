package com.intfocus.hx.scanner.model;

import com.intfocus.hx.data.response.scanner.StoreItem;

import java.util.List;

/**
 * ****************************************************
 * author: JamesWong
 * created on: 17/08/22 上午10:01
 * e-mail: PassionateWsj@outlook.com
 * name:
 * desc:
 * ****************************************************
 */

public interface OnStoreSelectorResultListener {
    void onResultFailure(Throwable e);
    void onResultSuccess(List<StoreItem> data);
}
