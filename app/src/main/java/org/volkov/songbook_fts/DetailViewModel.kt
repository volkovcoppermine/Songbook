package org.volkov.songbook_fts

import android.app.Application
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private var canPlay: Boolean = false
    private var player: MediaPlayer? = null

    fun prepare(num: String, context: Context) {
        canPlay = context.resources.assets.list("midi")?.contains("$num.mid") ?: false
        if (canPlay) {
            player = MediaPlayer()
            val afd: AssetFileDescriptor = context.assets.openFd("midi/$num.mid")
            player?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            afd.close()
            player?.prepare()
        }
    }

    fun togglePlayback(context: Context) {
        if (player != null) {
            if (!(player?.isPlaying)!!) {
                player?.start()
            } else {
                player?.stop()
                player?.prepareAsync()
            }
        } else Toast.makeText(context, R.string.midi_not_found, Toast.LENGTH_SHORT).show()
    }

    fun release() {
        player?.stop()
        player?.release()
        player = null
    }
}