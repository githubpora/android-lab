package com.example.administrator.todo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/1.
 */
// 日历Fragment的事件处理类

public class CalendarFragment extends Fragment {
    View view;
    private CalendarView calendarView;  // 布局内的控件
    private Date date; // 自定义的日期对象
    private List<Map<String, Object>> mData;
    private ListView listView;
    private SimpleAdapter adapter;
    private myDatabase db;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = (CalendarView)view.findViewById(R.id.calendar);
        date = new Date(0, 0, 0, 0, 0, 0);
        mData = new ArrayList<>();
        listView = (ListView)view.findViewById(R.id.list_calendar_task);
        adapter = new SimpleAdapter(getActivity(), mData, R.layout.item_task,
                new String[]{"title", "completed"},
                new int[] {R.id.text_item_title, R.id.text_item_isCompleted});

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
                View dialogview = layoutInflater.inflate(R.layout.detailview, null);


                TextView dialogtitle = (TextView)dialogview.findViewById(R.id.dialogtitle);
                TextView dialogdetial = (TextView)dialogview.findViewById(R.id.dialogdetail);
                TextView dialogdeadline = (TextView)dialogview.findViewById(R.id.dialogdeadline);
                TextView dialogstatus = (TextView)dialogview.findViewById(R.id.dialogstatus);
                Map<String, Object> temp = (Map<String, Object>)parent.getItemAtPosition(position);
                String title = temp.get("title").toString();
                String detial = temp.get("detail").toString();
                String deadline = temp.get("deadline").toString();

                dialogtitle.setText(title);
                dialogdetial.setText("任务详情："+detial);
                dialogdeadline.setText("到期时间："+deadline);
                String isfinish = temp.get("completed").toString();
                String status = "";
                if (isfinish.equals("√")) {
                    status = "状态       已完成";
                } else if (isfinish.equals("--")) {
                    status = "状态       待完成";
                }
                dialogstatus.setText(status);
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(view.getContext());

                AlertDialog dialog = builder.setView(dialogview).
                        setPositiveButton("关闭", null).create();

                // 设置动画
                Window window = dialog.getWindow();
                window.setWindowAnimations(R.style.mystyle);
                dialog.show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                // 长按出现弹出式菜单，选项： [标记为已完成/未完成]，[删除此任务]   (还有其它选项可以自行添加)
                // 标记完成的处理函数：要更新数据库，同时要更新mDatas的completed值，已完成则为"√"，未完成则为"--"，然后notifyDatachanged
                // 删除任务的处理函数：根据id删除数据库中的Task，同时删除mDatas中对应的值，然后notify
                // 待完成

                // 创建弹出式菜单对象
                // 这里用了弹出式菜单，但是弹出式菜单有一个缺点就是只能选择一项，所以选择了已完成就不能选择删除
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                // 获取菜单填充器
                MenuInflater inflater = popupMenu.getMenuInflater();
                // 填充菜单
                inflater.inflate(R.menu.popmenu, popupMenu.getMenu());
                // 绑定菜单项的点击事件
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.finish:
                                mData.get(position).put("completed", "√");
                                adapter.notifyDataSetChanged();
                                db = new myDatabase(getActivity());
                                db.update((int)mData.get(position).get("id"),
                                        mData.get(position).get("title").toString(),
                                        mData.get(position).get("detail").toString(),
                                        (Date)mData.get(position).get("deadline"),
                                        (Date)mData.get(position).get("remind_time"),
                                        true);
                                Toast.makeText(view.getContext(), "您已完成此任务！", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.delete:
                                db.delete((int)mData.get(position).get("id"));
                                mData.remove(position);
                                adapter.notifyDataSetChanged();
                                EventBus.getDefault().post(new MessageEvent("dele"));
                                Toast.makeText(view.getContext(), "您已删除此任务！", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return true;
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                mData.clear();
                adapter.notifyDataSetChanged();
                date.setYear(year-1900);
                date.setMonth(month);
                date.setDate(dayOfMonth);
                date.setHours(0);
                date.setMinutes(0);
                date.setSeconds(0);
                Log.e("date1", date.toString());
                setDatas();
            }
        });
        return view;
    }

    private void setDatas() {
        db = new myDatabase(getActivity());
        ArrayList<TaskInstance> list = db.queryByDate(date);
        Log.e("size1", ""+list.size());
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> temp = new HashMap<>();
                temp.put("id", list.get(i).getId());
                temp.put("title", list.get(i).getTitle());
                temp.put("detail", list.get(i).getDetails());
                temp.put("deadline", list.get(i).getDeadline());
                temp.put("remind_time", list.get(i).getRemind_time());
                temp.put("completed", list.get(i).getCompleted() ? "√" : "--");

                mData.add(temp);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
