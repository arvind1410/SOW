package com.silicontechnologies.plantfix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.silicontechnologies.plantix.R;
import com.silicontechnologies.plantix.view.activity.HomeActivity;

public class Week2 extends AppCompatActivity {

    public Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week2);

        button = (Button) findViewById(R.id.check);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Week2.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
