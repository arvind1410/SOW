package com.silicontechnologies.plantfix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.silicontechnologies.plantix.R;

public class BuyNow extends AppCompatActivity {

    public Button button1, button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_now);

        button1 = (Button) findViewById(R.id.btn1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BuyNow.this,"The order has been Placed",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BuyNow.this, WeekActivity.class);
                startActivity(intent);
            }
        });

        button2 = (Button) findViewById(R.id.btn2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyNow.this, WeekActivity.class);
                startActivity(intent);
            }
        });
    }
}
