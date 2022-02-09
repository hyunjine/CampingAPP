package com.example.camping.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.whenResumed
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.viewpager2.widget.ViewPager2
import com.example.camping.R
import com.example.camping.adapter.RecyclerViewAdapter
import com.example.camping.adapter.ViewPagerAdapter
import com.example.camping.util.inheritance.BaseFragment
import com.example.camping.data.Repository
import com.example.camping.data.retrofit.CustomClient
import com.example.camping.data.retrofit.CustomInterceptor
import com.example.camping.data.vo.Item
import com.example.camping.databinding.FragmentMainBinding
import com.example.camping.util.data.*
import com.example.camping.util.data.LOG.ERROR
import com.example.camping.util.data.RECOMMAND.KEY_WORDS
import com.example.camping.viewModel.MainViewModel
import kotlinx.coroutines.*
import java.util.*

class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_main

    private lateinit var repository: Repository

    private lateinit var adapter: RecyclerViewAdapter
    private var viewPagerState = ViewPager2.SCROLL_STATE_IDLE
    private var currentIndex = 0

    override fun setRepository() {
        repository = Repository(CustomClient.getClient(CustomInterceptor.Builder(ParameterType.RANDOM_LIST).build()), null)
    }

    override fun setViewModel() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun viewInitialize() {
        binding.viewModel = viewModel
        setViewPager()
        setRecyclerView()

        val index = Random().nextInt(KEY_WORDS.size)

        if (isInit.compareAndSet(true, false)){
            viewModel.getRandomSite(Repository(
                CustomClient.getClient(
                    CustomInterceptor.Builder(ParameterType.RANDOM_LIST)
                        .build()
                ),
                null
            ), KEY_WORDS[index])

            viewModel.getPetList(Repository(
                CustomClient.getClient(
                    CustomInterceptor.Builder(ParameterType.INIT_PET)
                        .area("강원")
                        .build()
                ),
                null
            ))
        }
    }

    private fun setViewPager() {
        binding.viewpager.adapter = ViewPagerAdapter(requireActivity())
        binding.viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    private fun setRecyclerView() {
        adapter = RecyclerViewAdapter(requireContext(), RecyclerViewType.PET)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.setHasFixedSize(true)
        LinearSnapHelper().attachToRecyclerView(binding.recyclerView)
    }

    override fun viewEvent() {
        viewModel.fragmentCall.observe(viewLifecycleOwner, {
            when (it.fragmentEventType) {
                FragmentEventType.START_LIST_FRAGMENT -> startListFragment(it.param!!, it.value!!)
                FragmentEventType.START_SELECT_AREA_FRAGMENT -> startSelectAreaFragment(it.param!!, it.value!!)
                FragmentEventType.ON_ITEM_CLICK -> startDetailFragment(it.item!!)
                FragmentEventType.SUCCESS_LOAD -> onSuccessLoad()
                FragmentEventType.EMPTY_LOAD, FragmentEventType.FAIL_LOAD -> Log.d(ERROR, "viewEvent: onEmptyLoad or on FailLoad")

                else -> Log.d(ERROR, "viewEvent: Not contain FragmentEventType")
            }
        })
        viewPagerEvent()
        recyclerViewEvent()
    }

    private fun viewPagerEvent() {
        // 사용자 슬라이드 시 코루틴 대기
        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                viewPagerState = state
            }
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setImageCountText(position.plus(1))
            }
        })
        // 자동 슬라이드
        CoroutineScope(Dispatchers.Main).launch {
            whenResumed {
                while(true) {
                    delay(3000)
                    if (viewPagerState == ViewPager2.SCROLL_STATE_IDLE) {
                        if (++currentIndex == viewModel.fragments.value!!.size)
                            currentIndex = 0
                        binding.viewpager.setCurrentItem(currentIndex, true)
                    }
                    else
                        continue
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setImageCountText(position: Int) {
        binding.txtImageCount.text = "${position}/${viewModel.fragments.value?.size}"
    }

    private fun recyclerViewEvent() {
        adapter.setOnItemClickListener(object : RecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(item: Item) {
                navController.navigate(MainFragmentDirections.actionMainFragmentToDetailFragment(DetailFragmentSafeArgs(item)))
            }
        })
    }

    private fun startListFragment(param: ParameterType, value: String?) = navController.navigate(MainFragmentDirections.actionMainFragmentToListFragment(ListFragmentSafeArgs(param, value, null)))

    private fun startSelectAreaFragment(param: ParameterType, value: String) = navController.navigate(MainFragmentDirections.actionMainFragmentToSelectAreaFragment(SelectAreaFragmentSafeArgs(param, value)))

    private fun startDetailFragment(item: Item) = navController.navigate(MainFragmentDirections.actionMainFragmentToDetailFragment(
        DetailFragmentSafeArgs(item)
    ))

    private fun onSuccessLoad() = setImageCountText(currentIndex)

    override fun onResume() {
        viewPagerState = ViewPager2.SCROLL_STATE_IDLE
        super.onResume()
    }
}