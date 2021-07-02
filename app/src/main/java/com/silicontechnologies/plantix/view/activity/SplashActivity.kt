package com.silicontechnologies.plantix.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.silicontechnologies.plantix.R
import com.silicontechnologies.plantix.base.BaseActivity

class SplashActivity : BaseActivity() {

    private var handler: Handler? = null
    private val TIME_DELAY: Long = 3000

    private val REQUEST_PERMISSION_CODE = 2
    private val mPermission = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

    val runnable: Runnable = Runnable {

        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            requestPermission()
        } else {
            navigateToHome()
        }
    }

    fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var allPermissionGranted = true
            for (permission in mPermission) {
                if (ActivityCompat.checkSelfPermission(this,
                                permission) != PackageManager.PERMISSION_GRANTED) {
                    allPermissionGranted = false
                }
            }
            if (!allPermissionGranted) {
                ActivityCompat.requestPermissions(this, mPermission, REQUEST_PERMISSION_CODE)
            } else {
                navigateToHome()
            }
        } else {
            navigateToHome()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        handler = Handler()
        handler!!.postDelayed(runnable, TIME_DELAY)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            var allPermissionGranted = true
            if (grantResults.size == permissions.size) {
                for (i in permissions.indices) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        allPermissionGranted = false
                        break
                    }
                }
            } else {
                allPermissionGranted = false
            }
            if (allPermissionGranted) {
                navigateToHome()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    fun navigateToHome() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

}