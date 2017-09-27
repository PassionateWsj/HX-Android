package com.intfocus.hx.dashboard.kpi.bean

/**
 * 仪表盘实体对象
 * Created by zbaoliang on 17-4-28.
 */
class MererEntity : java.io.Serializable {

    /**
     * 是否置顶显示：1为论波区，0为平铺区
     */
    var is_stick: Boolean = false
    /**
     * 仪表盘标题
     */
    var title: String? = null
    /**
     * 仪表盘所属组（大标题名称）
     */
    var group_name: String? = null
    /**
     * 仪表盘类型：line 折线图; bar 柱状图; ring 环形图; number 纯文本
     */
    var dashboard_type: String? = null
    /**
     * 外链地址
     */
    var target_url: String? = null
    /**
     * 单位（如：万元）
     */
    var unit: String? = null
    /**
     * 具体仪表数据
     */
    var data: com.intfocus.hx.dashboard.kpi.bean.MererEntity.LineEntity? = null

    inner class LineEntity : java.io.Serializable {

        var high_light: com.intfocus.hx.dashboard.kpi.bean.MererEntity.LineEntity.HighLight? = null

        var chart_data: IntArray? = null

        inner class HighLight : java.io.Serializable {
            var percentage: Boolean = false //是否显示百分比0、1
            var number: Double? = 0.toDouble() //高亮数字
            var compare: Double = 0.toDouble() //百分比
            var arrow: Int = 0 //决定箭头方向和颜色
        }
    }
}
