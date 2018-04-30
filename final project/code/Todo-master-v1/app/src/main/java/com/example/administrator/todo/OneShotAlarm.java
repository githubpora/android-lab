package com.example.administrator.todo;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;


/**
 * Created by ricky on 2018/1/5.
 */

public class OneShotAlarm extends BroadcastReceiver {
    private String title;
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "到时间啦", Toast.LENGTH_LONG).show();
        Vibrator mVibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        mVibrator.vibrate(new long[]{1000, 3000}, -1);
        Bundle bundle = intent.getExtras();
        title = bundle.getString("title");

        Intent clickintent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, clickintent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(title).
                setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.icon)).
                setSmallIcon(R.drawable.icon).
                setContentText("您的任务"+ title+ "到时间啦！请开始完成。").
                setContentIntent(pendingIntent).setAutoCancel(true);
        Notification notify = builder.build();
        notifyManager.notify(0,notify);
    }
}