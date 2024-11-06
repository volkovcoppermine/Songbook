package org.volkov.songbook_fts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.volkov.songbook_fts.util.MusicPlayer

class ViewModelFactory(_player: MusicPlayer) : ViewModelProvider.NewInstanceFactory() {
    private val player: MusicPlayer = _player

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == DetailViewModel::class.java) {
            return DetailViewModel(player) as T
        }
        return super.create(modelClass)
    }
}