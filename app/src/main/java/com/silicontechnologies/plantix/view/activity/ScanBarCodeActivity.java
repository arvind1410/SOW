package com.silicontechnologies.plantix.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.silicontechnologies.plantix.R;
import com.silicontechnologies.plantix.base.BaseActivity;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScanBarCodeActivity extends BaseActivity implements ZBarScannerView.ResultHandler {


    private ZBarScannerView zBarScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_asset);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        zBarScannerView = new ZBarScannerView(this);
        zBarScannerView.setSoundEffectsEnabled(true);
        zBarScannerView.setFlash(false);
        ((FrameLayout) findViewById(R.id.fl_scan_asset)).addView(zBarScannerView);
    }


    @Override
    public void onResume() {
        super.onResume();
        zBarScannerView.setResultHandler(this);
        zBarScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        zBarScannerView.stopCamera();
    }


    @Override
    public void handleResult(Result result) {
        if (result.getContents() != null) {
            zBarScannerView.stopCamera();
            Intent intent = new Intent();
            intent.putExtra("scan", result.getContents());
            setResult(100, intent);
            finish();
        } else {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }
    }

    public void resetScan() {
        zBarScannerView.resumeCameraPreview(this);
        zBarScannerView.startCamera();
    }


}
