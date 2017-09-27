package com.intfocus.hx.dashboard.kpi.bean;

import java.util.List;

/**
 * Created by liuruilin on 2017/6/21.
 */

public class TeatBean {

    /**
     * code : 200
     * message : 获取数据列表成功
     * data : [{"group_name":"Bravo实时销售","data":[{"title":"第二集群实时销售","dashboard_type":"number1","target_url":"mobile/v2/group/%@/template/4/report/30","unit":"万元","memo1":"昨日总销售额","memo2":null,"data":{"high_light":{"percentage":false,"number":"1804","compare":"6237","arrow":3},"chart_data":[]},"group_name":"Bravo实时销售","is_stick":false},{"title":"第二集群商行实时销售","dashboard_type":"number1","target_url":"mobile/v2/group/%@/template/2/report/36","unit":"万元","memo1":"昨日总销售额","memo2":null,"data":{"high_light":{"percentage":false,"number":"1804","compare":"6237","arrow":3},"chart_data":[]},"group_name":"Bravo实时销售","is_stick":false}]},{"group_name":"异常预警","data":[{"title":"个单量异常实时预警(门店)","dashboard_type":"number2","target_url":"mobile/v2/group/%@/template/2/report/67","unit":"万元","memo1":"当前 41 项个单量异常预警","memo2":null,"data":{"high_light":{"percentage":false,"number":"3248","compare":"7.63","arrow":1},"chart_data":[]},"group_name":"异常预警","is_stick":false},{"title":"生鲜负毛利实时预警(门店)","dashboard_type":"number2","target_url":"mobile/v2/group/%@/template/2/report/68","unit":"万元","memo1":"当前 6 项负毛利过高预警","memo2":null,"data":{"high_light":{"percentage":false,"number":"3248","compare":"-13.39","arrow":3},"chart_data":[]},"group_name":"异常预警","is_stick":false},{"title":"生鲜定价毛利异常实时预警(门店)","dashboard_type":"number2","target_url":"mobile/v2/group/%@/template/2/report/70","unit":"万元","memo1":"当前 204 项定价过高预警","memo2":null,"data":{"high_light":{"percentage":false,"number":"3248","compare":"-13.39","arrow":3},"chart_data":[]},"group_name":"异常预警","is_stick":false}]},{"group_name":"Bravo销售概况","data":[{"title":"第二集群销售额","dashboard_type":"number3","target_url":"mobile/v2/group/%@/template/4/report/8","unit":"万元","memo1":"昨日总销售额","memo2":"周同比增长率","data":{"high_light":{"percentage":false,"number":"5898","compare":"5579","arrow":1},"chart_data":[]},"group_name":"Bravo销售概况","is_stick":false},{"title":"第二集群月累计同店同比","dashboard_type":"number3","target_url":"mobile/v2/group/%@/template/4/report/9","unit":"万元","memo1":"月累计销售额","memo2":"月累计同比增长率","data":{"high_light":{"percentage":false,"number":"29661","compare":"-7.28","arrow":4},"chart_data":[]},"group_name":"Bravo销售概况","is_stick":false},{"title":"第二集群客流","dashboard_type":"number3","target_url":"mobile/v2/group/%@/template/4/report/10","unit":"万次","memo1":"昨日客流","memo2":"周同比增长率","data":{"high_light":{"percentage":false,"number":"94","compare":"5.62","arrow":1},"chart_data":[]},"group_name":"Bravo销售概况","is_stick":false},{"title":"第二集群客单价","dashboard_type":"number3","target_url":"mobile/v2/group/%@/template/4/report/11","unit":"元","memo1":"昨日客单价","memo2":"周同比增长率","data":{"high_light":{"percentage":false,"number":"61.85","compare":"-0.34","arrow":4},"chart_data":[]},"group_name":"Bravo销售概况","is_stick":false}]}]
     */

    private int code;
    private String message;
    private List<DataBeanXX> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBeanXX> getData() {
        return data;
    }

    public void setData(List<DataBeanXX> data) {
        this.data = data;
    }

