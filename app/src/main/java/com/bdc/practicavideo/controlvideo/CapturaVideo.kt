package com.bdc.practicavideo.controlvideo

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun rememberCapturaVideo(onExito: () -> Unit): ActivityResultLauncher<Uri> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo(),
        onResult = {success ->
            if (success) onExito() //Video capturado correctamente
        }
    )
}