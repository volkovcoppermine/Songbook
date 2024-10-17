package org.volkov.songbook_fts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap

class SongViewModel(application: Application) : AndroidViewModel(application) {
    private val searchQuery: MutableLiveData<String> = MutableLiveData()
    private val songRepository = SongRepository(application)
    var searchResults: LiveData<List<Song>> =
        searchQuery.switchMap { query: String -> songRepository.performSearch(query) }

    fun performSearch(query: String, fts: Boolean) {
        searchResults =
            if (query.isEmpty()) songRepository.getAllSongs() else if (fts) songRepository.performLyricsSearch(
                query
            ) else songRepository.performSearch(query)
    }
}