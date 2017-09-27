package com.intfocus.hx.subject.template_v2.mode;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONReader;
import com.intfocus.hx.subject.template_v2.entity.MDetalUnitEntity;
import com.intfocus.hx.subject.template_v2.entity.MererDetalEntity;
import com.intfocus.hx.subject.template_v2.entity.msg.MDetalRootPageRequestResult;
import com.zbl.lib.baseframe.core.AbstractMode;
import com.zbl.lib.baseframe.utils.TimeUtil;

import java.io.StringReader;
import java.util.ArrayList;

import static com.intfocus.hx.YHApplication.threadPool;

/**
 * 仪表盘-数据处理模块
 * Created by zbaoliang on 17-4-28.
 */
public class ModularTwo_UnitTablesParentMode extends AbstractMode {

    String TAG = ModularTwo_UnitTablesParentMode.class.getSimpleName();

    Context ctx;

    MererDetalEntity entity;

    public ModularTwo_UnitTablesParentMode(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void requestData() {
    }

    public ArrayList<MDetalUnitEntity> datas;

    /**
     * 解析数据
     *
     * @param result
     */
    public void analysisData(final String result) {
        Log.i(TAG, "StartAnalysisTime:" + TimeUtil.getNowTime());
        datas = new ArrayList<>();
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    StringReader isr = new StringReader(result);
                    JSONReader reader = new JSONReader(isr);
                    reader.startArray();
                    while (reader.hasNext()) {
                        MDetalUnitEntity entity = new MDetalUnitEntity();
                        reader.startObject();
                        while (reader.hasNext()) {
                            String key = reader.readString();
                            switch (key) {
                                case "table":
                                    entity.config = reader.readObject().toString();
                                    break;

                                case "title":
                                    entity.type = reader.readObject().toString();
                                    break;
                            }
                        }
                        datas.add(entity);
                        reader.endObject();
                    }
                    reader.endArray();
                    MDetalRootPageRequestResult RequestResult = new MDetalRootPageRequestResult(true, 200, datas);
                    dataCallback(RequestResult, "onMessageEvent");
                    Log.i(TAG, "EndAnalysisTime:" + TimeUtil.getNowTime());
                } catch (Exception e) {
                    dataCallback(new MDetalRootPageRequestResult(true, 400, null), "onMessageEvent");
                    e.printStackTrace();
                }
            }
        });
    }
}
