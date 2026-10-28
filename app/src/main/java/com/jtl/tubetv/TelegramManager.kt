package com.jtl.tubetv

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import java.io.File

object TelegramManager {

    private const val TAG = "TelegramManager"
    
    private const val API_ID: Int = 39640144
    private const val API_HASH: String = "8254d4848a11db32f56b54d736cbe142"

    private var isInitialized = false

    fun initClient(context: Context) {
        if (isInitialized) return
        
        val filesDir = File(context.filesDir, "tdlib")
        if (!filesDir.exists()) {
            filesDir.mkdirs()
        }
        
        Log.d(TAG, "TDLib Client wird initialisiert. Pfad: ${filesDir.absolutePath}")
        // Hier wird später der TDLib-Client mit den Parametern (api_id, api_hash, filesDir) gestartet
        
        isInitialized = true
    }

    fun requestQrCodeBitmap(width: Int, height: Int, onResult: (Bitmap?) -> Unit) {
        Log.d(TAG, "Fordere Telegram QR-Login an...")
        
        val authUrl = "https://t.me/login/jtl_tv_demo_token"
        val bitmap = generateQRCode(authUrl, width, height)
        onResult(bitmap)
    }

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
