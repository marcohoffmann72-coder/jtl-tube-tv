package com.jtl.tubetv

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class QrLoginFragment : Fragment() {

    private lateinit var ivQrCode: ImageView
    private lateinit var tvStatus: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_qr_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivQrCode = view.findViewById(R.id.ivQrCode)
        tvStatus = view.findViewById(R.id.tvStatus)

        tvStatus.text = "Status: Warte auf Telegram-Verbindung..."

        // QR-Code anzeigen, sobald er bereit ist
        TelegramManager.onQrCodeReady = { bitmap: Bitmap? ->
            bitmap?.let { bmp ->
                activity?.runOnUiThread {
                    ivQrCode.setImageBitmap(bmp)
                    tvStatus.text = "Status: QR-Code bereit zum Scannen!"
                }
            } ?: run {
                activity?.runOnUiThread {
                    tvStatus.text = "Status: Fehler beim Generieren des QR-Codes"
                }
            }
        }

        // Zum Hauptbildschirm wechseln bei Erfolg
        TelegramManager.onLoginSuccess = {
            activity?.runOnUiThread {
                tvStatus.text = "Status: Erfolgreich angemeldet!"
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, MainFragment())
                    .commit()
            }
        }

        // Initialer Versuch
        TelegramManager.requestQrCodeBitmap(300, 300) { bitmap: Bitmap? ->
            bitmap?.let { bmp ->
                activity?.runOnUiThread {
                    ivQrCode.setImageBitmap(bmp)
                }
            }
        }
    }
}
