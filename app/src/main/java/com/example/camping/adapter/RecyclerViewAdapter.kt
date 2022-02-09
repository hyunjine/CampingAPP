package com.example.camping.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.camping.R
import com.example.camping.data.vo.Item
import com.example.camping.databinding.CampingListItemBinding
import com.example.camping.databinding.FavoriteListItemBinding
import com.example.camping.databinding.PetListItemBinding
import com.example.camping.databinding.RecentSearchItemBinding
import com.example.camping.util.data.LOG.TAG
import com.example.camping.util.data.RecyclerViewType

class RecyclerViewAdapter(
    private val context: Context,
    private val type: RecyclerViewType
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(item: Item)
    }

    private lateinit var listener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    var list = ArrayList<Item>()

    inner class ListViewHolder(val binding: CampingListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(currentData: Item) {
            if (type == RecyclerViewType.AROUND)
                binding.txtDistance.visibility = View.VISIBLE
            binding.data = currentData

            Glide.with(context)
                .load(currentData.firstImageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_empty_picture) // 이미지 로딩을 시작하기 전에 보여줄 이미지를 설정
                .error(R.drawable.ic_empty_picture)       // 리소스를 불러오다가 에러가 발생했을 때 보여줄 이미지를 설정
                .fallback(R.drawable.ic_empty_picture)    // load할 url이 null인 경우 등 비어있을 때 보여줄 이미지를 설정
                .into(binding.imageView)
        }
    }

    inner class FavoriteViewHolder(val binding: FavoriteListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(currentData: Item) {
            binding.data = currentData
            Glide.with(context)
                .load(currentData.firstImageUrl)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                .centerCrop()
                .placeholder(R.drawable.ic_empty_picture)
                .error(R.drawable.ic_empty_picture)
                .fallback(R.drawable.ic_empty_picture)
                .into(binding.imageView)
        }
    }
    @GlideModule
    inner class PetViewHolder(val binding: PetListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(currentData: Item) {
            binding.data = currentData
            Glide.with(context)
                .load(currentData.firstImageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_empty_picture)
                .error(R.drawable.ic_empty_picture)
                .fallback(R.drawable.ic_empty_picture)
                .into(binding.imageView)
        }
    }

    // 뷰 홀더 지정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (type) {
            RecyclerViewType.LIST, RecyclerViewType.AROUND-> ListViewHolder(CampingListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            RecyclerViewType.FAVORITE -> FavoriteViewHolder(FavoriteListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            RecyclerViewType.PET -> PetViewHolder(PetListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    // 뷰 홀더에 데이터 바인딩
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListViewHolder -> holder.bind(list[position])
            is FavoriteViewHolder -> holder.bind(list[position])
            is PetViewHolder -> holder.bind(list[position])
        }
        holder.itemView.setOnClickListener {
            listener.onItemClick(list[position])
        }
    }

    // 뷰 홀더의 개수 리턴
    override fun getItemCount(): Int = list.size
}

