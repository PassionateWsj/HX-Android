package com.intfocus.hx.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.intfocus.hx.util.LoadingUtils;
import com.zbl.lib.baseframe.core.AbstractFragment;
import com.zbl.lib.baseframe.core.Subject;

/**
 * Created by liuruilin on 2017/5/8.
 */

public abstract class BaseModeFragment<Target extends Subject> extends AbstractFragment<Target> {
    public Activity act;
    public Context ctx;

    protected Dialog loadingDialog;

    @Override
    public void onAttach(Context context) {
        ctx = context.getApplicationContext();
        act = (Activity) context;
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public String formatNumber(String number) {
        if (number.contains("")) {
            number = number.replaceAll("0+?$", "");//去掉多余的0
            number = number.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return number;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    protected void showDialog(Context context) {
        loadingDialog = LoadingUtils.createLoadingDialog(context);
        loadingDialog.show();
    }

    protected void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
