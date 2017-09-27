package com.intfocus.hx.subject.template_v2.entity.msg;

import com.intfocus.hx.subject.template_v2.entity.MererEntity;

import java.util.ArrayList;

/**
 * 仪表数据请求结果
 * Created by zbaoliang on 17-4-28.
 */
public class MeterRequestResult {
    public boolean isSuccress ;
    public int stateCode ;
    public ArrayList<MererEntity> topDatas;
    public ArrayList<MererEntity> bodyDatas;

    public MeterRequestResult(boolean isSuccress, int stateCode) {
        this.isSuccress = isSuccress;
        this.stateCode = stateCode;
    }

    public boolean isSuccress() {
        return isSuccress;
    }

    public void setSuccress(boolean succress) {
        isSuccress = succress;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public ArrayList<MererEntity> getTopDatas() {
        return topDatas;
    }

    public void setTopDatas(ArrayList<MererEntity> topDatas) {
        this.topDatas = topDatas;
    }

    public ArrayList<MererEntity> getBodyDatas() {
        return bodyDatas;
    }

    public void setBodyDatas(ArrayList<MererEntity> bodyDatas) {
        this.bodyDatas = bodyDatas;
    }
}
