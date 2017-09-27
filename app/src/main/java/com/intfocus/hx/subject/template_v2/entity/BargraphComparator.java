package com.intfocus.hx.subject.template_v2.entity;

/**
 * 条状图-重构对象-以便排序用
 * Created by zbaoliang on 17-5-16.
 */
public class BargraphComparator {
    public String name;
    public float data;
    public int color;

    public BargraphComparator(String name, float data, int color) {
        this.name = name;
        this.data = data;
        this.color = color;
    }
}
