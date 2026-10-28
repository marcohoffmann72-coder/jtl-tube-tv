package com.example.jtltube // Passe das Package an deine Struktur an

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

        // Fokus beim Start direkt auf den empfohlenen QR-Code-Button legen
        btnQr.requestFocus()

        btnQr.setOnClickListener {
            // Später: TDLib QR-Code Anforderung
            Toast.makeText(this, "QR-Code-Login wird gestartet...", Toast.LENGTH_SHORT).show()
        }

        btnPhone.setOnClickListener {
            // Später: Telefonnummer Eingabemaske
            Toast.makeText(this, "Telefonnummer-Login wird gestartet...", Toast.LENGTH_SHORT).show()
        }
    }
}
