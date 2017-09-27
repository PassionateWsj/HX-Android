package com.intfocus.hx.subject.template_v2.entity.msg;


import com.intfocus.hx.subject.template_v2.entity.MDetalUnitEntity;

import java.util.ArrayList;

/**
 * 仪表数据详情页面请求结果
 * Created by zbaoliang on 17-4-28.
 */
public class MDetalRootPageRequestResult {
    public boolean isSuccress;
    public int stateCode;
    public ArrayList<MDetalUnitEntity> datas;

    public MDetalRootPageRequestResult(boolean isSuccress, int stateCode, ArrayList<MDetalUnitEntity> datas) {
        this.isSuccress = isSuccress;
        this.stateCode = stateCode;
        this.datas = datas;
    }
}