    public static class DataBeanXX {
        /**
         * group_name : Bravo实时销售
         * data : [{"title":"第二集群实时销售","dashboard_type":"number1","target_url":"mobile/v2/group/%@/template/4/report/30","unit":"万元","memo1":"昨日总销售额","memo2":null,"data":{"high_light":{"percentage":false,"number":"1804","compare":"6237","arrow":3},"chart_data":[]},"group_name":"Bravo实时销售","is_stick":false},{"title":"第二集群商行实时销售","dashboard_type":"number1","target_url":"mobile/v2/group/%@/template/2/report/36","unit":"万元","memo1":"昨日总销售额","memo2":null,"data":{"high_light":{"percentage":false,"number":"1804","compare":"6237","arrow":3},"chart_data":[]},"group_name":"Bravo实时销售","is_stick":false}]
         */

        private String group_name;
        private List<DataBeanX> data;

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public List<DataBeanX> getData() {
            return data;
        }

        public void setData(List<DataBeanX> data) {
            this.data = data;
        }

        public static class DataBeanX {
            /**
             * title : 第二集群实时销售
             * dashboard_type : number1
             * target_url : mobile/v2/group/%@/template/4/report/30
             * unit : 万元
             * memo1 : 昨日总销售额
             * memo2 : null
             * data : {"high_light":{"percentage":false,"number":"1804","compare":"6237","arrow":3},"chart_data":[]}
             * group_name : Bravo实时销售
             * is_stick : false
             */

            private String title;
            private String dashboard_type;
            private String target_url;
            private String unit;
            private String memo1;
            private Object memo2;
            private DataBean data;
            private String group_name;
            private boolean is_stick;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDashboard_type() {
                return dashboard_type;
            }

            public void setDashboard_type(String dashboard_type) {
                this.dashboard_type = dashboard_type;
            }

            public String getTarget_url() {
                return target_url;
            }

            public void setTarget_url(String target_url) {
                this.target_url = target_url;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public String getMemo1() {
                return memo1;
            }

            public void setMemo1(String memo1) {
                this.memo1 = memo1;
            }

            public Object getMemo2() {
                return memo2;
            }

            public void setMemo2(Object memo2) {
                this.memo2 = memo2;
            }

            public DataBean getData() {
                return data;
            }

            public void setData(DataBean data) {
                this.data = data;
            }

            public String getGroup_name() {
                return group_name;
            }

            public void setGroup_name(String group_name) {
                this.group_name = group_name;
            }

            public boolean isIs_stick() {
                return is_stick;
            }

            public void setIs_stick(boolean is_stick) {
                this.is_stick = is_stick;
            }

            public static class DataBean {
                /**
                 * high_light : {"percentage":false,"number":"1804","compare":"6237","arrow":3}
                 * chart_data : []
                 */

                private HighLightBean high_light;
                private List<?> chart_data;

                public HighLightBean getHigh_light() {
                    return high_light;
                }

                public void setHigh_light(HighLightBean high_light) {
                    this.high_light = high_light;
                }

                public List<?> getChart_data() {
                    return chart_data;
                }

                public void setChart_data(List<?> chart_data) {
                    this.chart_data = chart_data;
                }

                public static class HighLightBean {
                    /**
                     * percentage : false
                     * number : 1804
                     * compare : 6237
                     * arrow : 3
                     */

                    private boolean percentage;
                    private String number;
                    private String compare;
                    private int arrow;

                    public boolean isPercentage() {
                        return percentage;
                    }

                    public void setPercentage(boolean percentage) {
                        this.percentage = percentage;
                    }

                    public String getNumber() {
                        return number;
                    }

                    public void setNumber(String number) {
                        this.number = number;
                    }

                    public String getCompare() {
                        return compare;
                    }

                    public void setCompare(String compare) {
                        this.compare = compare;
                    }

                    public int getArrow() {
                        return arrow;
                    }

                    public void setArrow(int arrow) {
                        this.arrow = arrow;
                    }
                }
            }
        }
    }
}
