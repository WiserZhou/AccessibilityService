package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button mBtnClick;
    private Button mBtnClick2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setThings();
    }

    public void setThings() {
        mBtnClick = findViewById(R.id.button);
        mBtnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                AutoClickUtil.autoClick(getApplicationContext(), 0.5F, 0.5F);
            }
        });
        mBtnClick2 = findViewById(R.id.button2);
        mBtnClick2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                AppLauncher.openApp(getApplicationContext(),"QQ");
            }
        });
    }
}
