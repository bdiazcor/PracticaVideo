package com.bdc.practicavideo

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import com.bdc.practicavideo.controlvideo.ReproductorVideo
import com.bdc.practicavideo.ui.theme.PracticaVideoTheme
import com.bdc.practicavideo.ui.theme.screens.VideoPlayerScreen
import com.bdc.practicavideo.ui.theme.screens.VideoRecorderScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        //1. Preparamos el motor de reproducción de video
        val reproductorVideo = ReproductorVideo(
            this,
            uri = "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4".toUri()
        )

        //2. Montamos la UI
        setContent {
            PracticaVideoTheme {
                //Surface envuelve la pantalla para aplicar el fondo del tema
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) { }
                VideoPlayerScreen(reproductorVideo)
                //VideoRecorderScreen()
            }
        }
    }
}

