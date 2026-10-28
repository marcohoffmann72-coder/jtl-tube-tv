package com.example.jtltube // Ersetze das hier durch dein echtes Package

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class MainActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun initializePlayer() {
        // ExoPlayer instanziieren
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            val playerView = findViewById<PlayerView>(R.id.player_view)
            playerView.player = exoPlayer

            // HIER TRÄGST DU DEINE STREAM-URL EIN:
            // (Aktuell ein stabiler Test-Stream, du kannst ihn gegen deinen Stream ersetzen)
            val streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
            
            val mediaItem = MediaItem.fromUri(streamUrl)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
        }
    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            exoPlayer.release()
        }
        player = null
    }
}
