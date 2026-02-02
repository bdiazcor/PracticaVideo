package com.bdc.practicavideo.controlvideo

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

/***
 * Motor de reproducción de video
 */

class ReproductorVideo (
    context: Context,
    uri: Uri
) {
    //Motor real de reproducción
    val player = ExoPlayer.Builder(context).build().apply {
        setMediaItem(MediaItem.fromUri(uri))
        prepare()
    }

    fun play() = player.play()
    fun pause() = player.pause()
    fun stop() = player.stop()
    fun release() = player.release()
}





