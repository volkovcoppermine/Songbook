package org.volkov.songbook_fts.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.volkov.songbook_fts.data.Song
import org.volkov.songbook_fts.data.SongRepository

class SongViewModel(application: Application) : AndroidViewModel(application) {
    private val songRepository = SongRepository(application)
    private var _searchResults = MutableLiveData<List<Song>>()
    val searchResults: LiveData<List<Song>> get() = _searchResults

    fun performSearch(query: String, fts: Boolean) {
        viewModelScope.launch {
            _searchResults.value = withContext(Dispatchers.IO) {
                if (query.isEmpty()) songRepository.getAllSongs() else if (fts) songRepository.performLyricsSearch(
                    query
                ) else songRepository.performSearch(query)
            }
        }
    }
}