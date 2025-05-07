package com.example.rescueme

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class VideoPlayerActivity : AppCompatActivity() {
    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_video_player)

        videoView = findViewById(R.id.videoView)
        
        // Get video URL from intent
        val videoUrl = intent.getStringExtra("videoUrl")
        if (videoUrl != null) {
            setupVideoPlayer(videoUrl)
        } else {
            finish()
        }
    }

    private fun setupVideoPlayer(videoUrl: String) {
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(Uri.parse(videoUrl))
        videoView.start()
    }

    override fun onPause() {
        super.onPause()
        videoView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView.stopPlayback()
    }
} 