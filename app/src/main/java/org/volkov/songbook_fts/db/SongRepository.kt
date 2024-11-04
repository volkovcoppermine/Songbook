package org.volkov.songbook_fts.db

import android.app.Application
import androidx.lifecycle.LiveData

class SongRepository (application: Application) {
    private val songDao = SongDatabase.getInstance(application).songDao()
    fun getAllSongs(): LiveData<List<Song>> = songDao.getAllSongs()
    fun performSearch(query: String):LiveData<List<Song>> = songDao.performSearch(query)
    fun performLyricsSearch(query: String):LiveData<List<Song>> = songDao.performLyricsSearch(query)
}