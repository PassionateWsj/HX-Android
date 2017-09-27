package com.intfocus.hx.subject.template_v2.entity.msg;

import com.intfocus.hx.subject.template_v2.entity.MererDetalEntity;

/**
 * 仪表数据详情页面请求结果
 * Created by zbaoliang on 17-4-28.
 */
public class MDetalActRequestResult {
    public boolean isSuccress ;
    public int stateCode ;
    public MererDetalEntity datas;

    public MDetalActRequestResult(boolean isSuccress, int stateCode, MererDetalEntity datas) {
        this.isSuccress = isSuccress;
        this.stateCode = stateCode;
        this.datas = datas;
    }
}
