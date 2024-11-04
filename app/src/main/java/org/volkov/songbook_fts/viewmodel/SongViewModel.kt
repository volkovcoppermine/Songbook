package org.volkov.songbook_fts.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.volkov.songbook_fts.db.Song
import org.volkov.songbook_fts.db.SongRepository

class SongViewModel(application: Application) : AndroidViewModel(application) {
    private val searchQuery: MutableLiveData<String> = MutableLiveData()
    private val songRepository = SongRepository(application)
    var searchResults: LiveData<List<Song>> =
        searchQuery.switchMap { query: String -> songRepository.performSearch(query) }

    fun performSearch(query: String, fts: Boolean) {
        viewModelScope.launch {
            searchResults = withContext(Dispatchers.IO) {
                if (query.isEmpty()) songRepository.getAllSongs() else if (fts) songRepository.performLyricsSearch(
                    query
                ) else songRepository.performSearch(query)
            }
        }
    }
}