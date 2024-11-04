package org.volkov.songbook_fts.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
@Fts4(tokenizer = "unicode61")
data class Song (
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    val rowId: Int,
    val num: String,
    val title: String,
    val lyrics: String?
)