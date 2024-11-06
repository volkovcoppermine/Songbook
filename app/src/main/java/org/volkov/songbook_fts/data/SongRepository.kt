package org.volkov.songbook_fts.data

import android.app.Application

class SongRepository (application: Application) {
    private val songDao = SongDatabase.getInstance(application).songDao()
    fun getAllSongs(): List<Song> = songDao.getAllSongs()
    fun performSearch(query: String):List<Song> = songDao.performSearch(query)
    fun performLyricsSearch(query: String):List<Song> = songDao.performLyricsSearch(query)
}