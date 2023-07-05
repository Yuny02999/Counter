package com.example.text_sqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TalkActivity extends AppCompatActivity implements View.OnClickListener {
    TextView titles;
    ImageView returnButton, more, voice, icon, face;
    EditText content;
    Bundle bundle;
    Button send;
    Handler handler;
    DataOutputStream outputStream;
    DataInputStream inputStream;
    Socket socket;
    String receive_msg, send_msg = "";
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.INTERNET
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);
        initView();
        applypermission();

        Intent intent = getIntent();
        bundle = intent.getBundleExtra("key");
        String titleDate = bundle.getString("stringKey");
        titles.setText(titleDate);

        

        //循环判断edittext中是否有内容并作出改变
        Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public void run() {
                        String data = content.getText().toString().trim();
                        if (data.length() >= 1) {
                            send.setBackgroundResource(R.drawable.talk_sendbutton);
                            send.setText("发送");
                            send.setTextSize(15);
                            send.getLayoutParams().height = face.getLayoutParams().height + 90;
                            send.getLayoutParams().width = face.getLayoutParams().width + 130;
                        } else {
                            send.setBackgroundResource(R.drawable.icon_button);
                            send.getLayoutParams().width = face.getLayoutParams().width + 100;
                            send.getLayoutParams().height = face.getLayoutParams().height + 100;
                            send.setText("");
                        }
                        handler.postDelayed(this, 200);
                    }
                });
            }
        }).start();
    }

    private void applypermission() {
        if(Build.VERSION.SDK_INT>=23){
            boolean needapply=false;
            for(int i=0;i<PERMISSIONS_STORAGE.length;i++){
                int chechpermission= ContextCompat.checkSelfPermission(getApplicationContext(),
                        PERMISSIONS_STORAGE[i]);
                if(chechpermission!= PackageManager.PERMISSION_GRANTED){
                    needapply=true;
                }
            }
            if(needapply){
                ActivityCompat.requestPermissions(TalkActivity.this,PERMISSIONS_STORAGE,1);
            }
        }
    }

    private void initView() {
        titles = findViewById(R.id.titles);
        returnButton = findViewById(R.id.returnButton);
        more = findViewById(R.id.more);
        voice = findViewById(R.id.voice);
//        icon = findViewById(R.id.icon);
        face = findViewById(R.id.face);
        content = findViewById(R.id.content);
        send = findViewById(R.id.send);

        send.setOnClickListener(this);
        returnButton.setOnClickListener(this);
        more.setOnClickListener(this);
        voice.setOnClickListener(this);
//        icon.setOnClickListener(this);
        face.setOnClickListener(this);
        content.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.returnButton:
                finish();
                break;
        }
    }
}