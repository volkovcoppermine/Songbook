package org.volkov.songbook_fts

import android.content.Intent
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongAdapter : RecyclerView.Adapter<SongAdapter.SongHolder>() {
    val TAG = "Search"
    var songs: ArrayList<Song> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class SongHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val textViewNum = itemView.findViewById<TextView>(R.id.text_view_num)
        val textViewTitle = itemView.findViewById<TextView>(R.id.text_view_title)
        val textViewSnippet = itemView.findViewById<TextView>(R.id.text_view_snippet)
        val context = itemView.context

        init { itemView.setOnClickListener(this) }

        override fun onClick(v: View) {
            val current: Song = songs[bindingAdapterPosition]
            Log.d(TAG, "onClick: $current")
            val intent: Intent = Intent(context, SongActivity::class.java)
            intent.putExtra("NUM", current.num)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.song_item, parent, false)
        return SongHolder(itemView)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        val current = songs[position]
        holder.textViewNum.text = current.num
        holder.textViewTitle.text = current.title
        if(!current.lyrics.isNullOrEmpty()) {
            holder.textViewSnippet.visibility = View.VISIBLE
            holder.textViewSnippet.text = Html.fromHtml(current.lyrics, Html.FROM_HTML_MODE_COMPACT)
        }
    }
}