package com.intfocus.hx.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intfocus.hx.R;
import com.intfocus.hx.base.BaseActivity;
import com.intfocus.hx.data.response.BaseResult;
import com.intfocus.hx.listen.NoDoubleClickListener;
import com.intfocus.hx.net.ApiException;
import com.intfocus.hx.net.CodeHandledSubscriber;
import com.intfocus.hx.net.RetrofitUtil;
import com.intfocus.hx.util.ActionLogUtil;
import com.intfocus.hx.util.ToastColor;
import com.intfocus.hx.util.ToastUtils;
import com.intfocus.hx.util.URLs;

import org.json.JSONObject;

import static android.app.ProgressDialog.show;

public class ForgetPasswordActivity extends BaseActivity {

    private ImageButton mBackBtn;
    private EditText mEtEmployeeId;
    private TextView mBtnSubmit;
    private EditText mEtEmployeePhoneNum;
    private ProgressDialog mRequestDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_forget_password);
        initView();
        initListener();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mBackBtn = (ImageButton) findViewById(R.id.ibtn_find_pwd_back);
        mEtEmployeeId = (EditText) findViewById(R.id.et_find_pwd_employee_id);
        mEtEmployeePhoneNum = (EditText) findViewById(R.id.et_find_pwd_employee_phone_num);
        mBtnSubmit = (TextView) findViewById(R.id.tv_btn_find_pwd_submit);
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mBtnSubmit.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
                String userNum = mEtEmployeeId.getText().toString();
                String mobile = mEtEmployeePhoneNum.getText().toString();
                if (userNum == null && "".equals(userNum)) {
                    ToastUtils.INSTANCE.show(ForgetPasswordActivity.this, "员工号无效");
                } else if (mobile.length() == 11) {

                    // 发起 post 请求
                    startPost(userNum, mobile);

                    /*
                     * 用户行为记录, 单独异常处理，不可影响用户体验
                     */
                    try {
                        logParams = new JSONObject();
                        logParams.put(URLs.kAction, "重置密码");
                        ActionLogUtil.actionLog(mAppContext, logParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtils.INSTANCE.show(ForgetPasswordActivity.this, "请输入正确的手机号");
                }
            }
        });
    }

    /**
     * 发起 post 请求
     *
     * @param userNum
     * @param mobile
     */
    public void startPost(String userNum, String mobile) {
        mRequestDialog = ProgressDialog.show(this, "稍等", "正在重置密码...");
        RetrofitUtil.getHttpService(getApplicationContext()).resetPwd(userNum, mobile)
                .compose(new RetrofitUtil.CommonOptions<BaseResult>())
                .subscribe(new CodeHandledSubscriber<BaseResult>() {
                    @Override
                    public void onError(ApiException apiException) {
                        mRequestDialog.dismiss();
                        showErrorMsg(apiException.getDisplayMessage());
//                        mBtnSubmit.setClickable(true);
                    }

                    @Override
                    public void onBusinessNext(BaseResult data) {
                        ToastUtils.INSTANCE.show(ForgetPasswordActivity.this, data.getMessage(), ToastColor.SUCCESS);
                    }

                    @Override
                    public void onCompleted() {
                        mRequestDialog.dismiss();
//                        mBtnSubmit.setClickable(true);
                    }
                });

    }

    /**
     * 显示错误信息
     *
     * @param message
     */
    public void showErrorMsg(String message) {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_forget_pwd_notice, null);
        final PopupWindow popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        TextView tv_forget_pwd_notice_content = (TextView) view.findViewById(R.id.tv_forget_pwd_notice_content);
        view.findViewById(R.id.tv_forget_pwd_notice_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        tv_forget_pwd_notice_content.setText(message);

        View parent = LayoutInflater.from(this).inflate(R.layout.activity_new_forget_password, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);

    }
}
