package com.jtl.tubetv

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class MainActivity : FragmentActivity() {

    private var player: ExoPlayer? = null
    private lateinit var playerView: PlayerView
    private lateinit var titleTextView: TextView

    // Deine Telegram API Zugangsdaten
    private val apiId: Int = 39640144
    private val apiHash: String = "8254d4848a11db32f56b54d736cbe142"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Einfaches Layout für den Start erzeugen
        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setBackgroundColor(android.graphics.Color.BLACK)
            setPadding(32, 32, 32, 32)
        }

        titleTextView = TextView(this).apply {
            text = "JTLTube - Telegram TV (API verbunden)"
            setTextColor(android.graphics.Color.WHITE)
            textSize = 22f
            setPadding(0, 0, 0, 24)
        }
        layout.addView(titleTextView)

        playerView = PlayerView(this).apply {
            layoutParams = android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT
            )
        }
        layout.addView(playerView)

        setContentView(layout)
        initPlayer()
    }

    private fun initPlayer() {
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            playerView.player = exoPlayer
            
            // Platzhalter-Stream (später die Telegram-Videodateien)
            val sampleUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
            val mediaItem = MediaItem.fromUri(sampleUrl)
            
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }
}
