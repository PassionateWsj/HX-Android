package com.intfocus.hx.subject.template_v2.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 仪表盘实体对象
 * Created by zbaoliang on 17-4-28.
 */
public class MDRPUnitCurveChartEntity implements Serializable {
    public String chart_type;
    public String title;
    public String[] legend;
    public String[] xAxis;
    public String yAxis;
    public ArrayList<SeriesEntity> series;
    public int stateCode = 200;

    public class SeriesEntity implements Serializable {
        public String data;
        public String name;
        public String type;
    }
}
