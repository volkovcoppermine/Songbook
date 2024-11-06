package org.volkov.songbook_fts.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Song::class], version = 1)
abstract class SongDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao

    companion object {
        private var instance: SongDatabase? = null

        fun getInstance(context: Context): SongDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context): SongDatabase {
            return Room.databaseBuilder(context, SongDatabase::class.java, "songs.db")
                .createFromAsset("songs.db")
                .build()
        }
    }
}