package com.intfocus.hx.dashboard.kpi.bean

import java.io.Serializable

/**
 * Created by liuruilin on 2017/6/21.
 */

class KpiData : Serializable {

    /**
     * code : 200
     * message : 获取数据列表成功
     * data : [{"group_name":"Bravo销售概况","data":[{"title":"第二集群销售额","dashboard_type":"number3","target_url":"mobile/v2/group/%@/template/4/report/8","unit":"万元","memo1":"昨日总销售额","memo2":"周同比增长率","data":{"high_light":{"percentage":false,"number":"5898","compare":"5579","arrow":1},"chart_data":[]},"group_name":"Bravo销售概况","is_stick":false},{"title":"第二集群月累计同店同比","dashboard_type":"number3","target_url":"mobile/v2/group/%@/template/4/report/9","unit":"万元","memo1":"月累计销售额","memo2":"月累计同比增长率","data":{"high_light":{"percentage":false,"number":"29661","compare":"-7.28","arrow":4},"chart_data":[]},"group_name":"Bravo销售概况","is_stick":false},{"title":"第二集群客流","dashboard_type":"number3","target_url":"mobile/v2/group/%@/template/4/report/10","unit":"万次","memo1":"昨日客流","memo2":"周同比增长率","data":{"high_light":{"percentage":false,"number":"94","compare":"5.62","arrow":1},"chart_data":[]},"group_name":"Bravo销售概况","is_stick":false},{"title":"第二集群客单价","dashboard_type":"number3","target_url":"mobile/v2/group/%@/template/4/report/11","unit":"元","memo1":"昨日客单价","memo2":"周同比增长率","data":{"high_light":{"percentage":false,"number":"61.85","compare":"-0.34","arrow":4},"chart_data":[]},"group_name":"Bravo销售概况","is_stick":false}]}]
     */

    var code: Int = 0
    var message: String? = null
    var data: List<KpiGroup>? = null

    class KpiGroup : Serializable {
        /**
         * group_name : Bravo销售概况
         * data : [{"title":"第二集群销售额","dashboard_type":"number3","target_url":"mobile/v2/group/%@/template/4/report/8","unit":"万元","memo1":"昨日总销售额","memo2":"周同比增长率","data":{"high_light":{"percentage":false,"number":"5898","compare":"5579","arrow":1},"chart_data":[]},"group_name":"Bravo销售概况","is_stick":false},{"title":"第二集群月累计同店同比","dashboard_type":"number3","target_url":"mobile/v2/group/%@/template/4/report/9","unit":"万元","memo1":"月累计销售额","memo2":"月累计同比增长率","data":{"high_light":{"percentage":false,"number":"29661","compare":"-7.28","arrow":4},"chart_data":[]},"group_name":"Bravo销售概况","is_stick":false},{"title":"第二集群客流","dashboard_type":"number3","target_url":"mobile/v2/group/%@/template/4/report/10","unit":"万次","memo1":"昨日客流","memo2":"周同比增长率","data":{"high_light":{"percentage":false,"number":"94","compare":"5.62","arrow":1},"chart_data":[]},"group_name":"Bravo销售概况","is_stick":false},{"title":"第二集群客单价","dashboard_type":"number3","target_url":"mobile/v2/group/%@/template/4/report/11","unit":"元","memo1":"昨日客单价","memo2":"周同比增长率","data":{"high_light":{"percentage":false,"number":"61.85","compare":"-0.34","arrow":4},"chart_data":[]},"group_name":"Bravo销售概况","is_stick":false}]
         */

        var group_name: String? = null
        var data: List<KpiGroupItem>? = null

        class KpiGroupItem : Serializable {
            /**
             * title : 第二集群销售额
             * dashboard_type : number3
             * target_url : mobile/v2/group/%@/template/4/report/8
             * unit : 万元
             * memo1 : 昨日总销售额
             * memo2 : 周同比增长率
             * data : {"high_light":{"percentage":false,"number":"5898","compare":"5579","arrow":1},"chart_data":[]}
             * group_name : Bravo销售概况
             * is_stick : false
             */

            var title: String? = null
            var dashboard_type: String? = null
            var target_url: String? = null
            var unit: String? = null
            var memo1: String? = null
            var memo2: String? = null
            var data: KpiGroupItemData? = null
            var group_name: String? = null
            var isIs_stick: Boolean = false

            class KpiGroupItemData : Serializable {
                /**
                 * high_light : {"percentage":false,"number":"5898","compare":"5579","arrow":1}
                 * chart_data : []
                 */

                var high_light: HighLightBean? = null
                var chart_data: List<*>? = null

                class HighLightBean : Serializable {
                    /**
                     * percentage : false
                     * number : 5898
                     * compare : 5579
                     * arrow : 1
                     */

                    var isPercentage: Boolean = false
                    var number: String? = null
                    var compare: String? = null
                    var arrow: Int = 0
                }
            }
        }
    }
}
