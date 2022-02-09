package com.example.camping.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.camping.databinding.RecentSearchItemBinding

class RecentViewAdapter: RecyclerView.Adapter<RecentViewAdapter.RecentViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(word: String, position: Int)
        fun onRemoveClick(position: Int)
    }

    private lateinit var listener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    var list = ArrayList<String>()

    inner class RecentViewHolder(val binding: RecentSearchItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(word: String){
            binding.txtWord.text = word
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder = RecentViewHolder(RecentSearchItemBinding.inflate(LayoutInflater.from(parent.context) ,parent,false))

    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        holder.bind(list[position])
        // 검색어 클릭
        holder.itemView.setOnClickListener {
            listener.onItemClick(list[position], position)
        }
        // 삭제 클릭
        holder.binding.btnRemove.setOnClickListener {
            listener.onRemoveClick(position)
        }
    }

    override fun getItemCount(): Int = list.size
}

