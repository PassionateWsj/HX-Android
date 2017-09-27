package com.intfocus.hx.scanner.model;

import android.content.Context;

/**
 * ****************************************************
 * author: JamesWong
 * created on: 17/08/22 上午10:00
 * e-mail: PassionateWsj@outlook.com
 * name:
 * desc:
 * ****************************************************
 */

interface StoreSelectorModel {
    void loadData(Context mContext, String keyWord, OnStoreSelectorResultListener listener);
}
