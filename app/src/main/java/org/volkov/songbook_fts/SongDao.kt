package org.volkov.songbook_fts

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface SongDao {
    @Query("select `rowid`, * from songs")
    fun getAllSongs(): LiveData<List<Song>>

    @Query("select rowid, num, title, snippet(songs) as lyrics from Songs where lyrics match :query || '*'")
    fun performLyricsSearch(query: String): LiveData<List<Song>>

    @Query("select rowid, num, title from songs where num match :query || '*' or title match :query || '*'")
    fun performSearch(query: String): LiveData<List<Song>>
}