package com.silicontechnologies.plantfix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.silicontechnologies.plantix.R;

public class WeekActivity extends AppCompatActivity {

    public Button week1, week2, week3, week4, week5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        week1 = (Button) findViewById(R.id.week1);
        week2 = (Button) findViewById(R.id.week2);
        week3 = (Button) findViewById(R.id.week3);
        week4 = (Button) findViewById(R.id.week4);
        week5 = (Button) findViewById(R.id.week5);
        week1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeekActivity.this, Week1.class);
                startActivity(intent);
            }
        });

        week2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeekActivity.this, Week2.class);
                startActivity(intent);
            }
        });

        week3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeekActivity.this, Week3.class);
                startActivity(intent);
            }
        });

        week4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeekActivity.this, Week4.class);
                startActivity(intent);
            }
        });
        week5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeekActivity.this, Week5.class);
                startActivity(intent);
            }
        });
    }
}
