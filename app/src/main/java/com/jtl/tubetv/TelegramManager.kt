package com.jtl.tubetv

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

object TelegramManager {
    private const val TAG = "TelegramManager"
    private var client: Client? = null
    private val isInitialized = AtomicBoolean(false)
    
    // Deine Telegram API Credentials
    private const val API_ID = 39640144
    private const val API_HASH = "8254d4848a11db32f56b54d736cbe142"

    private var currentQrCodeLink: String? = null

    fun initClient(context: Context) {
        if (isInitialized.getAndSet(true)) return

        // TDLib native Bibliothek laden
        try {
            System.loadLibrary("tdjni")
        } catch (e: UnsatisfiedLinkError) {
            Log.e(TAG, "Fehler beim Laden der libtdjni.so: ${e.message}")
            return
        }

        val filesDir = File(context.filesDir, "tdlib")
        if (!filesDir.exists()) {
            filesDir.mkdirs()
        }

        // TDLib Client starten
        client = Client.create(
            { update -> handleUpdate(update) },
            { throwable -> Log.e(TAG, "TDLib Error: ${throwable.message}") },
            { throwable -> Log.e(TAG, "TDLib Fatal Error: ${throwable.message}") }
        )
    }

    private fun handleUpdate(update: TdApi.Update) {
        when (update) {
            is TdApi.UpdateAuthorizationState -> {
                when (update.authorizationState) {
                    is TdApi.AuthorizationStateWaitTdlibParameters -> {
                        sendTdlibParameters()
                    }
                    is TdApi.AuthorizationStateWaitPhoneNumber -> {
                        // Wir wollen direkt den QR-Login erzwingen
                        requestQrLogin()
                    }
                    is TdApi.AuthorizationStateWaitOtherDeviceConfirmation -> {
                        val state = update.authorizationState as TdApi.AuthorizationStateWaitOtherDeviceConfirmation
                        currentQrCodeLink = state.link
                        Log.i(TAG, "QR-Link empfangen: $currentQrCodeLink")
                    }
                    is TdApi.AuthorizationStateReady -> {
                        Log.i(TAG, "Erfolgreich bei Telegram eingeloggt!")
                    }
                    else -> {
                        Log.d(TAG, "Anderer Auth-State: ${update.authorizationState}")
                    }
                }
            }
        }
    }

    private fun sendTdlibParameters() {
        val parameters = TdApi.SetTdlibParameters().apply {
            apiId = API_ID
            apiHash = API_HASH
            useFileDatabase = true
            useChatInfoDatabase = true
            useMessageDatabase = true
            useSecretChats = false
            systemLanguageCode = "de"
            deviceModel = "Android TV"
            systemVersion = "1.0"
            applicationVersion = "1.0"
            databaseDirectory = "tdlib"
            filesDirectory = "tdlib/files"
        }
        client?.send(parameters) { result ->
            if (result is TdApi.Error) {
                Log.e(TAG, "Fehler bei SetTdlibParameters: ${result.message}")
            } else {
                // Nach Parametern direkt QR-Login anfordern
                requestQrLogin()
            }
        }
    }

    private fun requestQrLogin() {
        client?.send(TdApi.RequestQrCodeAuthentication(emptyArray())) { result ->
            if (result is TdApi.Error) {
                Log.e(TAG, "Fehler beim Anfordern des QR-Logins: ${result.message}")
            }
        }
    }

    fun requestQrCodeBitmap(width: Int, height: Int, callback: (Bitmap?) -> Unit) {
        // Falls ein echter Link da ist, generieren wir ihn, sonst vorerst einen Fallback/Wartelink
        val linkToEncode = currentQrCodeLink ?: "https://t.me/login/jtl_tv_waiting"
        
        try {
            val qrCodeWriter = QRCodeWriter()
            val bitMatrix = qrCodeWriter.encode(linkToEncode, BarcodeFormat.QR_CODE, width, height)
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            callback(bmp)
        } catch (e: Exception) {
            Log.e(TAG, "QR-Code Generierung fehlgeschlagen", e)
            callback(null)
        }
    }
}
