package com.intfocus.hx.dashboard.kpi.bean;

/**
 * Created by liuruilin on 2017/5/19.
 */

public class MessageData {
    /**
     * id : 26
     * title : 生意人功能说明
     * content : <br/>苹果下载地址：https://www.pgyer.com/yh-i<br/>
     * created_at : 2017-03-14T13:23:05.000+08:00
     */

    private int id;
    private String title;
    private String content;
    private String created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
