package com.intfocus.hx.dashboard.mine.bean;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * ****************************************************
 * author: JamesWong
 * created on: 17/08/01 下午5:35
 * e-mail: PassionateWsj@outlook.com
 * name:
 * desc:主要有两个注解:
 * 1. DatabaseTable : 用于表名的设置。
 * 2. DatabaseField : 用于设置表中的字段
 * 注意,必须要有无参数的构造方法。。。
 * ****************************************************
 */
@DatabaseTable(tableName = "push_message")
public class PushMessageBean implements Serializable {

    /**
     * type : report
     * title : 第二集群销售额
     * url : /mobile/v2/group/%@/template/4/report/8
     * obj_id : 8
     * obj_type : 1
     * debug_timestamp : 2017-08-01 15:50:51 +0800
     */
    @DatabaseField(columnName = "_id", generatedId = true)
    private Long _id;

    @DatabaseField(columnName = "type", dataType = DataType.STRING,defaultValue = "")
    private String type;

    @DatabaseField(columnName = "title", dataType = DataType.STRING,defaultValue = "")
    private String title;

    @DatabaseField(columnName = "url", dataType = DataType.STRING,defaultValue = "")
    private String url;

    @DatabaseField(columnName = "obj_id", dataType = DataType.INTEGER)
    private int obj_id;

    @DatabaseField(columnName = "obj_type", dataType = DataType.INTEGER)
    private int obj_type;

    @DatabaseField(columnName = "debug_timestamp", dataType = DataType.STRING,defaultValue = "")
    private String debug_timestamp;

    @DatabaseField(columnName = "body_title", dataType = DataType.STRING,defaultValue = "")
    private String body_title;

    @DatabaseField(columnName = "body_text", dataType = DataType.STRING,defaultValue = "")
    private String body_text;

    @DatabaseField(columnName = "new_msg", dataType = DataType.BOOLEAN,defaultValue = "true")
    private boolean new_msg;

    @DatabaseField(columnName = "user_id",dataType = DataType.INTEGER)
    private int user_id;

    //TODO 注意,必须要有无参数的构造方法。。。
    public PushMessageBean() {
    }

    public PushMessageBean(String type, String title, String url, int obj_id, int obj_type, String debug_timestamp, String body_title, String body_text, boolean new_msg) {
        this.type = type;
        this.title = title;
        this.url = url;
        this.obj_id = obj_id;
        this.obj_type = obj_type;
        this.debug_timestamp = debug_timestamp;
        this.body_title = body_title;
        this.body_text = body_text;
        this.new_msg = new_msg;
    }

    public Long get_id() {
        return _id;
    }

    public String getBody_title() {
        return body_title;
    }

    public void setBody_title(String body_title) {
        this.body_title = body_title;
    }

    public String getBody_text() {
        return body_text;
    }

    public void setBody_text(String body_text) {
        this.body_text = body_text;
    }

    public boolean getNew_msg() {
        return new_msg;
    }

    public void setNew_msg(boolean new_msg) {
        this.new_msg = new_msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getObj_id() {
        return obj_id;
    }

    public void setObj_id(int obj_id) {
        this.obj_id = obj_id;
    }

    public int getObj_type() {
        return obj_type;
    }

    public void setObj_type(int obj_type) {
        this.obj_type = obj_type;
    }

    public String getDebug_timestamp() {
        return debug_timestamp;
    }

    public void setDebug_timestamp(String debug_timestamp) {
        this.debug_timestamp = debug_timestamp;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
