package com.intfocus.hx.subject.template_v2.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 仪表盘实体对象
 * Created by zbaoliang on 17-4-28.
 */
public class MererDetalEntity implements Serializable {
    public String name;
    public ArrayList<PageData> data;

    public static class PageData implements Serializable {

        public String parts;
        public String title;
    }
}
