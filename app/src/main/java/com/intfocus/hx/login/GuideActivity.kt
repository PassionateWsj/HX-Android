package com.intfocus.hx.login

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.intfocus.hx.R
import com.intfocus.hx.login.adapter.GuidePagerAdapter
import com.intfocus.hx.login.widget.GuideFragment
import kotlinx.android.synthetic.main.activity_guide.*

class GuideActivity : AppCompatActivity() {

    private val permissionsArray = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
    private val CODE_AUTHORITY_REQUEST = 0
    private val totalPageCount = 3


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        // 使背景填满整个屏幕,包括状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }

        setContentView(R.layout.activity_guide)
        val fgmList = (0 until totalPageCount).map { GuideFragment().newInstance(it, totalPageCount) }
        vp_guide_pager.adapter = GuidePagerAdapter(supportFragmentManager, fgmList)
        vp_guide_pager.currentItem = 0
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
                if (grantResults.size > 0) {
                    for (i in permissions.indices) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        } else {
                            flag = true
                        }
                    }
                }

                if (flag) {
                    setAlertDialog(this, "某些权限获取失败，是否到本应用的设置界面设置权限")
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun setAlertDialog(context: Context, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("温馨提示")
                .setMessage(message)
                .setPositiveButton("确认") { dialog, which -> goToAppSetting() }
                .setNegativeButton("取消") { dialog, which ->
                    // 返回DashboardActivity
                }
        builder.show()
    }

    private fun goToAppSetting() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

}
