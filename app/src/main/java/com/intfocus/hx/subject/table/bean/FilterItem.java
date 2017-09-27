package com.intfocus.hx.subject.table.bean;

/**
 * Created by CANC on 2017/4/24.
 */

public class FilterItem {
    /**
     * value : 区域A
     * index : 1
     */

    private String value;
    private int index;
    //是否选中，默认选中
    public boolean isSelected = true;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
