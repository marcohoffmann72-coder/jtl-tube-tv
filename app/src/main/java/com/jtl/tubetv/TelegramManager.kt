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
    var onQrCodeReady: ((Bitmap?) -> Unit)? = null
    var onLoginSuccess: (() -> Unit)? = null
    var onError: ((String) -> Unit)? = null

    fun initClient(context: Context) {
        if (isInitialized.getAndSet(true)) return

        try {
            System.loadLibrary("tdjni")
        } catch (e: UnsatisfiedLinkError) {
            onError?.invoke("Fehler: libtdjni.so nicht geladen")
            return
        }

        val filesDir = File(context.filesDir, "tdlib")
        if (!filesDir.exists()) {
            filesDir.mkdirs()
        }

        client = Client.create(
            { tdObject -> 
                if (tdObject is TdApi.Update) {
                    handleUpdate(tdObject)
                }
            },
            { throwable -> onError?.invoke("TDLib Error: ${throwable.message}") },
            { throwable -> onError?.invoke("TDLib Fatal: ${throwable.message}") }
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
                        // Erst hier fordern wir den QR-Login sauber an
                        requestQrLogin()
                    }
                    is TdApi.AuthorizationStateWaitOtherDeviceConfirmation -> {
                        val state = update.authorizationState as TdApi.AuthorizationStateWaitOtherDeviceConfirmation
                        currentQrCodeLink = state.link
                        Log.i(TAG, "QR-Link empfangen: $currentQrCodeLink")
                        generateAndNotifyQrBitmap()
                    }
                    is TdApi.AuthorizationStateReady -> {
                        Log.i(TAG, "Erfolgreich bei Telegram eingeloggt!")
                        onLoginSuccess?.invoke()
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
                onError?.invoke("Param-Fehler: ${result.message}")
            }
        }
    }

    private fun requestQrLogin() {
        client?.send(TdApi.RequestQrCodeAuthentication(LongArray(0))) { result ->
            if (result is TdApi.Error) {
                onError?.invoke("QR-Anfrage-Fehler: ${result.message}")
            }
        }
    }

    private fun generateAndNotifyQrBitmap() {
        val linkToEncode = currentQrCodeLink ?: return
        requestQrCodeBitmap(300, 300, linkToEncode) { bmp ->
            onQrCodeReady?.invoke(bmp)
        }
    }

    fun requestQrCodeBitmap(width: Int, height: Int, customLink: String? = null, callback: (Bitmap?) -> Unit) {
        val linkToEncode = customLink ?: currentQrCodeLink
        
        if (linkToEncode == null) {
            callback(null)
            return
        }

        try {
            val qrCodeWriter = QRCodeWriter()
            val bitMatrix = qrCodeWriter.encode(linkToEncode, BarcodeFormat.QR_CODE, width, height)
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
                }
            }
            callback(bmp)
        } catch (e: Exception) {
            onError?.invoke("QR-Generierung fehlgeschlagen")
            callback(null)
        }
    }
}
