package com.intfocus.hx.subject.template_v2.entity;

import java.io.Serializable;

/**
 * Created by zbaoliang on 17-5-14.
 */

public class MDRPUnitSingleValue {

    public State state;
    public MainData main_data;
    public MainData sub_data;


    public static class State implements Serializable {
        public int color;
    }

    public static class MainData implements Serializable {
        public String name;
        public String data;
        public String format;
        public String percentage;
    }

}
