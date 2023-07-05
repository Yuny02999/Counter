package com.example.text_sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gyf.immersionbar.ImmersionBar;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton returnButton;
    Button add;
    EditText user, password, new_password;
    Toast mToast;
    MyHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        makeBackgroundAll();
        //设置底部引导线背景为透明
        ImmersionBar.with(this)
                .transparentBar()
                .init();
        initView();

        returnButton.setOnClickListener(this);
        add.setOnClickListener(this);

    }

    private void initView() {
        returnButton = findViewById(R.id.returnButton);
        add = findViewById(R.id.add);
        user = findViewById(R.id.user);
        password = findViewById(R.id.password);
        new_password = findViewById(R.id.new_password);
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
            case R.id.add:
                unEmpty();
                break;
            case R.id.returnButton:

                finish();
                break;
        }
    }

    private void mToast(Context context, String msg) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.TOP, 0, 0);
        mToast.setText(msg);
        mToast.show();
    }

    private void unEmpty() {
        String userTemp = user.getText().toString();
        String passTemp = password.getText().toString();
        String passTemp2 = new_password.getText().toString();
        if (userTemp.isEmpty() || passTemp.isEmpty() || passTemp2.isEmpty()) {
            String msg = "信息不能为空!";
            mToast(this, msg);
        } else {
            if (passTemp.equals(passTemp2)) {
                String msg = "前后密码相同!";
                mToast(this, msg);
            } else {
                findUser(userTemp, passTemp, passTemp2);
            }
        }
    }

    //查找账号是否存在
    private void findUser(String user, String pass, String newPass) {
        myHelper = new MyHelper(getApplicationContext());
        SQLiteDatabase database = myHelper.getWritableDatabase();
        String sql = "select user from text_table where user=?";
        Cursor cr = database.rawQuery(sql, new String[]{user});
        if (cr.moveToFirst()) {
            @SuppressLint("Range") String userTemp = cr.getString(cr.getColumnIndex("user"));
            if (userTemp.equals(user)) {
                Log.i("=================", userTemp);
                findPass(pass, newPass);
            }
        } else {
            String msg = "账号未注册";
            mToast(this, msg);
        }
        cr.close();
        database.close();
    }

    //查询密码
    private void findPass(String pass, String newPass) {
        myHelper = new MyHelper(getApplicationContext());
        SQLiteDatabase db = myHelper.getWritableDatabase();
        String sql = "select pwd from text_table where pwd=?";
        Cursor cr = db.rawQuery(sql, new String[]{pass});
        if (cr.moveToFirst()) {
            @SuppressLint("Range") String passTemp = cr.getString(cr.getColumnIndex("pwd"));
            if (passTemp.equals(pass)) {
                changePass(newPass, passTemp);
            }
        }
        cr.close();
        db.close();
    }

    //条件通过 修改密码
    private void changePass(String newPass, String passTemp) {
        String userTemp = user.getText().toString();
        myHelper = new MyHelper(getApplicationContext());
        SQLiteDatabase db = myHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("pwd", newPass);
        db.update("text_table", cv, "user=?", new String[]{userTemp});
        if (!passTemp.equals(newPass)) {
            Toast.makeText(this, "用户" + userTemp + "修改成功", Toast.LENGTH_SHORT).show();
            finish();
        }
        db.close();
    }
}