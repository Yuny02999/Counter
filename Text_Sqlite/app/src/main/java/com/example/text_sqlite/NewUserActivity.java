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
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.immersionbar.ImmersionBar;

import java.sql.Time;
import java.util.Timer;

public class NewUserActivity extends AppCompatActivity implements View.OnClickListener {
    Button add;
    ImageButton returnButton;
    EditText userEdit, passEdit, twoPassEdit;
    MyHelper myHelper;
    SQLiteDatabase db;
    Cursor cursor;
    Toast toast;
    TextView add1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        makeBackgroundAll();
        //设置底部引导线背景为透明
        ImmersionBar.with(this)
                .transparentBar()
                .init();
        initView();


    }

    private void mToast(Context context, String msg) {
        if (toast != null){
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(context,msg,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP,0 , 0);
        toast.setText(msg);
        toast.show();
    }

    private void initView() {
        add = findViewById(R.id.add);
        userEdit = findViewById(R.id.userEdit);
        passEdit = findViewById(R.id.passEdit);
        twoPassEdit = findViewById(R.id.repeatPassEdit);
        returnButton = findViewById(R.id.returnButton);
        add1 = findViewById(R.id.add1);

        add.setOnClickListener(this);
        returnButton.setOnClickListener(this);
        add1.setOnClickListener(this);
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
                add_helper();
                break;
            case R.id.returnButton:
                overridePendingTransition(R.anim.newuser_translate_out, 0);
                finish();
                break;
        }
    }

    //判断输入内容字符最少为6字符
    private void maxLength(String userNew, String onePassNew) {
        if (userEdit.getText().toString().length() >= 6) {
            if (passEdit.getText().toString().length() >= 6) {
                twoPassEquals(userNew, onePassNew);
            }
        } else {
            String msg = "账号或密码太短";
            mToast(this ,msg);
        }
    }

    private void twoPassEquals(String userNew, String onePassNew) {
        String passTwo = twoPassEdit.getText().toString().trim();
        if (onePassNew.equals(passTwo)) {
            queryUserinfo(userNew, onePassNew);
        } else {
            String msg = "二次密码不一致";
            mToast(this ,msg);
        }
    }

    private void add_helper() {
        String userNew = userEdit.getText().toString();
        String onePassNew = passEdit.getText().toString();
        String twoPassNew = twoPassEdit.getText().toString();
        //判断输入为空
        if (userNew.isEmpty() || onePassNew.isEmpty() || twoPassNew.isEmpty()) {
            String msg = "账号密码不能为空";
            mToast(this, msg);
        } else {
            maxLength(userNew,onePassNew);

        }
    }

    private void queryUserinfo(String userNew, String onePassNew) {
        myHelper = new MyHelper(getApplicationContext());
        db = myHelper.getReadableDatabase();
        //判断账号是否存在
        String sql = "select user from text_table where user=?";
        cursor = db.rawQuery(sql, new String[]{userNew});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String getNew = cursor.getString(cursor.getColumnIndex("user"));
            if (getNew.equals(userNew)) {
                String msg = "账号已注册,请登录";
                mToast(this, msg);
                Log.i("========oldUser========", getNew);
                return;
            }
            //=========在moveToFirst方法中无法插入或执行任何操作============
        }
        add_user(userNew, onePassNew);
        cursor.close();
    }

    private void add_user(String userNew, String onePassNew) {
        myHelper = new MyHelper(getApplicationContext());
        db = myHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("user", userNew);
        cv.put("pwd", onePassNew);
        long i = db.insert("text_table", null, cv);
        if (i > 0) {
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            finish();
            Log.i("=======NewUser========", userNew);
            Log.i("=======NewPass========", onePassNew);
        } else {
            Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        db.close();
    }
}