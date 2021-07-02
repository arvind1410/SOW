package com.silicontechnologies.plantfix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.silicontechnologies.plantix.R;

public class DateActivity extends AppCompatActivity {

    public Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        button = (Button) findViewById(R.id.btn_buy);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DateActivity.this, BuyNow.class);
                startActivity(intent);
            }
        });
    }
}
