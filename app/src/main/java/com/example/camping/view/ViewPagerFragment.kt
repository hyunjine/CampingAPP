package com.example.camping.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.camping.R
import com.example.camping.data.vo.Item
import com.example.camping.databinding.FragmentViewpagerBinding
import com.example.camping.util.data.DetailFragmentSafeArgs
import com.example.camping.viewModel.OnViewPagerItemClick

class ViewPagerFragment(
    private val onViewPagerItemClick: OnViewPagerItemClick?,
    private val item: Item?,
    private val imageUrl: String?
) : Fragment() {
    private var _binding: FragmentViewpagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_viewpager, container,false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewInitialize()
    }

    private fun viewInitialize() {
        if (item != null) {
            Glide.with(this)
                .load(item.firstImageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_empty_picture) // 이미지 로딩을 시작하기 전에 보여줄 이미지를 설정
                .error(R.drawable.ic_empty_picture)       // 리소스를 불러오다가 에러가 발생했을 때 보여줄 이미지를 설정
                .fallback(R.drawable.ic_empty_picture)    // load할 url이 null인 경우 등 비어있을 때 보여줄 이미지를 설정
                .into(binding.imageView)
        } else if (imageUrl != null){
            Glide.with(this)
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_empty_picture) // 이미지 로딩을 시작하기 전에 보여줄 이미지를 설정
                .error(R.drawable.ic_empty_picture)       // 리소스를 불러오다가 에러가 발생했을 때 보여줄 이미지를 설정
                .fallback(R.drawable.ic_empty_picture)    // load할 url이 null인 경우 등 비어있을 때 보여줄 이미지를 설정
                .into(binding.imageView)
        }
    }

    override fun onResume() {
        super.onResume()
        if (item != null) {
            binding.root.setOnClickListener {
                onViewPagerItemClick!!.onItemClick(item)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}