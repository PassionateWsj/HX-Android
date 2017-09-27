package com.intfocus.hx.subject.template_v2.entity;

/**
 * Created by liuruilin on 2017/8/8.
 */

public class DataHolder {
    private String data;
    public String getData() {return data;}
    public void setData(String data) {this.data = data;}
    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}
}
