package org.volkov.songbook_fts.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import org.volkov.songbook_fts.R
import org.volkov.songbook_fts.databinding.ActivitySongBinding
import org.volkov.songbook_fts.data.NO_RESULTS
import org.volkov.songbook_fts.util.MusicPlayer
import org.volkov.songbook_fts.util.fileFromAsset
import org.volkov.songbook_fts.viewmodel.DetailViewModel
import org.volkov.songbook_fts.viewmodel.ViewModelFactory

class SongActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySongBinding
    private lateinit var detailViewModel: DetailViewModel
    private var player: MusicPlayer = MusicPlayer()
    private var canPlay: Boolean = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        title = getString(R.string.app_name)

        val num: String = intent.extras?.getString("NUM").toString()
        detailViewModel = ViewModelProvider(this, ViewModelFactory(player))[DetailViewModel::class.java]
        canPlay = detailViewModel.prepare(this, num)

        if (num == NO_RESULTS)
            binding.pdfView.initWithFile(fileFromAsset(this, "Cat.pdf"))
        else
            binding.pdfView.initWithFile(fileFromAsset(this, "scores/$num.pdf"))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_song, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_play -> {
                if (canPlay) player.togglePlayback()
                else Toast.makeText(this, R.string.midi_not_found, Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_about -> {
                val intent = Intent(this, CreditsActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return true
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }
}