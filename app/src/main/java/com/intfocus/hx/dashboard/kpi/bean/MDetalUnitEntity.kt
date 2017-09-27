package com.intfocus.hx.dashboard.kpi.bean

/**
 * 仪表盘详情页面每个单元模块数据
 * Created by zbaoliang on 17-5-7.
 */
class MDetalUnitEntity {
    /**
     * 图表类型：
     * banner 标题栏;
     * chart 曲线图表;
     * info 一般标签(附标题);
     * single_value 单值组件;
     * line-or-bar 柱状图(竖);
     * bargraph 条状图(横);
     * tables#v3 类Excel冻结横竖首列表格;
     */
    var type: String? = null
    var config: String? = null
}
