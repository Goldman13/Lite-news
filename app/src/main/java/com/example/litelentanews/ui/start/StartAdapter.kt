package com.example.litelentanews.ui.start

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.litelentanews.databinding.StartGridNewsBinding

class StartAdapter(
        val list:List<String>,
        val click:(String)->Unit
):RecyclerView.Adapter<StartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StartViewHolder {
        val bind = StartGridNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StartViewHolder(bind)
    }

    override fun onBindViewHolder(holder: StartViewHolder, position: Int) {
        val item = list.get(position)
        holder.title.text = item

        holder.itemView.setOnClickListener {
            click(item)
        }

    }

    override fun getItemCount(): Int {
       return list.size
    }
}
class StartViewHolder(bind:StartGridNewsBinding):RecyclerView.ViewHolder(bind.root){
    val title = bind.titleNews
}