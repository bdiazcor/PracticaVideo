package com.bdc.practicavideo.controlvideo

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

//1) Helper de Compose: Crea y recuerda el launcher
// No pinta nada pero necesitan a Compose para "recordar" estados, objetos o
// gestionar ciclos de vida: remember {...}, rememberLauncherForActivityResult {...}, etc
@Composable
fun rememberCapturaVideo(
    onExito: () -> Unit,
    onCancel: () -> Unit,
): ActivityResultLauncher<Uri> {

    // Creamos (y "recordamos") el launcher UNA sola vez en Compose.
    // Importante: aquí NO le damos ninguna Uri todavía.
    // La Uri se enviará más tarde cuando hagamos launcher.launch(uri).
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo(),

        // Este callback se ejecuta cuando volvemos de la app de cámara.
        // success = true si se ha grabado; false si se canceló o falló.
        onResult = { success ->
            if (success) onExito() else onCancel()
        }
    )
}

//2) Crea la Uri destino de MediaStore
fun crearVideoEnMediaStore(context: Context, nombre: String): Uri {

    // Metadatos del archivo que se va a crear en la "biblioteca" del sistema (MediaStore)
    val values = ContentValues().apply {
        put(MediaStore.Video.Media.DISPLAY_NAME, nombre)
        put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")

        // API 29+ (Scoped Storage):
        // RELATIVE_PATH = carpeta donde aparecerá (Movies/GrabacionesUT3)
        put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/GrabacionesUT3")
    }

    // Insertamos el registro en MediaStore y recibimos la Uri (content://...)
    // Esa Uri será el "destino" donde la cámara grabará el vídeo.
    return context.contentResolver.insert(
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
        values
    ) ?: error("No se pudo crear el vídeo en MediaStore")
}

// 3) Borrar la entrada en la Galeria (si existe) si el usuario cancela
fun borrarVideo(context: Context, uri: Uri) {
    context.contentResolver.delete(uri, null, null)
}

