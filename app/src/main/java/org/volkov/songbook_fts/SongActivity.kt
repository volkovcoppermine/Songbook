package org.volkov.songbook_fts

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import org.volkov.songbook_fts.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySongBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.song)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        title = getString(R.string.app_name)

        val num: String = intent.extras?.getString("NUM").toString()
        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        detailViewModel.prepare(num, this)

        if (num != "Нет результатов")
            binding.pdfView.initWithFile(detailViewModel.fileFromAsset(this, "scores/$num.pdf"))
        else
            binding.pdfView.initWithFile(detailViewModel.fileFromAsset(this, "Cat.pdf"))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_song, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_play -> {
                detailViewModel.togglePlayback(this)
                return true
            }
        }
        return true
    }

    override fun onStop() {
        super.onStop()
        detailViewModel.release()
    }
}