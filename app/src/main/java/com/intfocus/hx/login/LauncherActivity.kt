package com.intfocus.hx.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
import com.intfocus.hx.R
import com.intfocus.hx.screen_lock.ConfirmPassCodeActivity
import com.intfocus.hx.util.AssetsUpDateUtil
import com.intfocus.hx.util.HttpUtil
import com.intfocus.hx.util.LogUtil
import com.intfocus.hx.util.OnCheckAssetsUpdateResultListener
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*


class LauncherActivity : Activity(), Animation.AnimationListener {

    val ctx = this
    /**
     * 最短点击间隔时长 ms
     */
    private val MIN_CLICK_DELAY_TIME = 2000
    /**
     * 上一次点击的时间
     */
    private var lastClickTime: Long = 0
    private lateinit var toast: Toast
    private lateinit var mSettingSP: SharedPreferences
    private lateinit var mUserSP: SharedPreferences
    private lateinit var mAssetsSP: SharedPreferences
    private lateinit var mAssetsSPEdit: SharedPreferences.Editor
    private lateinit var packageInfo: PackageInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash)

        initData()
        initAnim()
    }

    private fun initData() {
        mSettingSP = getSharedPreferences("SettingPreference", Context.MODE_PRIVATE)
        mUserSP = getSharedPreferences("UserBean", Context.MODE_PRIVATE)
        mAssetsSP = getSharedPreferences("AssetsMD5", Context.MODE_PRIVATE)
        try {
            packageInfo = packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        mAssetsSPEdit = mAssetsSP.edit()

        toast = Toast.makeText(ctx, "再按一次退出生意人", Toast.LENGTH_SHORT)
    }

    private fun initAnim() {
        val animation = AlphaAnimation(1f, 1.0f)
        animation.duration = 1500
        animation.setAnimationListener(this)
        logo.startAnimation(animation)
    }

    override fun onAnimationRepeat(p0: Animation?) {
    }

    override fun onAnimationEnd(p0: Animation?) {
//        number_progress_bar_splash.visibility = View.VISIBLE
//        tv_splash_status.visibility = View.VISIBLE
//        firstUnZipAssets()
        intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_SINGLE_TOP)
        this.startActivity(intent)

        finish()
    }

    override fun onAnimationStart(p0: Animation?) {
        tv_splash_status.text = "正在下载报表样式文件.."
    }

    private fun enter() {
        val packageInfo = this.packageManager.getPackageInfo(this.packageName, 0)
        when {
            mSettingSP.getBoolean("ScreenLock", false) -> {
                intent = Intent(this, ConfirmPassCodeActivity::class.java)
                intent.putExtra("is_from_login", true)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_SINGLE_TOP)
                this.startActivity(intent)

                finish()
            }
            mSettingSP.getInt("Version", 0) != packageInfo.versionCode -> {
                intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_SINGLE_TOP)
                this.startActivity(intent)

                finish()
            }
            else -> {
                intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_SINGLE_TOP)
                this.startActivity(intent)

                finish()
            }
        }
    }

    private fun firstUnZipAssets() {
        if (!HttpUtil.isConnected(ctx)) {
            tv_splash_status.text = "请检查网络"
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    ctx.finish()
                }
            }, 2000)
            return
        }
        if (mSettingSP.getInt("Version", 0) == 0) {

            AssetsUpDateUtil.checkFirstSetup(ctx, object : OnCheckAssetsUpdateResultListener {
                override fun onResultSuccess() {
                    checkAssets()
                }

                override fun onFailure() {
                }
            })
        } else {
            checkAssets()
        }
    }

    private fun checkAssets() {
        LogUtil.d("hjjzz", "MainThread:::" + Thread.currentThread().name)
        AssetsUpDateUtil.checkAssetsUpdate(ctx, number_progress_bar_splash, object : OnCheckAssetsUpdateResultListener {
            override fun onResultSuccess() {
                LogUtil.d("hjjzz", "onResultSuccess:::" + Thread.currentThread().name)
                tv_splash_status.text = "已是最新资源"
                enter()
            }

            override fun onFailure() {
                tv_splash_status.text = "更新失败"
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        ctx.finish()
                    }
                }, 2000)
            }
        })
    }

    override fun onBackPressed() {
        val currentTime = Calendar.getInstance().timeInMillis
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime
            toast.show()
            return
        }
        toast.cancel()
        finish()

    }

    override fun onDestroy() {
        AssetsUpDateUtil.unSubscribe()
        super.onDestroy()
    }
}
