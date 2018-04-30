package com.example.administrator.todo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/1.
 */

// 成就Fragment的事件处理类

public class AchievementFragment extends Fragment {
    View view;
    private BarChart barchart;
    private ImageView mon_good;
    private ImageView mon_bad;
    private ImageView tue_good;
    private ImageView tue_bad;
    private ImageView wed_good;
    private ImageView wed_bad;
    private ImageView thu_good;
    private ImageView thu_bad;
    private ImageView fri_good;
    private ImageView fri_bad;
    private ImageView sat_good;
    private ImageView sat_bad;
    private ImageView sun_good;
    private ImageView sun_bad;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_achievement, container, false);
        initview();
        setdata();
        setattr();
        return view;
    }

    private void initview() {
        barchart = (BarChart) view.findViewById(R.id.barchart);
        mon_good = (ImageView) view.findViewById(R.id.mongood);
        mon_bad = (ImageView) view.findViewById(R.id.monbad);
        mon_good.setVisibility(View.INVISIBLE);
        mon_bad.setVisibility(View.INVISIBLE);

        tue_good = (ImageView) view.findViewById(R.id.tuegood);
        tue_bad = (ImageView) view.findViewById(R.id.tuebad);
        tue_good.setVisibility(View.INVISIBLE);
        tue_bad.setVisibility(View.INVISIBLE);

        wed_good = (ImageView) view.findViewById(R.id.wedgood);
        wed_bad = (ImageView) view.findViewById(R.id.wedbad);
        wed_good.setVisibility(View.INVISIBLE);
        wed_bad.setVisibility(View.INVISIBLE);

        thu_good = (ImageView) view.findViewById(R.id.thugood);
        thu_bad = (ImageView) view.findViewById(R.id.thubad);
        thu_good.setVisibility(View.INVISIBLE);
        thu_bad.setVisibility(View.INVISIBLE);

        fri_good = (ImageView) view.findViewById(R.id.frigood);
        fri_bad = (ImageView) view.findViewById(R.id.fribad);
        fri_good.setVisibility(View.INVISIBLE);
        fri_bad.setVisibility(View.INVISIBLE);

        sat_good = (ImageView) view.findViewById(R.id.satgood);
        sat_bad = (ImageView) view.findViewById(R.id.satbad);
        sat_good.setVisibility(View.INVISIBLE);
        sat_bad.setVisibility(View.INVISIBLE);

        sun_good = (ImageView) view.findViewById(R.id.sungood);
        sun_bad = (ImageView) view.findViewById(R.id.sunbad);
        sun_good.setVisibility(View.INVISIBLE);
        sun_bad.setVisibility(View.INVISIBLE);
    }

    private void setdata() {

        ArrayList<String> xvals = new ArrayList<>(); // x轴方向数据
        xvals.add("Monday");
        xvals.add("TuesDay");
        xvals.add("WednesDay");
        xvals.add("Thursday");
        xvals.add("FriDay");
        xvals.add("SaturDay");
        xvals.add("SunDay");

        ArrayList<BarEntry> yvals = new ArrayList<>();  // y轴方向数据
            /* 将这个换为数据库中的每天的认为完成数量的比例，通过查询周一的总数量，和周一完成的任务数量来确定*/
        Calendar calendar = Calendar.getInstance();
        myDatabase mydatebase = new myDatabase(getContext());
        int judge = calendar.get(Calendar.DAY_OF_WEEK);
        Log.e("date", ""+judge);
        ArrayList<TaskInstance> all;
        if (judge == 1) {
            for (int i = 0; i < 7; i++) {
                if (i == 0) calendar.add(Calendar.DAY_OF_MONTH, i-6);
                else calendar.add(Calendar.DAY_OF_MONTH, 1);
                int y = calendar.get(Calendar.YEAR);
                int m = calendar.get(Calendar.MONTH);
                 int d = calendar.get(Calendar.DAY_OF_MONTH);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                Date date = new Date(y - 1900, m, d, 0, 0, 0);
                Log.e("query", date.toString());
                Log.e("query", String.valueOf(date.getTime()));
                all = mydatebase.queryByDate(date);
                 int size = all.size();
                Log.e("date", "size: "+size );
                int finish = 0;
                for (int j = 0; j < size; j++) {
                TaskInstance temp = all.get(j);
                if (temp.getCompleted()) finish++;
                 }
                Log.e("date", "finish"+finish);
                 if (size == 0) yvals.add(new BarEntry(0, i));
                else {
                    float value = (float) finish / (float) size;
                     Log.e("date", "bili "+value);
                    yvals.add(new BarEntry(value, i));
                }
             }
         }
         else {
             for (int i = 0; i < 7; i++) {
                 if (i == 0) calendar.add(Calendar.DAY_OF_MONTH, i-judge+2);
                 else calendar.add(Calendar.DAY_OF_MONTH, 1);
                 int y = calendar.get(Calendar.YEAR);
                 int m = calendar.get(Calendar.MONTH);
                 int d = calendar.get(Calendar.DAY_OF_MONTH);
                 int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                 Date date = new Date(y - 1900, m, d, 0, 0, 0);
                 Log.e("query", date.toString());
                 Log.e("query", String.valueOf(date.getTime()));
                 all = mydatebase.queryByDate(date);
                 int size = all.size();
                 Log.e("date", "size: "+size );
                 int finish = 0;
                 for (int j = 0; j < size; j++) {
                     TaskInstance temp = all.get(j);
                     if (temp.getCompleted()) finish++;
                 }
                 Log.e("date", "finish"+finish);
                 if (size == 0) yvals.add(new BarEntry(0, i));
                 else {
                     float value = (float) finish / (float) size;
                     yvals.add(new BarEntry(value, i));
                     Log.e("date", "bili "+value);
                 }
             }
         }

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yvals, "collection");
        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet);

        BarData barData = new BarData(xvals, barDataSet);
        barchart.setData(barData);
    }
    private void setattr() {
        barchart.setDescription("");
        barchart.setDrawValueAboveBar(true);
        barchart.getXAxis().setDrawGridLines(false);
        barchart.animateXY(2000, 2000);
        //barchart.setDoubleTapToZoomEnabled(true);//双击缩放
        // barchart.setScaleEnabled(true);  // 可以缩放
        barchart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); // x轴在下方
        barchart.getAxisRight().setEnabled(false);

        /* 当点击条形图entry时，显示今天任务完成卡通图，如果完成等于超过50%，则为笑脸，否则为哭脸
        * 我们要做的与数据库部分的连接就是以周为单位查询例如：周一的任务有几个，周一的任务完成的有几个*/
        barchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                if (e.getXIndex() == 0) {
                    Log.d("Monday", "monday");
                    if (e.getVal() >= 0.5) {
                        Log.d("Monday", "mondaygood");
                        mon_good.setVisibility(View.VISIBLE);
                    } else {
                        Log.d("Monday", "mondaybad");
                        mon_bad.setVisibility(View.VISIBLE);
                    }
                } else if (e.getXIndex() == 1) {
                    if (e.getVal() >= 0.5) {
                        tue_good.setVisibility(View.VISIBLE);
                    } else {
                        tue_bad.setVisibility(View.VISIBLE);
                    }
                } else if (e.getXIndex() == 2) {
                    if (e.getVal() >= 0.5) {
                        wed_good.setVisibility(View.VISIBLE);
                    } else {
                        wed_bad.setVisibility(View.VISIBLE);
                    }
                } else if (e.getXIndex() == 3) {
                    if (e.getVal() >= 0.5) {
                        thu_good.setVisibility(View.VISIBLE);
                    } else {
                        thu_bad.setVisibility(View.VISIBLE);
                    }
                } else if (e.getXIndex() == 4) {
                    if (e.getVal() >= 0.5) {
                        fri_good.setVisibility(View.VISIBLE);
                    } else {
                        fri_bad.setVisibility(View.VISIBLE);
                    }
                } else if (e.getXIndex() == 5) {
                    if (e.getVal() >= 0.5) {
                        sat_good.setVisibility(View.VISIBLE);
                    } else {
                        sat_bad.setVisibility(View.VISIBLE);
                    }
                } else if (e.getXIndex() == 6) {
                    if (e.getVal() >= 0.5) {
                        sun_good.setVisibility(View.VISIBLE);
                    } else {
                        sun_bad.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected() {
                mon_good.setVisibility(View.INVISIBLE);
                mon_bad.setVisibility(View.INVISIBLE);
                tue_good.setVisibility(View.INVISIBLE);
                tue_bad.setVisibility(View.INVISIBLE);
                wed_good.setVisibility(View.INVISIBLE);
                wed_bad.setVisibility(View.INVISIBLE);
                thu_good.setVisibility(View.INVISIBLE);
                thu_bad.setVisibility(View.INVISIBLE);
                fri_good.setVisibility(View.INVISIBLE);
                fri_bad.setVisibility(View.INVISIBLE);
                sat_good.setVisibility(View.INVISIBLE);
                sat_bad.setVisibility(View.INVISIBLE);
                sun_good.setVisibility(View.INVISIBLE);
                sun_bad.setVisibility(View.INVISIBLE);
            }
        });
    }
}
