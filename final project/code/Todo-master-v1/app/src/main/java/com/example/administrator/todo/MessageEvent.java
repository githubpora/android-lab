package com.example.administrator.todo;

import java.util.Date;

/**
 * Created by Administrator on 2018/1/1.
 */

public class MessageEvent {
    private String type;
    private String title;
    private String details;
    private Date ddl;
    private Date remind_time;
    MessageEvent(String type, String title, String details, Date ddl, Date remind_time) {
        this.type = type;
        this.title = title;
        this.details = details;
        this.ddl = ddl;
        this.remind_time = remind_time;
    }
    MessageEvent(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public Date getDdl() {
        return ddl;
    }

    public Date getRemind_time() {
        return remind_time;
    }
}
