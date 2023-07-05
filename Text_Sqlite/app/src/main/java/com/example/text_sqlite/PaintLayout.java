package com.example.text_sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

public class PaintLayout extends AppCompatActivity {
ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_layout);
        initView();
        Bitmap bitmap = Bitmap.createBitmap(500,500,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.BLACK);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(50);
        float[] points = new float[]{200, 100};
        canvas.drawPoints(points,paint);
        image.setImageBitmap(bitmap);
    }

    private void initView() {
        image = findViewById(R.id.image);
    }
}