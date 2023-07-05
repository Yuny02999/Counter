package com.example.text_sqlite;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class MyAdapter extends PagerAdapter {

    private final List<View> mListAdapter;

    public MyAdapter(List<View> mListAdapter) {
        this.mListAdapter = mListAdapter;
    }

    @Override
    public int getCount() {
        return mListAdapter.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(mListAdapter.get(position), 0);
        return mListAdapter.get(position);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mListAdapter.get(position));
    }
}
