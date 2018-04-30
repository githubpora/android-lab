package com.example.administrator.todo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2018/1/1.
 */
// ViewPager的FragmentAdapter
// 原理类似ListView的Adapter

public class MyFragmentAdapter extends FragmentPagerAdapter {
    Context context;
    List<Fragment> listFragment;

    public MyFragmentAdapter(FragmentManager fm, Context context, List<Fragment> listFragment) {
        super(fm);
        this.context = context;
        this.listFragment = listFragment;
    }
    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }
}
