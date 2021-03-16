package com.example.litelentanews.ui.listnews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.litelentanews.databinding.ListCategoryItemBinding
import com.example.litelentanews.domain.model.Category

class ListNewsCategoryAdapter(
    val query: (String) -> Unit
):RecyclerView.Adapter<ListNewsCategoryViewHolder>() {

    var list = listOf<Category>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListNewsCategoryViewHolder {
        val binding = ListCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListNewsCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListNewsCategoryViewHolder, position: Int) {
        val item = list[position]
        holder.categoryName.text = item.name
        holder.title.text = item.total.toString()

        holder.itemView.setOnClickListener {
            query(item.name)
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }

}

class ListNewsCategoryViewHolder(binding:ListCategoryItemBinding):RecyclerView.ViewHolder(binding.root){
    val categoryName = binding.category
    val title = binding.total

}