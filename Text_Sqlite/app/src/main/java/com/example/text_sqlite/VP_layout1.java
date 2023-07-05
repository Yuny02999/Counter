package com.example.text_sqlite;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VP_layout1 extends AppCompatActivity {
    ListView mListView;
    TextView data, title;
    ImageView image;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vp_layout1);
        initView();

    }

    private void mToast(String temp) {
        if (toast == null){
            Toast.makeText(this, temp, Toast.LENGTH_SHORT).show();
        }else {
            toast.setText(temp);
            toast.setDuration(Toast.LENGTH_SHORT);
        }

    }


    private void initView() {
        data = findViewById(R.id.data);
        title = findViewById(R.id.title);
        mListView = findViewById(R.id.mListView);

    }
}