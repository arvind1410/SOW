package com.silicontechnologies.plantix.view.activity


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.silicontechnologies.plantfix.SowActivity
import com.silicontechnologies.plantix.R

import kotlinx.android.synthetic.main.activity_signup.*


class SignUpActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(
                true)
        scan_bar_code.setOnClickListener {
            startActivityForResult(Intent(this, ScanBarCodeActivity::class.java), 101)
        }
        email_sign_in_button.setOnClickListener { attemptLogin() }
    }

    private fun attemptLogin() {
        startActivity(Intent(this, SowActivity::class.java))

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
            return true;
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 100 && data != null) {
            var scanedData = data.getStringExtra("scan")
            scanned_date.setText(scanedData)

        }
    }


}
