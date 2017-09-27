package com.intfocus.hx.scanner

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.CheckBox
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.intfocus.hx.R
import com.intfocus.hx.subject.WebApplicationActivity
import com.intfocus.hx.util.*
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.filter.Filter
import kotlinx.android.synthetic.main.activity_bar_code_scanner_v2.*
import kotlinx.android.synthetic.main.popup_input_barcode.view.*


class BarCodeScannerActivity : AppCompatActivity(), QRCodeView.Delegate, View.OnClickListener {
    companion object {
        val TAG = "hjjzz"
        val REQUEST_CODE_CHOOSE = 1
    }

    private var isLightOn = false
    private var isStartActivity = false
    lateinit var mUserSP: SharedPreferences

    private var view: View? = null
    private var popupWindow: PopupWindow? = null
    private var tvInputBarcodeLight: TextView? = null
    private var cbInputBarcodeLight: CheckBox? = null
    private var tvBarcodeLight: TextView? = null
    private var cbBarcodeLight: CheckBox? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_code_scanner_v2)
        mUserSP = getSharedPreferences("UserBean", Context.MODE_PRIVATE)
        initView()
        initListener()

        zbarview_barcode_scanner.setDelegate(this)
        zbarview_barcode_scanner.startSpot()
    }

    override fun onStart() {
        super.onStart()
        zbarview_barcode_scanner.startCamera()
    }

    override fun onRestart() {
        super.onRestart()
        if (popupWindow!!.isShowing) {
            checkLightStatus(isLightOn, tvInputBarcodeLight!!, cbInputBarcodeLight!!)
        } else {
            checkLightStatus(isLightOn, tvBarcodeLight!!, cbBarcodeLight!!)
            zbarview_barcode_scanner.showScanRect()
            zbarview_barcode_scanner.startSpot()
        }
    }

    override fun onStop() {
        super.onStop()
        isLightOn = false
        zbarview_barcode_scanner.closeFlashlight()
        zbarview_barcode_scanner.stopCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        zbarview_barcode_scanner.onDestroy()
    }

    /**
     * 初始化视图
     */
    private fun initView() {
        tvBarcodeLight = findViewById(R.id.tv_barcode_light) as TextView
        cbBarcodeLight = findViewById(R.id.cb_barcode_light) as CheckBox

        view = LayoutInflater.from(this).inflate(R.layout.popup_input_barcode, null)
        popupWindow = PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true)
        popupWindow!!.setBackgroundDrawable(BitmapDrawable())
        val mTypeFace = Typeface.createFromAsset(this.assets, "ALTGOT2N.TTF")
        view!!.et_input_barcode.typeface = mTypeFace
        tvInputBarcodeLight = view!!.findViewById(R.id.tv_input_barcode_light) as TextView
        cbInputBarcodeLight = view!!.findViewById(R.id.cb_input_barcode_light) as CheckBox
    }

    /**
     * 初始化监听器
     */
    private fun initListener() {
        //手电筒开关
        ll_btn_light_switch.setOnClickListener {
            isLightOn = !isLightOn
            checkLightStatus(isLightOn, tvBarcodeLight!!, cbBarcodeLight!!)
            if (isLightOn) {
                zbarview_barcode_scanner.openFlashlight()
            } else {
                zbarview_barcode_scanner.closeFlashlight()
            }
        }
        //手动输入条码点击样式
        ll_btn_input_bar_code.setOnTouchListener { _, event ->
            when {
                event.action == MotionEvent.ACTION_DOWN -> {
                    tv_input_bar_code.background = resources.getDrawable(R.drawable.btn_manual1)
                    tv_barcode_input.setTextColor(resources.getColor(R.color.co7_syr))
                }
                event.action == MotionEvent.ACTION_UP -> {
                    tv_input_bar_code.background = resources.getDrawable(R.drawable.btn_manual2)
                    tv_barcode_input.setTextColor(resources.getColor(R.color.co10_syr))
                }
            }
            false
        }
        //手动输入条码弹出popupwindow
        ll_btn_input_bar_code.setOnClickListener {
            zbarview_barcode_scanner.stopSpot()
            popupWindow!!.showAsDropDown(barcode_top_reference, 0, 0)
            checkLightStatus(isLightOn, tvInputBarcodeLight!!, cbInputBarcodeLight!!)
        }
        iv_barcode_back.setOnClickListener {
            finish()
        }
        //popupWindows中打开手电筒点击事件
        view!!.ll_btn_input_barcode_light_switch.setOnClickListener {
            isLightOn = !isLightOn
            checkLightStatus(isLightOn, tvInputBarcodeLight!!, cbInputBarcodeLight!!)
            if (isLightOn) {
                zbarview_barcode_scanner.openFlashlight()
            } else {
                zbarview_barcode_scanner.closeFlashlight()
            }
        }

        view!!.btn_input_barcode_confirm.setOnClickListener {
            var trim = view!!.et_input_barcode.text.toString()
            if (trim == "") {
                ToastUtils.show(this, "请输入条码")
                return@setOnClickListener
            }

            isStartActivity = true
            var link = "http://sm.andoner.cn/static/app/quan.html?" + "uid=" + mUserSP.getString("user_num", "") + "&" + "code=" + trim
            val intent = Intent(this, WebApplicationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.putExtra(URLs.kBannerName, "核销奖券")
            intent.putExtra(URLs.kLink, link)
            startActivity(intent)
            finish()
        }

        view!!.iv_input_barcode_back.setOnClickListener {
            checkLightStatus(isLightOn, tvBarcodeLight!!, cbBarcodeLight!!)
            popupWindow!!.dismiss()
            isStartActivity = false
        }
        popupWindow!!.setOnDismissListener {
            if (isStartActivity) {
                finish()
            } else {
                checkLightStatus(isLightOn, tvBarcodeLight!!, cbBarcodeLight!!)
                zbarview_barcode_scanner.showScanRect()
                zbarview_barcode_scanner.startSpot()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_barcode_gallery -> {
                Matisse.from(this)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .maxSelectable(1)
                        .addFilter(GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f)
                        .imageEngine(GlideEngine())
                        .forResult(BarCodeScannerActivity.REQUEST_CODE_CHOOSE)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onScanQRCodeOpenCameraError() {
        ToastUtils.show(this@BarCodeScannerActivity, "扫描失败，请重新扫描")
        zbarview_barcode_scanner.postDelayed({ zbarview_barcode_scanner.startSpot() }, 2000)
    }

    override fun onScanQRCodeSuccess(result: String?) {
        Log.i(TAG, "result:" + result)
//        Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
        var toIntOrNull = result!!.toLongOrNull()
        if (toIntOrNull == null) {
            ToastUtils.show(this@BarCodeScannerActivity, "暂时只支持条形码")
            zbarview_barcode_scanner.postDelayed({ zbarview_barcode_scanner.startSpot() }, 2000)
            return
        }

        var link = "http://sm.andoner.cn/static/app/quan.html?" + "uid=" + mUserSP.getString("user_num", "") + "&" + "code=" + result
        val intent = Intent(this, WebApplicationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.putExtra(URLs.kBannerName, "核销奖券")
        intent.putExtra(URLs.kLink, link)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        zbarview_barcode_scanner.showScanRect()
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            var mProgressDialog = ProgressDialog.show(this, "稍等", "正在扫描")
            zbarview_barcode_scanner.stopSpot()
//            tv_barcode_scanning.visibility = View.VISIBLE
            val picturePath = ImageUtil.handleImageOnKitKat(Matisse.obtainResult(data)[0], this)
            LogUtil.d(TAG, "picturePath:::" + picturePath)
            object : AsyncTask<Void, Void, String>() {
                override fun doInBackground(vararg params: Void): String {
                    return QRCodeDecoder.syncDecodeQRCode(picturePath)
                }

                override fun onPostExecute(result: String) {
                    mProgressDialog.dismiss()
                    if (TextUtils.isEmpty(result) || "".equals(result)) {
                        ToastUtils.show(this@BarCodeScannerActivity, "未发现条形码")
//                        tv_barcode_scanning.visibility = View.GONE
                        zbarview_barcode_scanner.postDelayed({ zbarview_barcode_scanner.startSpot() }, 2000)
                    } else {
                        onScanQRCodeSuccess(result)
                    }
                }
            }.execute()
        }
    }

    /**
     * 改变手电筒按钮 开/关 样式
     */
    private fun checkLightStatus(status: Boolean, tv: TextView, checkBox: CheckBox) {
        if (status) {
            tv.setTextColor(resources.getColor(R.color.co7_syr))
            tv.text = "关闭手电筒"
        } else {
            tv.setTextColor(resources.getColor(R.color.co10_syr))
            tv.text = "打开手电筒"
        }
        checkBox.isChecked = status
    }
}

