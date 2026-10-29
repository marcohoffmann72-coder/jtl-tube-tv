package com.jtl.tubetv

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class QrLoginFragment : Fragment() {

    private lateinit var ivQrCode: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_qr_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivQrCode = view.findViewById(R.id.ivQrCode)

        // QR-Code anzeigen, sobald er bereit ist
        TelegramManager.onQrCodeReady = { bitmap: Bitmap? ->
            bitmap?.let { bmp ->
                activity?.runOnUiThread {
                    ivQrCode.setImageBitmap(bmp)
                }
            }
        }

        // Automatisch zum Hauptbildschirm wechseln, sobald der Login erfolgreich war
        TelegramManager.onLoginSuccess = {
            activity?.runOnUiThread {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, MainFragment())
                    .commit()
            }
        }

        // Direkt initial versuchen zu laden
        TelegramManager.requestQrCodeBitmap(300, 300) { bitmap: Bitmap? ->
            bitmap?.let { bmp ->
                activity?.runOnUiThread {
                    ivQrCode.setImageBitmap(bmp)
                }
            }
        }
    }
}
