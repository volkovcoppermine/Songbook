package org.volkov.songbook_fts

import android.app.Application
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

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

    fun fileFromAsset(context: Context, assetName: String): File {
        val outFile = File(context.cacheDir, "$assetName")
        if (assetName.contains("/")) {
            outFile.parentFile.mkdirs()
        }
        copy(context.assets.open(assetName), outFile)
        return outFile
    }

    @Throws(IOException::class)
    private fun copy(inputStream: InputStream?, output: File?) {
        var outputStream: OutputStream? = null
        try {
            outputStream = FileOutputStream(output)
            var read = 0
            val bytes = ByteArray(1024)
            while (inputStream!!.read(bytes).also { read = it } != -1) {
                outputStream.write(bytes, 0, read)
            }
        } finally {
            try {
                inputStream?.close()
            } finally {
                outputStream?.close()
            }
        }
    }
}