package com.example.litelentanews.ui.listnews

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.litelentanews.R
import com.example.litelentanews.databinding.CardNewsBinding
import com.example.litelentanews.domain.model.LentaNewsItem
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListNewsAdapter(
        var list:List<LentaNewsItem>,
        bottomSheetBehavior: BottomSheetBehavior<*>,
        val query: (String,View,ActionMode.Callback)->Unit)
    : RecyclerView.Adapter<ListNewsViewHolder>() {

    private var actionMode:ActionMode? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListNewsViewHolder {
        val binding = CardNewsBinding
                .inflate(LayoutInflater.from(parent.context),parent,false)
        return ListNewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListNewsViewHolder, position: Int) {
        val item = list[position]
        holder.title.text = item.title
        Glide.with(holder.itemView).load(item.linkImage).into(holder.image)
        holder.itemView.setOnClickListener {
            query(item.link, it, actionModeCallback)
        }
    }

    val actionModeCallback = object:ActionMode.Callback{
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            val inflater: MenuInflater = mode.menuInflater
            inflater.inflate(R.menu.lenta_news_context_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
           return false
        }

        override fun onDestroyActionMode(mode: ActionMode) {
           actionMode = null
           bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class ListNewsViewHolder(binding:CardNewsBinding):RecyclerView.ViewHolder(binding.root){
    val title = binding.newsTitle
    val image = binding.newsImageTitle
}