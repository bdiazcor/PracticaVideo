package com.bdc.practicavideo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView
import com.bdc.practicavideo.controlvideo.ReproductorVideo

data class VideoUiState(
    val isPlaying: Boolean = false
)

@Composable
fun VideoPlayerScreen(
    playerManager: ReproductorVideo
) {
    //ESTADO: Habilita la reactividad
    var uiState by remember { mutableStateOf(VideoUiState()) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //Aquí se muestra el video. Es el puente entre Compose y la View clásica de Android
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    player = playerManager.player
                    useController = false //Creamos nuestro propio control
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            //TODO 1: Botón Play
            Button(
                onClick = {
                    playerManager.play()
                    uiState = uiState.copy(isPlaying = true)
                },

                //TODO 2: Control estado: cuando se muestra el botón Play

                enabled = !uiState.isPlaying,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Play")
            }

            //TODO 3: Botón Pause
            Button(
                onClick = {
                    playerManager.pause()
                    uiState = uiState.copy(isPlaying = false)
                },
                //TODO 4: Control estado: cuando se muestra el botón Pause
                enabled = uiState.isPlaying,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Pause")
            }

        }

    }

    //TODO 5: Ciclo de vida - Liberar recursos

    //DisposableEffect: Es un efecto secundario que permite ejecutar lógica cuando un Composable
    // entra en la composición y, realiza una tarea de limpieza mediante el bloque onDispose cuando
    // el Composable es destruido o se reinicia.
    DisposableEffect(Unit) {//Unit: solo se ejecuta una vez
        onDispose { //Instrucciones que se ejecutan antes de que el composable se destruya
            playerManager.release()
        }
    }

}





