package com.example.text_sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
public class MyHelper extends SQLiteOpenHelper {
    private static final int version = 1;
    private static final String table = "SQLite.db";
    public MyHelper(@Nullable Context context) {
        super(context, table, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "Create table text_table(id Integer primary key autoincrement, user varchar(20), pwd varchar(20))";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
