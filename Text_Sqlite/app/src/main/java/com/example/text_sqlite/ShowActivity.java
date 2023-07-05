package com.example.text_sqlite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ShowActivity extends AppCompatActivity implements View.OnClickListener {
    TextView title, message, Address_book, discover, me;
    ImageView message_Img, Address_Img, discover_Img, me_Img, icon_button, find_button;
    VP_layout1 VP1;
    VP_layout2 VP2;
    VP_layout3 VP3;
    VP_layout4 VP4;
    ViewPager viewpager;
    Toast mToast;
    RelativeLayout message_layout, address_layout, discover_layout, me_layout;
    Bundle bundle;
    AlertDialog dialog;
    private final String[] letter = {"#", "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"};
    private final String[] titles = {"一尾", "二尾", "三尾", "四尾", "五尾", "六尾", "七尾", "八尾", "九尾"};
    private final String[] info = {"守鹤", "又旅", "矶抚", "孙悟空", "穆王", "犀犬", "重明", "牛鬼", "九喇嘛"};
    List<Map<String, Object>> list = new ArrayList<>();
    List<View> mLayoutList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        makeBackgroundAll();
        //设置底部引导线背景为透明
        ImmersionBar.with(this)
                .transparentBar()
                .init();
        initView();

        //查找并实例化四个界面
        @SuppressLint("InflateParams") View view1 = getLayoutInflater().inflate(R.layout.activity_vp_layout1, null);
        ListView listView = view1.findViewById(R.id.mListView);
        @SuppressLint("InflateParams") View view2 = getLayoutInflater().inflate(R.layout.activity_vp_layout2, null);
        @SuppressLint("InflateParams") View view3 = getLayoutInflater().inflate(R.layout.activity_vp_layout3, null);
        @SuppressLint("InflateParams") View view4 = getLayoutInflater().inflate(R.layout.activity_vp_layout4, null);

        //四个界面加入viewpager

        mLayoutList.add(view1);
        mLayoutList.add(view2);
        mLayoutList.add(view3);
        mLayoutList.add(view4);
        MyAdapter myAdapter = new MyAdapter(mLayoutList);
        viewpager.setAdapter(myAdapter);


        String[] titles = {"WeChat2.0", "通讯录", "发现", "我"};
        message.setTextColor(Color.parseColor("#32CD32"));
        message_Img.setColorFilter(Color.parseColor("#32CD32"));
        viewpager.setCurrentItem(0);
        viewpager.setOffscreenPageLimit(4);

        //设置viewpager点击或滑动事件监听
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                title.setText(titles[position]);
                changeColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //第一个界面ListView
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, getData(),
                R.layout.list_item_layout,
                new String[]{"picture", "title", "data"},
                new int[]{R.id.picture, R.id.title, R.id.data});
        listView.setAdapter(simpleAdapter);

        //设置第一个ListView监听
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Map<String, Object> temp = list.get(i);
            Toast.makeText(this, Objects.requireNonNull(temp.get("title")) + "-" + i, Toast.LENGTH_SHORT).show();
            Object text = Objects.requireNonNull(temp.get("title"));
            String temp1 = text.toString();
            startTalk(temp1);
        });

        //第二个界面ListView
        ListView list_two = view2.findViewById(R.id.list_two);
        SimpleAdapter simpleAdapter1 = new SimpleAdapter(this, getData(),
                R.layout.list_item_layout2,
                new String[]{"picture", "title"},
                new int[]{R.id.picture, R.id.title});
        list_two.setAdapter(simpleAdapter1);
    }

    //===========================重点!!传递title内容==================================
    private void startTalk(String temp) {
        Intent intent = new Intent(this, TalkActivity.class);
        bundle = new Bundle();
        bundle.putString("stringKey", temp);
        intent.putExtra("key", bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.newuser_translate_in, R.anim.newuser_translate_out);
        Log.i("===============", temp);
    }

    //自定义Toast
    private void mToast(Context context, String temp) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        } else {
            mToast = Toast.makeText(context,temp,Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.setText(temp);
            mToast.show();
        }
    }

    //用户返回时弹出提示框
    public void onBackPressed() {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("是否退出？")
                .setMessage("退出需重新登录")
                .setIcon(R.drawable.addicon)
                .setPositiveButton("确定", (dialog1, i) -> {
                    dialog1.dismiss();
                    ShowActivity.this.finish();
                })
                .setNegativeButton("取消", (dialog2, i) -> dialog2.dismiss());
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_style);
        dialog.show();
    }

    //随着页面改变而更改颜色
    private void changeColor(int position) {
        if (position == 0) {
            message.setTextColor(Color.parseColor("#32CD32"));
            message_Img.setColorFilter(Color.parseColor("#32CD32"));
            Address_book.setTextColor(Color.parseColor("#000000"));
            Address_Img.setColorFilter(Color.parseColor("#000000"));
            discover.setTextColor(Color.parseColor("#000000"));
            discover_Img.setColorFilter(Color.parseColor("#000000"));
            me.setTextColor(Color.parseColor("#000000"));
            me_Img.setColorFilter(Color.parseColor("#000000"));
        } else if (position == 1) {
            message.setTextColor(Color.parseColor("#000000"));
            message_Img.setColorFilter(Color.parseColor("#000000"));
            Address_book.setTextColor(Color.parseColor("#32CD32"));
            Address_Img.setColorFilter(Color.parseColor("#32CD32"));
            discover.setTextColor(Color.parseColor("#000000"));
            discover_Img.setColorFilter(Color.parseColor("#000000"));
            me.setTextColor(Color.parseColor("#000000"));
            me_Img.setColorFilter(Color.parseColor("#000000"));
        } else if (position == 2) {
            message.setTextColor(Color.parseColor("#000000"));
            message_Img.setColorFilter(Color.parseColor("#000000"));
            Address_book.setTextColor(Color.parseColor("#000000"));
            Address_Img.setColorFilter(Color.parseColor("#000000"));
            discover.setTextColor(Color.parseColor("#32CD32"));
            discover_Img.setColorFilter(Color.parseColor("#32CD32"));
            me.setTextColor(Color.parseColor("#000000"));
            me_Img.setColorFilter(Color.parseColor("#000000"));
        } else if (position == 3) {
            message.setTextColor(Color.parseColor("#000000"));
            message_Img.setColorFilter(Color.parseColor("#000000"));
            Address_book.setTextColor(Color.parseColor("#000000"));
            Address_Img.setColorFilter(Color.parseColor("#000000"));
            discover.setTextColor(Color.parseColor("#000000"));
            discover_Img.setColorFilter(Color.parseColor("#000000"));
            me.setTextColor(Color.parseColor("#32CD32"));
            me_Img.setColorFilter(Color.parseColor("#32CD32"));
        }
    }

    //动态添加ListView内容
    private List<Map<String, Object>> getData() {
        for (int i = 0; i < titles.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("picture", R.drawable.addicon);
            map.put("title", titles[i]);
            map.put("data", info[i]);
            list.add(map);
        }
        return list;
    }

    private void initView() {
        me = findViewById(R.id.me);
        title = findViewById(R.id.title);
        message = findViewById(R.id.message);
        discover = findViewById(R.id.discover);
        Address_book = findViewById(R.id.Address_book);
        icon_button = findViewById(R.id.icon_button);
        find_button = findViewById(R.id.find_button);

        me_Img = findViewById(R.id.me_Img);
        message_Img = findViewById(R.id.message_Img);
        discover_Img = findViewById(R.id.discover_Img);
        Address_Img = findViewById(R.id.Address_Img);

        VP1 = new VP_layout1();
        VP2 = new VP_layout2();
        VP3 = new VP_layout3();
        VP4 = new VP_layout4();

        viewpager = findViewById(R.id.viewpager);
        me_layout = findViewById(R.id.me_layout);
        message_layout = findViewById(R.id.message_layout);
        address_layout = findViewById(R.id.address_layout);
        discover_layout = findViewById(R.id.discover_layout);

        me_layout.setOnClickListener(this);
        message_layout.setOnClickListener(this);
        address_layout.setOnClickListener(this);
        discover_layout.setOnClickListener(this);
        icon_button.setOnClickListener(this);
        find_button.setOnClickListener(this);

    }

    @SuppressLint("ObsoleteSdkInt")
    private void makeBackgroundAll() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0 全透明实现
            //getWindow.setStatusBarColor(Color.TRANSPARENT)
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4 全透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.message_layout:
                viewpager.setCurrentItem(0, false);     //false表示取消viewpager过渡动画
                break;
            case R.id.address_layout:
                viewpager.setCurrentItem(1, false);
                break;
            case R.id.discover_layout:
                viewpager.setCurrentItem(2, false);
                break;
            case R.id.me_layout:
                viewpager.setCurrentItem(3, false);
                break;
            case R.id.icon_button:
                showTopList();
                break;
            case R.id.Relate1:
                String msg = "===";
                mToast(this, msg);
                break;
        }
    }

    private void showTopList() {
        dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width= WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        View view = LayoutInflater.from(this).inflate(R.layout.top_layout,null);
        RelativeLayout Relate1 = view.findViewById(R.id.Relate1);
        RelativeLayout Relate2 = view.findViewById(R.id.Relate2);
        RelativeLayout Relate3 = view.findViewById(R.id.Relate3);
        RelativeLayout Relate4 = view.findViewById(R.id.Relate4);
        window.setContentView(view);
        Relate1.setOnClickListener(view1 -> Toast.makeText(ShowActivity.this, "1", Toast.LENGTH_SHORT).show());
        Relate2.setOnClickListener(view12 -> Toast.makeText(ShowActivity.this, "2", Toast.LENGTH_SHORT).show());
        Relate3.setOnClickListener(view13 -> Toast.makeText(ShowActivity.this, "3", Toast.LENGTH_SHORT).show());
        Relate4.setOnClickListener(view14 -> Toast.makeText(ShowActivity.this, "4", Toast.LENGTH_SHORT).show());
    }
}