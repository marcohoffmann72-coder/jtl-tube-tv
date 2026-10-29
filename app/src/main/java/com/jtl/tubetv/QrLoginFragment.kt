package com.jtl.tubetv

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

        // Auf QR-Code Updates vom TelegramManager lauschen
        TelegramManager.onQrCodeReady = { bitmap ->
            bitmap?.let { bmp ->
                activity?.runOnUiThread {
                    ivQrCode.setImageBitmap(bmp)
                }
            }
        }
    }
}
