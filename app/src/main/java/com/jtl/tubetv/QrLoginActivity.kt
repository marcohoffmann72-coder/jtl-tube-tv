package com.jtl.tubetv

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class QrLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_login)

        val btnRefresh = findViewById<Button>(R.id.btn_refresh_qr)
        val btnBack = findViewById<Button>(R.id.btn_back_login)
        val tvStatus = findViewById<TextView>(R.id.tv_status)

        // Fokus direkt auf den "Neu laden"-Button setzen
        btnRefresh.requestFocus()

        btnRefresh.setOnClickListener {
            tvStatus.text = "QR-Code wird aktualisiert..."
            Toast.makeText(this, "Neuen QR-Code angefordert", Toast.LENGTH_SHORT).show()
        }

        btnBack.setOnClickListener {
            finish() // Zurück zum Login-Menü
        }
    }
}
