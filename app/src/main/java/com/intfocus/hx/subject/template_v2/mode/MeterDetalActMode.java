package com.intfocus.hx.subject.template_v2.mode;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONReader;
import com.intfocus.hx.subject.template_v2.entity.MererDetalEntity;
import com.intfocus.hx.subject.template_v2.entity.msg.MDetalActRequestResult;
import com.zbl.lib.baseframe.core.AbstractMode;
import com.zbl.lib.baseframe.utils.TimeUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.intfocus.hx.YHApplication.threadPool;


/**
 * 仪表盘-数据处理模块
 * Created by zbaoliang on 17-4-28.
 */
public class MeterDetalActMode extends AbstractMode {

    String TAG = MeterDetalActMode.class.getSimpleName();
    String group_id;
    String report_id;

    Context ctx;

    MererDetalEntity entity;

    public MeterDetalActMode(Context ctx) {
        this.ctx = ctx;
    }

    public void requestData(String group_id, String report_id) {
        this.group_id = group_id;
        this.report_id = report_id;
        requestData();
    }

    @Override
    public void requestData() {
        entity = null;
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
//                try {

                InputStream is = null;
                try {
                    is = ctx.getAssets().open("kpi_detaldata.json");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                InputStreamReader isr = new InputStreamReader(is);
                JSONReader reader = new JSONReader(isr);
                reader.startArray();
                reader.startObject();
//
//                Log.i(TAG, "requestStartTime:" + TimeUtil.getNowTime());
//                String urlString = String.format(K.kReportJsonAPIPath, K.kBaseUrl, group_id, "1", report_id);
//                String assetsPath = FileUtil.sharedPath(ctx);
//                String itemsString;
//                Map<String, String> headers = ApiHelper.checkResponseHeader(urlString, assetsPath);
//                Map<String, String> response = HttpUtil.httpGet(ctx, urlString, headers);
//                Log.i(TAG, "requestEndTime:" + TimeUtil.getNowTime());
//                if (!response.get("code").equals("200") && !response.get("code").equals("304")) {
//                    MDetalActRequestResult result1 = new MDetalActRequestResult(true, 400, null);
//                    EventBus.getDefault().post(result1);
//                    return;
//                }
//                ApiHelper.storeResponseHeader(urlString, assetsPath, response);
//                //请求数据成功
//                itemsString = response.get("body").toString();
//                if (TextUtils.isEmpty(itemsString)) {
//                    itemsString = FileUtil.readFile(assetsPath + K.kTemplateV1);//取数据
//                } else {
//                    try {
//                        FileUtil.writeFile(assetsPath + K.kTemplateV1, itemsString);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (TextUtils.isEmpty(itemsString)) {
//                    MDetalActRequestResult result1 = new MDetalActRequestResult(true, 400, null);
//                    EventBus.getDefault().post(result1);
//                    return;
//                }
//                StringReader stringReader = new StringReader(itemsString);
//                Log.i(TAG, "analysisDataStartTime:" + TimeUtil.getNowTime());
//                JSONReader reader = new JSONReader(stringReader);
//                reader.startArray();
//                reader.startObject();

                entity = new MererDetalEntity();
                entity.data = new ArrayList<>();

                while (reader.hasNext()) {
                    String key = reader.readString();
                    switch (key) {
                        case "name":
                            String name = reader.readObject().toString();
                            entity.name = name;
                            break;

                        case "data":
                            reader.startArray();
                            while (reader.hasNext()) {
                                reader.startObject();
                                MererDetalEntity.PageData data = new MererDetalEntity.PageData();
                                while (reader.hasNext()) {
                                    String dataKey = reader.readString();
                                    switch (dataKey) {
                                        case "parts":
                                            String parts = reader.readObject().toString();
                                            data.parts = parts;
                                            break;

                                        case "title":
                                            String title = reader.readObject().toString();
                                            data.title = title;
                                            break;
                                    }
                                }
                                reader.endObject();
                                entity.data.add(data);
                            }
                            reader.endArray();
                            break;
                    }
                }
                reader.endObject();
                reader.endArray();
                EventBus.getDefault().post(new MDetalActRequestResult(true, 200, entity));
                Log.i(TAG, "analysisDataEndTime:" + TimeUtil.getNowTime());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    MDetalActRequestResult result1 = new MDetalActRequestResult(true, 400, null);
//                    EventBus.getDefault().post(result1);
//                }
            }
        });
    }

    /**
     * 解析数据
     * @param result
     *//*
    private void analysisData(String result) {
        ArrayList<MererDetalEntity> datas = (ArrayList<MererDetalEntity>) JSON.parseArray(result, MererDetalEntity.class);
        EventBus.getDefault().post(new MDetalActRequestResult(true, 200, datas));
    }*/
}
