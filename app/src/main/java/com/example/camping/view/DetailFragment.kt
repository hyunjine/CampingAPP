package com.example.camping.view

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.camping.R
import com.example.camping.adapter.ViewPagerAdapter
import com.example.camping.util.inheritance.BaseFragment
import com.example.camping.data.Repository
import com.example.camping.data.retrofit.BasicClient
import com.example.camping.data.retrofit.CustomClient
import com.example.camping.data.retrofit.CustomInterceptor
import com.example.camping.data.room.MDataBase
import com.example.camping.data.vo.Item
import com.example.camping.databinding.FragmentDetailBinding
import com.example.camping.util.data.FragmentEventType
import com.example.camping.util.data.LOG.ERROR
import com.example.camping.util.data.LOG.TAG
import com.example.camping.util.data.ParameterType
import com.example.camping.viewModel.DetailViewModel
import com.example.camping.viewModel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailFragment : BaseFragment<FragmentDetailBinding, DetailViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_detail

    private val action: DetailFragmentArgs by navArgs()
    private val data: Item
        get() = action.data.item

    private val contentId: Int
        get() = data.contentId

    private lateinit var repository: Repository

    override fun setRepository() {
        repository = Repository(CustomClient.getClient(CustomInterceptor.Builder(ParameterType.IMAGE_LIST).build()), MDataBase.getInstance(requireContext()))
    }

    override fun setViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(repository, data))[DetailViewModel::class.java]
    }

    override fun viewInitialize() {
        binding.viewModel = viewModel
        setViewPager()

        viewModel.getImageList(contentId.toString())
        viewModel.isExist(contentId)
    }

    private fun setViewPager() {
        binding.viewpager.adapter = ViewPagerAdapter(requireActivity())
        binding.viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    override fun viewEvent() {
        viewModel.fragmentCall.observe(viewLifecycleOwner, {
            when (it.fragmentEventType) {
                FragmentEventType.BACK_STACK -> backStack()
                FragmentEventType.SUCCESS_LOAD -> onSuccessLoad()
                FragmentEventType.FAVORITE_CLICK -> onFavoriteClick(it.isSelected!!)
                FragmentEventType.ASK_REAL_REMOVE_ABOUT_FAVORITE -> onAskRealRemove()
                FragmentEventType.OVER_SIZE -> onOverSize()

                else -> Log.d(ERROR, "viewEvent: Not contain FragmentEventType")
            }
        })
        viewPagerEvent()
    }

    private fun viewPagerEvent() {
        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            @SuppressLint("SetTextI18n")
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                binding.txtImageCount.text = "${position + 1}/${binding.viewpager.adapter!!.itemCount}"
            }
        })
    }

    private fun onFavoriteClick(isSelected: Boolean) {
        binding.btnFavorite.isSelected = isSelected
        if (isInit.compareAndSet(true, false))
            return
        if (isSelected)
            Toast.makeText(requireContext(), "찜을 눌렀습니다.\n찜 목록에서 확인해주세요.", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(requireContext(), "찜을 해제하였습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun onAskRealRemove() {
        NoticeDialog(requireContext(), R.layout.dialog_ask).setRemoveClickListener(object : NoticeDialog.OnRemoveClickListener {
            override fun onRemoveClick() {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.deleteFavorite()
                }
                viewModel.onSelected(!binding.btnFavorite.isSelected)
            }
        })
    }

    private fun onOverSize() = Toast.makeText(requireContext(), "최대 10개까지 찜이 가능합니다.\n추가를 원하실 경우 찜 목록에서 삭제해주세요.", Toast.LENGTH_SHORT).show()

    private fun onSuccessLoad() {
        binding.progressBar.visibility = View.GONE
        binding.txtImageCount.visibility = View.VISIBLE
    }
}