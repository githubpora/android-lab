package com.example.administrator.todo;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoScrollViewPager viewPager;
    private BottomNavigationView navigation;
    private List<Fragment> listFragment;
    public static AlarmManager am;
    public static int reqnum = 0;

    // 底部导航栏监听器
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_tasks:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_calendar:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_achievements:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        initView();
        am = (AlarmManager)getSystemService(ALARM_SERVICE);
    }

    private void initView() {

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager = (NoScrollViewPager) findViewById(R.id.view_pager);

        listFragment = new ArrayList<>();
        listFragment.add(new TaskFragment());       //添加Fragment进ViewPager的数据源
        listFragment.add(new CalendarFragment());
        listFragment.add(new AchievementFragment());

        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(), this, listFragment);
        Log.d("test", "1");
        viewPager.setAdapter(adapter);

    }
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.RECORD_AUDIO",
    };


    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.RECORD_AUDIO");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
