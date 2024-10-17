package org.volkov.songbook_fts

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButton.OnCheckedChangeListener

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    private lateinit var songViewModel: SongViewModel
    private val adapter: SongAdapter = SongAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        songViewModel = ViewModelProvider(this)[org.volkov.songbook_fts.SongViewModel::class.java]

        val searchResultsView = findViewById<RecyclerView>(R.id.search_results_view)
        val searchBarView = findViewById<EditText>(R.id.search_bar_view)
        val checkBoxFTSView = findViewById<CheckBox>(R.id.checkBoxFTS)

        searchBarView.addTextChangedListener(object : TextWatcher {
            var isFTS: Boolean = false;

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                isFTS = checkBoxFTSView.isChecked
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d(TAG, "onTextChanged isFTS: $isFTS")
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

        val result: List<Song>? = songViewModel.searchResults.value
        if (result.isNullOrEmpty()) performSearch("", false)
    }

    private fun performSearch(query: String, fts: Boolean): Unit {
        songViewModel.performSearch(query, fts)
        songViewModel.searchResults.observe(
            this
        ) { value -> adapter.songs = value as ArrayList<Song> }
    }
}