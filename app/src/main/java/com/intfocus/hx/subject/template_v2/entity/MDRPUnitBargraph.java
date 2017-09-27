package com.intfocus.hx.subject.template_v2.entity;

import java.util.ArrayList;

/**
 * 条状图数据实体对象
 * Created by zbaoliang on 17-5-15.
 */
public class MDRPUnitBargraph {
    public Series series;
    public XAxis xAxis;

    public static class Series {
        public ArrayList<Data> data;

        public String name;
        public String percentage;


        public static class Data{
            public float value;
            public int color;
        }
    }


    public static class XAxis {
        public String[] data;

        public String name;
    }
}
