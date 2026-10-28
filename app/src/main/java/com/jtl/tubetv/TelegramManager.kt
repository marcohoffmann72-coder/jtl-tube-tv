package com.jtl.tubetv

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

object TelegramManager {

    private const val TAG = "TelegramManager"
    private var isInitialized = false

    init {
        try {
            // Hier werden später die nativen TDLib C++ Bibliotheken geladen (.so Dateien)
            // System.loadLibrary("tdjni")
        } catch (e: UnsatisfiedLinkError) {
            Log.e(TAG, "Native TDLib noch nicht geladen: ${e.message}")
        }
    }

    fun initClient() {
        if (isInitialized) return
        Log.d(TAG, "TDLib Client wird initialisiert...")
        // TODO: TDLib Parameter (api_id, api_hash, files_dir) setzen
        isInitialized = true
    }

    // Fordert den QR-Login-Link an und übergibt ihn als generierten QR-Code Bitmap zurück
    fun requestQrCodeBitmap(width: Int, height: Int, onResult: (Bitmap?) -> Unit) {
        Log.d(TAG, "Fordere Telegram QR-Login an...")
        
        // Später kommt hier der echte Telegram-Auth-Token aus der TDLib hin.
        // Für den Anfang nutzen wir einen Test-Link, der in einen echten QR-Code umgewandelt wird:
        val authUrl = "https://t.me/login/jtl_tv_demo_token"
        
        val bitmap = generateQRCode(authUrl, width, height)
        onResult(bitmap)
    }

    // Hilfsfunktion: Wandelt einen String in einen QR-Code Bitmap um
    private fun generateQRCode(text: String, width: Int, height: Int): Bitmap? {
        return try {
            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height)
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            bmp
        } catch (e: Exception) {
            Log.e(TAG, "Fehler beim Generieren des QR-Codes", e)
            null
        }
    }

    fun logout() {
        Log.d(TAG, "Logout aus Telegram...")
        isInitialized = false
    }
}
