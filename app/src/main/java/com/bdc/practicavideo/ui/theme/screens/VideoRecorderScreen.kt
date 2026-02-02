package com.bdc.practicavideo.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.bdc.practicavideo.controlvideo.rememberCapturaVideo
import com.bdc.practicavideo.ui.theme.components.PermisoCamaraMicroUI
import java.io.File

@Composable
fun VideoRecorderScreen() {
    val context = LocalContext.current
    // Creamos un estado local para saber si mostrar el grabador o no en función de permisos concedidos
    var mostrarBotonGrabar by remember { mutableStateOf(false) }

    // 1. EL CONTRATO: Registramos la intención de capturar vídeo (controlvideo/CapturaVideo.kt).
    //    El código dentro de las llaves se ejecutará solo al volver de la cámara.
    val launcher = rememberCapturaVideo {
        // Aquí puedes decirles: "¡Ya ha vuelto el vídeo!"
        println("Vídeo capturado con éxito")
    }

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
                    // A. PREPARACIÓN: Creamos un archivo en la caché temporal de nuestra app.
                    val tempFile = File(context.cacheDir, "captura.mp4")

                    // B. SEGURIDAD: Usamos FileProvider para generar una dirección (Uri) segura.
                    // Esto permite que la cámara externa escriba en nuestra carpeta privada.
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider", // Debe coincidir con el Manifest
                        tempFile
                    )

                    // C. EJECUCIÓN: Lanzamos la cámara enviándole la dirección donde guardar.
                    launcher.launch(uri)
                }

            ) {
                Text("Abrir Cámara y Grabar")
            }
        } else{
            // Opcional: un texto informativo
            Text("Acepta los permisos para poder grabar", style = MaterialTheme.typography.bodySmall)
        }
    }
}