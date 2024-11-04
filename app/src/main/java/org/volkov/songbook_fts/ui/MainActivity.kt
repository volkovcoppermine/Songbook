package org.volkov.songbook_fts.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.volkov.songbook_fts.R
import org.volkov.songbook_fts.db.Song
import org.volkov.songbook_fts.viewmodel.SongViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var songViewModel: SongViewModel
    private val adapter: SongAdapter = SongAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        title = getString(R.string.app_name)

        songViewModel = ViewModelProvider(this)[SongViewModel::class.java]

        setupSearch()
    }

    private fun setupSearch() {
        val searchResultsView = findViewById<RecyclerView>(R.id.search_results_view)
        val searchBarView = findViewById<EditText>(R.id.search_bar_view)
        val checkBoxFTSView = findViewById<CheckBox>(R.id.checkBoxFTS)

        searchBarView.addTextChangedListener(object : TextWatcher {
            var isFTS: Boolean = false

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                isFTS = checkBoxFTSView.isChecked
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                performSearch(p0.toString(), isFTS)
            }

            override fun afterTextChanged(p0: Editable?) {
                return
            }
        })

        checkBoxFTSView.setOnCheckedChangeListener { _, isChecked ->
            performSearch(
                searchBarView.text.toString(),
                isChecked
            )
        }

        searchResultsView.layoutManager = LinearLayoutManager(this)
        searchResultsView.setHasFixedSize(true)
        searchResultsView.adapter = adapter

        //val result: List<Song>? = songViewModel.searchResults.value
        performSearch("", false)
    }

    private fun performSearch(query: String, fts: Boolean) {
        songViewModel.performSearch(query, fts)
        songViewModel.searchResults.observe(
            this
        ) { value -> adapter.songs = value as ArrayList<Song> }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_about -> {
                val intent = Intent(this, CreditsActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return true
    }
}