package com.bdc.practicavideo.ui.theme.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun PermisoCamaraMicroUI(
    onPermisosConcedidos: () -> Unit
) {
    val context = LocalContext.current

    // Estado que comprueba si AMBOS permisos están concedidos
    var permisosOK by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Usamos RequestMultiplePermissions para pedir Cámara y Audio a la vez
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { resultados ->
        // resultados es un Map<String, Boolean>
        val camaraOk = resultados[Manifest.permission.CAMERA] ?: false
        val microOk = resultados[Manifest.permission.RECORD_AUDIO] ?: false

        permisosOK = camaraOk && microOk
        if (permisosOK) onPermisosConcedidos()
    }

    Button(
        onClick = {
            if (!permisosOK) {
                launcher.launch(arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
                ))
            } else {
                onPermisosConcedidos()
            }
        }
    ) {
        Text(
            if (permisosOK) "Cámara y Micro Listos"
            else "Configurar Permisos de Grabación"
        )
    }
}