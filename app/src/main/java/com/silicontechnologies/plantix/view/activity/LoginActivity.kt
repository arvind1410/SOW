package com.silicontechnologies.plantix.view.activity


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.MenuItem
import com.silicontechnologies.plantix.HomePage
import com.silicontechnologies.plantix.R
import com.silicontechnologies.plantix.SowActivity

import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(
                true)

        email_sign_in_button.setOnClickListener {
            attemptLogin()
        }
        email_register_button.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun attemptLogin() {
        if (email.text.toString().isEmpty()) {
            Snackbar.make(email, "Enter EmailId", Snackbar.LENGTH_SHORT).show()
            return
        }

        if (password.text.toString().isEmpty()) {
            Snackbar.make(email, "Enter Password", Snackbar.LENGTH_SHORT).show()
            return
        }
        startActivity(Intent(this, com.silicontechnologies.plantfix.HomePage::class.java))

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
            return true;
        }
        return super.onOptionsItemSelected(item)

    }


}
