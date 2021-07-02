package com.silicontechnologies.plantfix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.silicontechnologies.plantix.R;

public class SowActivity extends AppCompatActivity {

    public ImageView button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sow);


        button = (ImageView) findViewById(R.id.cotton_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SowActivity.this, CottonActivity.class);
                startActivity(intent);
            }
        });
    }
}
