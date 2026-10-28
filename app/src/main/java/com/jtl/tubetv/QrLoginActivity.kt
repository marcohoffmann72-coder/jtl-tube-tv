package com.jtl.tubetv

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class QrLoginActivity : AppCompatActivity() {

    private lateinit var ivQrCode: ImageView
    private lateinit var tvStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_login)

        val btnRefresh = findViewById<Button>(R.id.btn_refresh_qr)
        val btnBack = findViewById<Button>(R.id.btn_back_login)
        ivQrCode = findViewById(R.id.iv_qr_code)
        tvStatus = findViewById(R.id.tv_status)

        // Fokus direkt auf den "Neu laden"-Button setzen
        btnRefresh.requestFocus()

        // Telegram-Client vorbereiten und QR-Code laden
        TelegramManager.initClient()
        loadQrCode()

        btnRefresh.setOnClickListener {
            loadQrCode()
            Toast.makeText(this, "Neuen QR-Code angefordert", Toast.LENGTH_SHORT).show()
        }

        btnBack.setOnClickListener {
            finish() // Zurück zum Login-Menü
        }
    }

    private fun loadQrCode() {
        tvStatus.text = "Lade QR-Code von Telegram..."
        
        // Generiert einen 300x300 QR-Code und zeigt ihn an
        TelegramManager.requestQrCodeBitmap(300, 300) { bitmap ->
            runOnUiThread {
                if (bitmap != null) {
                    ivQrCode.setImageBitmap(bitmap)
                    tvStatus.text = "Scanne den Code mit deiner Telegram-App"
                } else {
                    tvStatus.text = "Fehler beim Generieren des QR-Codes"
                }
            }
        }
    }
}
