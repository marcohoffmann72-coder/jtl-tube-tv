package com.example.jtltube

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

        // Fokus direkt auf den QR-Code-Button für die Fernbedienung setzen
        btnQr.requestFocus()

        btnQr.setOnClickListener {
            Toast.makeText(this, "QR-Code-Login wird gestartet...", Toast.LENGTH_SHORT).show()
        }

        btnPhone.setOnClickListener {
            Toast.makeText(this, "Telefonnummer-Login wird gestartet...", Toast.LENGTH_SHORT).show()
        }
    }
}
