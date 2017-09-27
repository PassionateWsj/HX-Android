package com.intfocus.hx.subject.template_v2.entity;

import java.io.Serializable;

/**
 * 仪表盘实体对象
 * Created by zbaoliang on 17-4-28.
 */
public class MererEntity implements Serializable {

    /**
     * 是否置顶显示：1为论波区，0为平铺区
     */
    public boolean is_stick;
    /**
     * 仪表盘标题
     */
    public String title;
    /**
     * 仪表盘所属组（大标题名称）
     */
    public String group_name;
    /**
     * 仪表盘类型：line 折线图; bar 柱状图; ring 环形图; number 纯文本
     */
    public String dashboard_type;
    /**
     * 外链地址
     */
    public String target_url;
    /**
     * 单位（如：万元）
     */
    public String unit;
    /**
     * 具体仪表数据
     */
    public LineEntity data;


    public class LineEntity implements Serializable {

        public HighLight high_light;

        public int[] chart_data;

        public class HighLight implements Serializable {
            public boolean percentage;//是否显示百分比0、1
            public double number;//高亮数字
            public double compare;//百分比
            public int arrow;//决定箭头方向和颜色
        }
    }
}
