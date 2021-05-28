package com.example.thumboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Keyboard thumb = (Keyboard) findViewById(R.id.keyboard_add);
//        findViewById(R.id.touchpad).setOnTouchListener(thumb.onTouch());

    }




}