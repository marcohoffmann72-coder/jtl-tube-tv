package com.jtl.tubetv

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class QrLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_login)

        val btnBack = findViewById<Button>(R.id.btn_back_login)
        btnBack.requestFocus()

        btnBack.setOnClickListener {
            finish() // Zurück zum Haupt-Login-Menü
        }
    }
}
