package com.jtl.tubetv

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnQr = findViewById<Button>(R.id.btn_qr_login)
        val btnPhone = findViewById<Button>(R.id.btn_phone_login)

        btnQr.requestFocus()

        btnQr.setOnClickListener {
            // Öffnet die QR-Login-Ansicht
            val intent = Intent(this, QrLoginActivity::class.java)
            startActivity(intent)
        }

        btnPhone.setOnClickListener {
            Toast.makeText(this, "Telefonnummer-Login folgt als Nächstes...", Toast.LENGTH_SHORT).show()
        }
    }
}
