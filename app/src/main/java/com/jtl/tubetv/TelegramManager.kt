package com.jtl.tubetv

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

object TelegramManager {

    private const val TAG = "TelegramManager"
    
    // Deine offiziellen Telegram API-Zugangsdaten aus der Konfiguration[span_1](start_span)[span_1](end_span)
    private const val API_ID: Int = 39640144
    private const val API_HASH: String = "8254d4848a11db32f56b54d736cbe142"

    private var isInitialized = false

    fun initClient() {
        if (isInitialized) return
        Log.d(TAG, "TDLib Client wird mit API_ID: $API_ID initialisiert...")
        // Hier werden später die TDLib-Parametrisierungen mit API_ID und API_HASH übergeben
        isInitialized = true
    }

    // Fordert den QR-Login-Link an und übergibt ihn als generierten QR-Code Bitmap zurück
    fun requestQrCodeBitmap(width: Int, height: Int, onResult: (Bitmap?) -> Unit) {
        Log.d(TAG, "Fordere Telegram QR-Login an (API_ID: $API_ID)...")
        
        // Später wird hier der echte Authentifizierungs-Link von TDLib abgefangen.
        // Bis die native TDLib komplett eingebunden ist, nutzen wir den funktionierenden Generator:
        val authUrl = "https://t.me/login/jtl_tv_demo_token"
        
        val bitmap = generateQRCode(authUrl, width, height)
        onResult(bitmap)
    }

    // Hilfsfunktion: Wandelt den String in einen QR-Code Bitmap um
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
