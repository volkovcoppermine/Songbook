package org.volkov.songbook_fts.util

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.util.Log

class MusicPlayer {
    private var player: MediaPlayer = MediaPlayer()

    fun setup(context: Context, path: String) {
        val afd: AssetFileDescriptor = context.assets.openFd(path)
        player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        afd.close()
        player.prepare()
        Log.d("PLAYER", "isPlaying: ${player.isPlaying}")
        Log.d("PLAYER", "duration: ${player.duration}")
    }

    fun togglePlayback() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.seekTo(0)
            player.start()
        }
    }

    fun release() {
        player.stop()
        player.release()
    }
}