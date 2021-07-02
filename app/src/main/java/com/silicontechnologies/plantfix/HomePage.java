package com.silicontechnologies.plantfix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.silicontechnologies.plantix.R;
import com.silicontechnologies.plantix.view.activity.HomeActivity;

public class HomePage extends AppCompatActivity implements View.OnClickListener{

    private CardView bankCard, wifiCard, helpCard, ideaCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        bankCard = (CardView) findViewById(R.id.bank_card);
        wifiCard = (CardView) findViewById(R.id.wifi_card);
        helpCard = (CardView) findViewById(R.id.help_card);
        ideaCard = (CardView) findViewById(R.id.idea_card);

        //Adding Clicklistner to cards
        bankCard.setOnClickListener(this);
        wifiCard.setOnClickListener(this);
        helpCard.setOnClickListener(this);
        ideaCard.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.bank_card:
                i = new Intent(this, SowActivity.class);
                startActivity(i);
                break;
            case R.id.help_card:
                i = new Intent(this, HomeActivity.class);
                startActivity(i);
                break;
            case R.id.wifi_card:
                Intent intent = getPackageManager().getLaunchIntentForPackage("cz.martykan.forecastie");
                if (intent != null){
                    startActivity(intent);
                }
                break;
            case R.id.idea_card:
                Intent intent1 = getPackageManager().getLaunchIntentForPackage("com.example.vmac.chatbot");
                if (intent1 != null){
                    startActivity(intent1);
                }
                break;
            default:
                break;
        }
    }
}
