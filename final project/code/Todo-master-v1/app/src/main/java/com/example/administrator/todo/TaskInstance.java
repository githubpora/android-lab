package com.example.administrator.todo;

import java.util.Date;

/**
 * Created by Administrator on 2017/12/31.
 */

public class TaskInstance {
    private int id;
    private String title;
    private String details;
    private Date deadline;
    private Date remind_time;
    private boolean isCompleted;
    TaskInstance() {
        id = -1;
        title = null;
        details = null;
        deadline = null;
        remind_time = null;
        isCompleted = false;
    }
    TaskInstance(int id, String title, String details, Date deadline, Date remind_time, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.deadline = deadline;
        this.remind_time = remind_time;
        this.isCompleted = isCompleted;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setRemind_time(Date remind_time) {
        this.remind_time = remind_time;
    }

    public Date getRemind_time() {
        return remind_time;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
    public boolean getCompleted() {
        return isCompleted;
    }
}
