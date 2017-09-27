package com.intfocus.hx.subject.template_v2.mode;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.intfocus.hx.subject.template_v2.entity.ModularTwo_UnitTableEntity;
import com.intfocus.hx.subject.template_v2.entity.msg.MDetalRootPageRequestResult;
import com.zbl.lib.baseframe.core.AbstractMode;
import com.zbl.lib.baseframe.utils.TimeUtil;

import static com.intfocus.hx.YHApplication.threadPool;

/**
 * 仪表盘-数据处理模块
 * Created by zbaoliang on 17-4-28.
 */
public class ModularTwo_UnitTableContMode extends AbstractMode {

    String TAG = ModularTwo_UnitTableContMode.class.getSimpleName();

    Context ctx;

    public ModularTwo_UnitTableContMode(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void requestData() {
    }

    /**
     * 解析数据
     *
     * @param result
     */
    public void analysisData(final String result) {
        Log.i(TAG, "StartAnalysisTime:" + TimeUtil.getNowTime());
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ModularTwo_UnitTableEntity entity = JSON.parseObject(result, ModularTwo_UnitTableEntity.class);
                    dataCallback(entity, "onMessageEvent");
                    Log.i(TAG, "EndAnalysisTime:" + TimeUtil.getNowTime());
                } catch (Exception e) {
                    dataCallback(new MDetalRootPageRequestResult(true, 400, null), "onMessageEvent");
                    e.printStackTrace();
                }
            }
        });
    }
}
