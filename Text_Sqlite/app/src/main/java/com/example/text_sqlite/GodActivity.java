package com.example.text_sqlite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.immersionbar.ImmersionBar;

public class GodActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn1, btn2, btn3, btn4;
    ImageButton returnButton;
    EditText edt1, edt2;
    TextView find_data;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_god);
        makeBackgroundAll();
        //设置底部引导线背景为透明
        ImmersionBar.with(this)
                .transparentBar()
                .init();
        initView();

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        returnButton.setOnClickListener(this);

    }

    //使手机顶部导航栏融入背景
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

    private void initView() {
        btn1 = findViewById(R.id.button);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);
        edt1 = findViewById(R.id.edt1);
        edt2 = findViewById(R.id.edt2);
        find_data = findViewById(R.id.find_data);
        returnButton = findViewById(R.id.returnButton);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        MyHelper myHelper = new MyHelper(getApplicationContext());
        SQLiteDatabase db = myHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        switch (view.getId()) {
            case R.id.returnButton:
                finish();
                break;

            case R.id.button:
                String user = edt1.getText().toString();
                String pass = edt2.getText().toString();
                if (TextUtils.isEmpty(user) | TextUtils.isEmpty(pass)) {
                    Toast toast = new Toast(this);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    Toast.makeText(this, "账号或密码为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                SQLiteDatabase db2 = myHelper.getReadableDatabase();
                String sql = "select user from text_table where user=?";
                Cursor cursor_data = db2.rawQuery(sql, new String[]{user});

                if (cursor_data.moveToFirst()) {
                    @SuppressLint("Range") String cursor_ = cursor_data.getString(cursor_data.getColumnIndex("user"));
                    if (user.equals(cursor_)) {
                        Toast.makeText(this, "用户名已存在", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                cv.put("user", user);
                cv.put("pwd", pass);
                long i = db.insert("text_table", null, cv);
                if (i > 0) {
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "failure", Toast.LENGTH_SHORT).show();
                }
                cursor_data.close();
                db2.close();
                break;

            case R.id.button2:
                dialog = new AlertDialog.Builder(this).create();
                dialog.show();
                //弹出输入法
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                //允许取消
                dialog.setCancelable(true);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.width = 700;
                lp.height = 500;
                window.setAttributes(lp);
                View view_dialog = LayoutInflater.from(this).inflate(R.layout.change_layout, null);
                window.setContentView(view_dialog);
                final EditText edt = view_dialog.findViewById(R.id.change_edt1);
                final Button del_button = view_dialog.findViewById(R.id.del_button);
                del_button.setOnClickListener(view12 -> {
                    String New_edt = edt.getText().toString();
                    if (New_edt.isEmpty()) {
                        Toast.makeText(GodActivity.this, "user为空", Toast.LENGTH_SHORT).show();
                    } else {
                        db.delete("text_table", "user=?", new String[]{New_edt});
                        long del_true = db.delete("text_table", "user=?", new String[]{New_edt});
                        if (del_true == 0) {
                            Toast.makeText(GodActivity.this, "failure", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (del_true == 1) {
                            Toast.makeText(GodActivity.this, "success", Toast.LENGTH_SHORT).show();
                            Log.i("已删除===============", New_edt);
                        }
                        db.close();
                    }
                });
                break;
            case R.id.button3:
                dialog = new AlertDialog.Builder(this).create();
                dialog.show();
                //弹出输入法
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                //允许取消
                dialog.setCancelable(true);
                Window window2 = dialog.getWindow();
                WindowManager.LayoutParams lp2 = dialog.getWindow().getAttributes();
                lp2.width = 700;
                lp2.height = 500;
                window2.setAttributes(lp2);
                View view_dialog2 = LayoutInflater.from(this).inflate(R.layout.change_layout2, null);
                window2.setContentView(view_dialog2);
                final EditText change_edt2 = view_dialog2.findViewById(R.id.change_edt2);
                final EditText change_edt3 = view_dialog2.findViewById(R.id.change_edt3);
                Button del_button2 = view_dialog2.findViewById(R.id.del_button2);
                del_button2.setOnClickListener(view1 -> {
                    String change_edt2_data = change_edt2.getText().toString();
                    String change_edt3_data = change_edt3.getText().toString();
                    if (change_edt2_data.isEmpty() | change_edt3_data.isEmpty()) {
                        Toast.makeText(GodActivity.this, "内容为空", Toast.LENGTH_SHORT).show();
                    } else {
                        cv.put("user", change_edt2_data);
                        cv.put("pwd", change_edt3_data);
                        db.update("text_table", cv, "user=?", new String[]{change_edt2_data});
                        Toast.makeText(GodActivity.this, "success", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.button4:
                Cursor cursor = db.query("text_table", new String[]{"user", "pwd"}
                        , null, null, null, null, null);
                StringBuilder text = new StringBuilder();
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String New_user = cursor.getString(cursor.getColumnIndex("user"));
                    @SuppressLint("Range") String New_pwd = cursor.getString(cursor.getColumnIndex("pwd"));
                    text.append("user: ").append(New_user).append("\n").append("pwd: ").append(New_pwd).append("\n").append("--------------------").append("\n");
                }
                find_data.setText(text.toString());
                cursor.close();
                break;

        }
    }
}