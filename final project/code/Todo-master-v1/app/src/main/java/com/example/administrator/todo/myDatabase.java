package com.example.administrator.todo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/31.
 */

public class myDatabase extends SQLiteOpenHelper{
    private static final String DB_NAME = "Tasks.db";
    private static final String TABLE_NAME = "Tasks";
    private static final int DB_VERSION = 1;
    private static Context mainCon = null;
    private static final String SQL_CREATE_TABLE = "create table "+ TABLE_NAME
            + "(_id integer primary key autoincrement,"
            + " title text,"
            + "details text,"
            + "deadline integer,"
            + "remind_time integer,"
            + "completed integer);";
    public myDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mainCon = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // no action
    }

    // 向数据库插入新的Task，需要提供：title, details, deadline(精确到日期的Date， 如"2017-10-10 00:00"),
    //      remind_time(如果设置了提醒时间，则是一个精确到秒的Date类型，如"2017-10-10 12:34，未设置则为null)
    //      isCompleted(是否完成的标记)
    public void insert(String title, String details, Date deadline, Date remind_time, boolean isCompleted) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("details", details);
        // Date类型插入数据库时，转化成long类型的毫秒数插入
        long temp = deadline.getTime();
        temp /= 1000;
        values.put("deadline", temp*1000);
        if (remind_time != null) {
            values.put("remind_time", remind_time.getTime());
            //闹钟题型，震动
            Calendar cal= Calendar.getInstance();
            cal.setTime(remind_time);
            Intent intent=new Intent("start");
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            intent.putExtras(bundle);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mainCon,MainActivity.reqnum++,intent,0);
            //MainActivity.am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
            //calendar.add(Calendar.SECOND, 10);
            MainActivity.am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        } else {
            // 如果未设置remindTime，字段值插入-1
            values.put("remind_time", -1);
        }
        values.put("completed", isCompleted ? 1 : 0);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // 更新Task 此处需要提供Task的ID
    // 用于修改Task页面后，更新到数据库
    public void update(int id, String title, String details, Date deadline, Date remind_time, boolean isCompleted) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "_id = ?";
        String[] whereArgs = {String.valueOf(id)};
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("details", details);
        values.put("deadline", deadline.getTime());
        if (remind_time != null) {
            values.put("remind_time", remind_time.getTime());
        } else {
            values.put("remind_time", -1);
        }
        values.put("completed", isCompleted ? 1 : 0);
        db.update(TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }
    // 根据TaskId删除Task
    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "_id = ?";
        String[] whereArgs = {String.valueOf(id)};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }
    // 获取deadline为date的所有Task
    // 因为deadline是精确到天的date，所以查询的date也必须是精确到天的date，即minute和second都要为0
    // 用于获取deadline为某一天的所有任务
    public ArrayList<TaskInstance> queryByDate(Date date) {
        Log.e("query", "ing");
        ArrayList<TaskInstance> ret= new ArrayList<TaskInstance>();
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {"_id", "title", "details", "deadline", "remind_time", "completed"};
        String selection = "deadline = ?";
        String[] selectionArgs = {String.valueOf(date.getTime())};
        //String[] selectionArgs = {date.toString()};
        Log.e("query", String.valueOf(date.getTime()));
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (!cursor.moveToFirst()) {
            Log.e("query", "error");
            db.close();
            cursor.close();
            return ret;
        } else {
            do {
                Date ddl, remind;
                Long time1 = cursor.getLong(3);
                ddl = new Date();
                ddl.setTime(time1);
                Long time2 = cursor.getLong(4);
                if (time2 == -1) {
                    remind = null;
                } else {
                    remind = new Date();
                    remind.setTime(time2);
                }
                TaskInstance task = new TaskInstance(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        ddl,
                        remind,
                        cursor.getInt(5) == 1
                );
                ret.add(task);
                Log.e("query", ""+ddl);
            } while (cursor.moveToNext());
            db.close();
            cursor.close();
            return ret;
        }
    }

    //  获取数据库中所有Task
    public ArrayList<TaskInstance> getAllTasks() {
        ArrayList<TaskInstance> ret = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {"_id", "title", "details", "deadline", "remind_time", "completed"};
        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        } else {
            do {
                Date ddl, remind;
                Long time1 = cursor.getLong(3);
                Log.e("in database", time1+"");
                ddl = new Date();
                ddl.setTime(time1);
                Log.e("query", "datebase"+ddl.toString());
                Log.e("query", "database"+String.valueOf(ddl.getTime()));
                Long time2 = cursor.getLong(4);
                if (time2 == -1) {
                    remind = null;
                } else {
                    remind = new Date();
                    remind.setTime(time2);
                }
                TaskInstance task = new TaskInstance(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        ddl,
                        remind,
                        cursor.getInt(5) == 1
                );
                ret.add(task);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return ret;
    }

    // 获取最大id
    // 因为id递增 所以最大id为最新添加的Task的id
    // 用于向数据库添加新的Task之后 获取此Task的id
    public int getNewestID() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("select _id from tasks", null);
        int ret = -1;
        cursor.moveToFirst();
        do {
            if (cursor.getInt(0) > ret) {
                ret = cursor.getInt(0);
            }
        } while (cursor.moveToNext());
        return ret;
    }
}
