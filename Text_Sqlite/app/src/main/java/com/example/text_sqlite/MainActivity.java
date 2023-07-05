package com.example.text_sqlite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.gyf.immersionbar.ImmersionBar;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button add, open;
    EditText user, password;
    TextView ForgetPass, NewUserAdd, runText;
    CheckBox agreement;
    Toast mToast;
    MyHelper myHelper;
    SQLiteDatabase db;
    Cursor cur;
    ProgressBar progressBar;
    Sprite WanderingCubes;

    boolean ok = false;
    LoadingDialog.Speed speed = LoadingDialog.Speed.SPEED_TWO;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeBackgroundAll();
        BottomLine();
        initView();

        WanderingCubes = new WanderingCubes();
        progressBar.setIndeterminateDrawable(WanderingCubes);
        progressBar.setVisibility(View.INVISIBLE);

//        loadingDialog = new LoadingDialog(this)
//                .setLoadingText("加载中...")
//                .setSuccessText("成功！")
//                .setInterceptBack(true)
//                .setRepeatCount(0)
//                .closeSuccessAnim()
//                .setLoadSpeed(speed);
//        loadingDialog.show();
//        if (ok){
//            loadingDialog.loadSuccess();
//        }else {
//            loadingDialog.loadFailed();
//        }


        //跑马灯与EditText共存
        runText.setSelected(true);
        //请求焦点
        runText.requestFocus();
        add.setEnabled(false);
        //服务条款勾选框监听事件
        agreement.setOnCheckedChangeListener((compoundButton, b) -> {
            if (agreement.isChecked()) {
                add.setEnabled(true);
            } else {
                add.setEnabled(false);
            }
        });
    }



    //用户返回时执行
    public void onBackPressed() {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setIcon(R.drawable.addicon)
                .setMessage("是否退出应用？")
                .setTitle("提示框")
                .setPositiveButton("确定", (dialog1, i) -> {
                    dialog1.dismiss();
                    MainActivity.this.finish();
                }).setNegativeButton("取消", (dialog2, i) -> dialog2.dismiss());
        dialog = builder.create();
        dialog.show();
    }

    //设置底部引导线背景为透明
    private void BottomLine() {
        ImmersionBar.with(this)
                .transparentBar()
                .init();
    }


    private void initView() {
        progressBar = findViewById(R.id.progressBar);
        runText = findViewById(R.id.runText);
        add = findViewById(R.id.add);
        user = findViewById(R.id.user);
        password = findViewById(R.id.password);
        ForgetPass = findViewById(R.id.ForgetPass);
        NewUserAdd = findViewById(R.id.NewUserAdd);
        open = findViewById(R.id.open);
        agreement = findViewById(R.id.agreement);
        progressBar = findViewById(R.id.progressBar);

        runText.setOnClickListener(this);
        add.setOnClickListener(this);
        open.setOnClickListener(this);
        ForgetPass.setOnClickListener(this);
        NewUserAdd.setOnClickListener(this);
        agreement.setOnClickListener(this);


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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                closeInputMethod();
                clickable();
                break;
            case R.id.open:
                Intent intent = new Intent(MainActivity.this, GodActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.god_translate_in, R.anim.god_translate_out);
                break;
            case R.id.NewUserAdd:
                Intent intent1 = new Intent(MainActivity.this, NewUserActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.newuser_translate_in, R.anim.newuser_translate_out);
                break;
            case R.id.ForgetPass:
                Intent intent2 = new Intent(MainActivity.this, ForgetActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.forget_translate_in, R.anim.forget_translate_out);
                break;
        }
    }

    //关闭输入法
    private void closeInputMethod() {
        InputMethodManager imm = (InputMethodManager) add.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(add.getApplicationWindowToken(), 0);
        }
    }

    //勾选服务协议
    private void clickable() {
        if (add.isEnabled()) {
            add_helper();
        } else {
            String msg = "请阅读勾选服务协议";
            mToast(this, msg);
            Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.checkbox_translate);
            agreement.startAnimation(animation);
            vibrator();
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

    //震动任务
    private void vibrator() {
        Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{0, 60}, -1);
    }

    //判断账号密码不为空
    private void add_helper() {
        String userNew = user.getText().toString();
        String passNew = password.getText().toString();
        if (userNew.isEmpty() || passNew.isEmpty()) {
            String msg = "账号密码不能为空";
            mToast(this, msg);
        } else {
            queryUserinfo(userNew, passNew);
        }
    }

    //若账号密码都正确，执行数据插入数据库并跳转页面---若账号密码错误，执行Toast并返回
    private void queryUserinfo(String userNew, String passNew) {
        progressBar.setVisibility(View.VISIBLE);
        myHelper = new MyHelper(getApplicationContext());
        db = myHelper.getReadableDatabase();
        String sql = "select user,pwd from text_table where user=? and pwd=?";
        cur = db.rawQuery(sql, new String[]{userNew, passNew});

        if (cur.moveToNext()) {
            @SuppressLint("Range") String user1 = cur.getString(cur.getColumnIndex("user"));
            @SuppressLint("Range") String pwd1 = cur.getString(cur.getColumnIndex("pwd"));
            if (userNew.equals(user1) && passNew.equals(pwd1)) {
                String msg = "欢迎用户" + user1;
                Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.setText(msg);
                toast.show();
                Intent intent = new Intent(MainActivity.this, ShowActivity.class);
                startActivity(intent);
                Log.i("==================", "success");
                password.setText("");
                agreement.setChecked(false);
            }
        } else {
            checkUser(userNew);
        }
        cur.close();
        db.close();
    }

    private void checkUser(String userNew) {
        myHelper = new MyHelper(getApplicationContext());
        db = myHelper.getReadableDatabase();
        String sql = "select user from text_table where user=?";
        cur = db.rawQuery(sql, new String[]{userNew});
        if (cur.moveToNext()) {
            @SuppressLint("Range") String data = cur.getString(cur.getColumnIndex("user"));
            if (data.length() != 0) {
                String msg = "账号或密码错误";
                mToast(this, msg);
                Log.i("==================", "false");
            }
        } else {
            String msg = "账号不存在";
            mToast(this, msg);
            Log.i("==================", "false");
        }
        db.close();
        cur.close();
    }
}