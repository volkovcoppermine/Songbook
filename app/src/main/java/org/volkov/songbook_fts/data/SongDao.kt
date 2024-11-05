package org.volkov.songbook_fts.data

import androidx.room.Dao
import androidx.room.Query

const val NO_RESULTS = "Нет результатов"

@Dao
interface SongDao {
    @Query("select rowid, num, title from songs")
    fun getAllSongs(): List<Song>

    @Query("select rowid, num, title, snippet(songs) as lyrics from songs where lyrics match :query || '*' " +
            "union select 65536, '$NO_RESULTS', 'По Вашему запросу ничего не найдено', null from songs " +
            "where not exists (select rowid, * from songs where lyrics match :query || '*')")
    fun performLyricsSearch(query: String): List<Song>

    @Query("select rowid, num, title from songs where num match :query || '*' or title match :query || '*' " +
            "union select 65536, '$NO_RESULTS', 'По Вашему запросу ничего не найдено' from songs " +
            "where not exists (select rowid, num, title from songs where num match :query || '*' or title match :query || '*')")
    fun performSearch(query: String): List<Song>
}