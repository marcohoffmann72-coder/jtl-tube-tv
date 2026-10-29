package com.jtl.tubetv

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Telegram Client beim App-Start initialisieren
        TelegramManager.initClient(applicationContext)

        // 2. Das QR-Login Fragment auf dem Bildschirm anzeigen
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, QrLoginFragment())
                .commit()
        }
    }
}
