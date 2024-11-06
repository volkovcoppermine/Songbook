package org.volkov.songbook_fts.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.volkov.songbook_fts.data.NO_RESULTS
import org.volkov.songbook_fts.util.MusicPlayer

class DetailViewModel(_player: MusicPlayer) : ViewModel() {
    private var player: MusicPlayer = _player

    fun prepare(context: Context, num: String): Boolean {
        val nothingToShow: Boolean = num == NO_RESULTS
        val canPlay: Boolean = if (nothingToShow) true
        else context.resources.assets.list("midi")?.contains("$num.mid") == true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (canPlay) {
                    val path = if (nothingToShow) "Purr.mp3" else "midi/$num.mid"
                    player.setup(context, path)
                }
            }
        }
        return canPlay
    }
}