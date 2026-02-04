package com.bdc.practicavideo.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bdc.practicavideo.controlvideo.borrarVideo
import com.bdc.practicavideo.controlvideo.crearVideoEnMediaStore
import com.bdc.practicavideo.controlvideo.rememberCapturaVideo
import com.bdc.practicavideo.ui.components.PermisoCamaraMicroUI

@Composable
fun VideoRecorderConMediaStoreScreen() {
    val context = LocalContext.current
    // Creamos un estado local para saber si mostrar el grabador o no en función de permisos concedidos
    var mostrarBotonGrabar by remember { mutableStateOf(false) }

    // Guardamos la Uri del vídeo que estamos grabando ahora
    // (para usarla al volver del launcher).
    var videoUri by remember { mutableStateOf<Uri?>(null) }

    // 1. EL CONTRATO: Registramos la intención de capturar vídeo.
    //    El código dentro de las llaves se ejecutará solo al volver de la cámara a la app.
    val launcher = rememberCapturaVideo(

        onExito = {
            // Recuperamos la Uri que habíamos creado antes de lanzar la cámara
            val uri = videoUri ?: return@rememberCapturaVideo
            Toast.makeText(context, "✅ Vídeo guardado en Galería", Toast.LENGTH_SHORT).show()

        },
        onCancel = {
            // Recuperamos la Uri que habíamos creado antes de lanzar la cámara
            val uri = videoUri ?: return@rememberCapturaVideo
            //Borramos el registro en la Galeria
            borrarVideo(context, uri)
            Toast.makeText(context, "❌ Grabación cancelada (no se guarda)", Toast.LENGTH_SHORT)
                .show()
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        // 2. LÓGICA DE PERMISOS: Llamamos al componente que gestiona los permisos la cámara y el micro.
        // Cuando el usuario acepta, el callback nos permite activar el botón de grabado.
        PermisoCamaraMicroUI(
            onPermisosConcedidos = {
                // Cuando se conceden, activamos el siguiente paso
                mostrarBotonGrabar = true
            }
        )

        // 3. UI REACTIVA: El botón de grabación solo "existe" si el estado de los permisos lo permite.
        if (mostrarBotonGrabar) {
            Button(
                onClick = {
                    // A) Creamos la Uri en MediaStore (en vez de File + FileProvider)
                    val uri = crearVideoEnMediaStore(
                        context,
                        "video_${System.currentTimeMillis()}.mp4"
                    )
                    // Guardamos esa Uri para usarla al volver (success/cancel)
                    videoUri = uri

                    // B. EJECUCIÓN: Lanzamos la cámara enviándole la dirección donde guardar.
                    launcher.launch(uri)
                }

            ) {
                Text("Grabar y Guardar en Galería")
            }
        } else {
            // Opcional: un texto informativo
            Text(
                "Acepta los permisos para poder grabar",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}