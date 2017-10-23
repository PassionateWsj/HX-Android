package com.intfocus.hx.login

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.intfocus.hx.R
import com.intfocus.hx.base.BaseActivity.kVersionCode
import com.intfocus.hx.dashboard.DashboardActivity
import com.intfocus.hx.data.response.BaseResult
import com.intfocus.hx.data.response.login.RegisterResult
import com.intfocus.hx.listen.NoDoubleClickListener
import com.intfocus.hx.login.bean.Device
import com.intfocus.hx.login.bean.DeviceRequest
import com.intfocus.hx.login.bean.NewUser
import com.intfocus.hx.net.ApiException
import com.intfocus.hx.net.CodeHandledSubscriber
import com.intfocus.hx.net.RetrofitUtil
import com.intfocus.hx.util.*
import com.intfocus.hx.util.K.kCurrentUIVersion
import com.intfocus.hx.util.K.kUserId
import com.intfocus.hx.util.K.kUserName
import com.intfocus.hx.util.URLs.kGroupId
import com.intfocus.hx.util.URLs.kRoleId
import com.intfocus.hx.util.URLs.kUserNum
import com.pgyersdk.update.PgyUpdateManager
import com.pgyersdk.update.UpdateManagerListener
import org.OpenUDID.OpenUDID_manager
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class LoginActivity : FragmentActivity() {
    var kFromActivity = "from_activity"         // APP 启动标识
    var kSuccess = "success"                    // 用户登录验证结果
    private var usernameEditText: EditText? = null
    private var passwordEditText: EditText? = null
    private var userNum: String? = null
    private var userPass: String? = null
    private var mLinearUsernameBelowLine: View? = null
    private var mLinearPasswordBelowLine: View? = null
    private var mLlEtUsernameClear: LinearLayout? = null
    private var mLlEtPasswordClear: LinearLayout? = null
    private var mBtnLogin: Button? = null
    private var mDeviceRequest: DeviceRequest? = null
    private var mUserSP: SharedPreferences? = null
    private var mUserSPEdit: SharedPreferences.Editor? = null
    private var mPushSP: SharedPreferences? = null
    private var mProgressDialog: ProgressDialog? = null
    private var logParams = JSONObject()
    private var ctx: Context? = null
    private var assetsPath: String? = null
    private var sharedPath: String? = null

    private val permissionsArray = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
    private val CODE_AUTHORITY_REQUEST = 0

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mUserSP = getSharedPreferences("UserBean", Context.MODE_PRIVATE)
        mPushSP = getSharedPreferences("PushMessage", Context.MODE_PRIVATE)
        mUserSPEdit = mUserSP!!.edit()

        ctx = this
        ApiHelper.getAMapLocation(this)
        assetsPath = FileUtil.dirPath(ctx, K.kHTMLDirName)
        sharedPath = FileUtil.sharedPath(ctx)

        setContentView(R.layout.activity_login_new)
//        checkPgyerVersionUpgrade(this@LoginActivity, true)

        usernameEditText = findViewById(R.id.etUsername) as EditText
        passwordEditText = findViewById(R.id.etPassword) as EditText
        mLinearUsernameBelowLine = findViewById(R.id.linearUsernameBelowLine)
        mLinearPasswordBelowLine = findViewById(R.id.linearPasswordBelowLine)
        mLlEtUsernameClear = findViewById(R.id.ll_etUsername_clear) as LinearLayout
        mLlEtPasswordClear = findViewById(R.id.ll_etPassword_clear) as LinearLayout
        mBtnLogin = findViewById(R.id.btn_login) as Button

        // 初始化监听
        initListener()

        getAuthority()


        /*
         * 显示记住用户名称
         */
        usernameEditText!!.setText(mUserSP!!.getString("user_num_login", ""))
    }

    /**
     * 初始化监听器
     */
    private fun initListener() {
        // 忘记密码监听
        findViewById(R.id.forgetPasswordTv).setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }

        // 注册监听
        findViewById(R.id.applyRegistTv).setOnClickListener {
            RetrofitUtil.getHttpService(ctx).getRegister("prompt-info-when-register")
                    .compose(RetrofitUtil.CommonOptions())
                    .subscribe(object : CodeHandledSubscriber<RegisterResult>() {
                        override fun onError(apiException: ApiException) {
                            ToastUtils.show(this@LoginActivity, apiException.displayMessage)
                        }

                        override fun onBusinessNext(data: RegisterResult) {
                            ToastUtils.show(this@LoginActivity, data.data!!)
                        }

                        override fun onCompleted() {}
                    })
        }

        // 用户名输入框 焦点监听 隐藏/显示 清空按钮
        usernameEditText!!.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            changeEditTextFocusUnderLineColor(hasFocus, mLinearUsernameBelowLine)
            if (usernameEditText!!.text.length > 0 && hasFocus) {
                mLlEtUsernameClear!!.visibility = View.VISIBLE
            } else {
                mLlEtUsernameClear!!.visibility = View.GONE
            }
        }

        // 用户名输入框 文本变化监听
        // 处理 显示/隐藏 清空按钮事件
        usernameEditText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (s.toString().length > 0) {
                    mLlEtUsernameClear!!.visibility = View.VISIBLE
                } else {
                    mLlEtUsernameClear!!.visibility = View.GONE
                }

            }
        })

        // 清空用户名 按钮 监听
        mLlEtUsernameClear!!.setOnClickListener { usernameEditText!!.setText("") }

        // 密码输入框 焦点监听 隐藏/显示 清空按钮
        passwordEditText!!.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            changeEditTextFocusUnderLineColor(hasFocus, mLinearPasswordBelowLine)
            if (passwordEditText!!.text.length > 0 && hasFocus) {
                mLlEtPasswordClear!!.visibility = View.VISIBLE
            } else {
                mLlEtPasswordClear!!.visibility = View.GONE
            }
        }

        // 密码输入框 文本变化监听
        // 处理 显示/隐藏 清空按钮事件
        passwordEditText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (s.toString().length > 0) {
                    mLlEtPasswordClear!!.visibility = View.VISIBLE
                } else {
                    mLlEtPasswordClear!!.visibility = View.GONE
                }

            }
        })

        // 密码输入框 回车 监听
        passwordEditText!!.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                //                    actionSubmit(v);
                hideKeyboard()
            }
            false
        }

        // 清空密码 按钮 监听
        mLlEtPasswordClear!!.setOnClickListener { passwordEditText!!.setText("") }

        // 背景布局 触摸 监听
        findViewById(R.id.login_layout).setOnTouchListener { v, event ->
            hideKeyboard()
            false
        }

        mBtnLogin!!.setOnClickListener(object : NoDoubleClickListener() {
            override fun onNoDoubleClick(v: View) {
                actionSubmit(v)
            }
        })

    }

    /**
     * 改变 EditText 正在编辑/不在编辑 下划线颜色
     *
     * @param hasFocus
     * @param underLineView
     */
    private fun changeEditTextFocusUnderLineColor(hasFocus: Boolean, underLineView: View?) {
        if (hasFocus) {
            underLineView!!.setBackgroundColor(resources.getColor(R.color.co1_syr))
        } else {
            underLineView!!.setBackgroundColor(resources.getColor(R.color.co9_syr))
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        PgyUpdateManager.unregister()
        super.onDestroy()
    }

    override fun onBackPressed() {
        finish()
        System.exit(0)
    }

    /*
     * 登录按钮点击事件
     */
    fun actionSubmit(v: View) {
        try {
            userNum = usernameEditText!!.text.toString()
            userPass = passwordEditText!!.text.toString()

            mUserSPEdit!!.putString("user_num_login", userNum).commit()

            if (userNum!!.isEmpty() || userPass!!.isEmpty()) {
                ToastUtils.show(this@LoginActivity, "请输入用户名与密码")
                return
            }

            hideKeyboard()
            mProgressDialog = ProgressDialog.show(this@LoginActivity, "稍等", "验证用户信息...")

            val packageInfo = packageManager.getPackageInfo(packageName, 0)

            // 上传设备信息
            uploadDeviceInformation(packageInfo)

            mUserSPEdit!!.putString(K.kAppVersion, String.format("a%s", packageInfo.versionName)).commit()
            mUserSPEdit!!.putString("os_version", "android" + Build.VERSION.RELEASE).commit()
            mUserSPEdit!!.putString("device_info", android.os.Build.MODEL).commit()

            // 登录验证
            RetrofitUtil.getHttpService(ctx).userLogin(userNum, URLs.MD5(userPass), mUserSP!!.getString("location", "0,0"))
                    .compose(RetrofitUtil.CommonOptions())
                    .subscribe(object : CodeHandledSubscriber<NewUser>() {

                        override fun onCompleted() {

                        }

                        /**
                         * 登录请求失败
                         * @param apiException
                         */
                        override fun onError(apiException: ApiException) {
                            mProgressDialog!!.dismiss()
                            try {
                                logParams = JSONObject()
                                logParams.put(URLs.kAction, "unlogin")
                                logParams.put(URLs.kUserName, userNum + "|;|" + userPass)
                                logParams.put(URLs.kObjTitle, apiException.displayMessage)
                                ActionLogUtil.actionLoginLog(ctx, logParams)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                            ToastUtils.show(this@LoginActivity, apiException.displayMessage)
                        }

                        /**
                         * 登录成功
                         * @param data 返回的数据
                         */
                        override fun onBusinessNext(data: NewUser) {
                            mUserSPEdit!!.putString("password", URLs.MD5(userPass)).commit()
                            upLoadDevice() //上传设备信息

                            mUserSPEdit!!.putBoolean(URLs.kIsLogin, true).commit()

                            mUserSPEdit!!.putString(kUserName, data.data!!.user_name).commit()
                            mUserSPEdit!!.putString(kGroupId, data.data!!.group_id).commit()
                            mUserSPEdit!!.putString(kRoleId, data.data!!.role_id).commit()
                            mUserSPEdit!!.putString(kUserId, data.data!!.user_id).commit()
                            mUserSPEdit!!.putString(URLs.kRoleName, data.data!!.role_name).commit()
                            mUserSPEdit!!.putString(URLs.kGroupName, data.data!!.group_name).commit()
                            mUserSPEdit!!.putString(kUserNum, data.data!!.user_num).commit()
                            mUserSPEdit!!.putString(kCurrentUIVersion, "v2").commit()

                            // 判断是否包含推送信息，如果包含 登录成功直接跳转推送信息指定页面
                            if (intent.hasExtra("msgData")) {
                                val msgData = intent.getBundleExtra("msgData")
                                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                intent.putExtra("msgData", msgData)
                                this@LoginActivity.startActivity(intent)
                            } else {
                                // 检测用户空间，版本是否升级版本是否升级
                                FileUtil.checkVersionUpgrade(ctx, assetsPath, sharedPath)

                                // 跳转至主界面
                                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                this@LoginActivity.startActivity(intent)
                            }

                            /*
                            * 用户行为记录, 单独异常处理，不可影响用户体验
                            */
                            try {
                                logParams = JSONObject()
                                logParams.put("action", "登录")
                                ActionLogUtil.actionLog(ctx, logParams)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                            mProgressDialog!!.dismiss()
                            finish()
                        }
                    })
        } catch (e: Exception) {
            e.printStackTrace()
            mProgressDialog!!.dismiss()
            ToastUtils.show(this, e.localizedMessage)
        }

    }

    private fun uploadDeviceInformation(packageInfo: PackageInfo) {
        mDeviceRequest = DeviceRequest()
        mDeviceRequest!!.user_num = userNum
        val deviceBean = DeviceRequest.DeviceBean()
        deviceBean.uuid = OpenUDID_manager.getOpenUDID()
        deviceBean.os = Build.MODEL
        deviceBean.name = Build.MODEL
        deviceBean.os_version = Build.VERSION.RELEASE
        deviceBean.platform = "android"
        mDeviceRequest!!.device = deviceBean
        mDeviceRequest!!.app_version = packageInfo.versionName
        mDeviceRequest!!.browser = WebView(this).settings.userAgentString
    }

    /**
     * 上传设备信息
     */
    private fun upLoadDevice() {
        RetrofitUtil.getHttpService(ctx).deviceUpLoad(mDeviceRequest)
                .compose(RetrofitUtil.CommonOptions())
                .subscribe(object : CodeHandledSubscriber<Device>() {
                    override fun onError(apiException: ApiException) {
                        ToastUtils.show(this@LoginActivity, apiException.displayMessage)
                    }

                    /**
                     * 上传设备信息成功
                     * @param data 返回的数据
                     */
                    override fun onBusinessNext(data: Device) {
                        mUserSPEdit!!.putString("device_uuid", data.mResult!!.device_uuid).commit()
                        mUserSPEdit!!.putBoolean("device_state", data.mResult!!.device_state).commit()
                        mUserSPEdit!!.putString("user_device_id", data.mResult!!.user_device_id.toString()).commit()

                        RetrofitUtil.getHttpService(ctx).putPushToken(data.mResult!!.device_uuid, mPushSP!!.getString("push_token", ""))
                                .compose(RetrofitUtil.CommonOptions())
                                .subscribe(object : CodeHandledSubscriber<BaseResult>() {
                                    override fun onError(apiException: ApiException) {

                                    }

                                    override fun onBusinessNext(data: BaseResult) {}

                                    override fun onCompleted() {

                                    }
                                })
                    }

                    override fun onCompleted() {}
                })

    }

    /**
     * 隐藏软件盘
     */
    fun hideKeyboard() {
        val imm = getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }

    /*
     * 托管在蒲公英平台，对比版本号检测是否版本更新
     * 对比 build 值，只准正向安装提示
     * 奇数: 测试版本，仅提示
     * 偶数: 正式版本，点击安装更新
     */
    fun checkPgyerVersionUpgrade(activity: Activity, isShowToast: Boolean) {
        PgyUpdateManager.register(activity, "com.intfocus.shengyiplus.fileprovider", object : UpdateManagerListener() {
            override fun onUpdateAvailable(result: String?) {
                try {
                    val appBean = UpdateManagerListener.getAppBeanFromString(result)

                    if (result == null || result.isEmpty()) {
                        return
                    }

                    val packageInfo = packageManager.getPackageInfo(packageName, 0)
                    val currentVersionCode = packageInfo.versionCode
                    val response = JSONObject(result)
                    val message = response.getString("message")

                    val responseVersionJSON = response.getJSONObject(URLs.kData)
                    val newVersionCode = responseVersionJSON.getInt(kVersionCode)

                    val newVersionName = responseVersionJSON.getString("versionName")

                    if (currentVersionCode >= newVersionCode) {
                        return
                    }

                    val pgyerVersionPath = String.format("%s/%s", FileUtil.basePath(applicationContext), K.kPgyerVersionConfigFileName)
                    FileUtil.writeFile(pgyerVersionPath, result)

                    if (newVersionCode % 2 == 1) {
                        if (isShowToast) {
                            ToastUtils.show(this@LoginActivity, String.format("有发布测试版本%s(%s)", newVersionName, newVersionCode), ToastColor.SUCCESS)
                        }

                        return
                    } else if (HttpUtil.isWifi(activity) && newVersionCode % 10 == 8) {
                        UpdateManagerListener.startDownloadTask(activity, appBean.downloadURL)
                        return
                    }
                    AlertDialog.Builder(activity)
                            .setTitle("版本更新")
                            .setMessage(if (message.isEmpty()) "无升级简介" else message)
                            .setPositiveButton(
                                    "确定"
                            ) { dialog, which -> UpdateManagerListener.startDownloadTask(activity, appBean.downloadURL) }
                            .setNegativeButton("下一次"
                            ) { dialog, which -> dialog.dismiss() }
                            .setCancelable(false)
                            .show()

                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onNoUpdateAvailable() {}
        })
    }

    /*
     * 获取权限 : 文件读写 (WRITE_EXTERNAL_STORAGE),读取设备信息 (READ_PHONE_STATE)
     */
    private fun getAuthority() {
        val permissionsList = permissionsArray.filter { ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED }
        if (!permissionsList.isEmpty() && permissionsList != null) {
            ActivityCompat.requestPermissions(this, permissionsList.toTypedArray(), CODE_AUTHORITY_REQUEST)
        }
    }

    /*
     * 权限获取反馈
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {

            CODE_AUTHORITY_REQUEST -> {
                var flag = false
                if (grantResults.isNotEmpty()) {
                    for (i in permissions.indices) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        } else {
                            flag = true
                        }
                    }
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}
