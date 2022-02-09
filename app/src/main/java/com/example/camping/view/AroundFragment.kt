package com.example.camping.view

import android.location.*
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.camping.R
import com.example.camping.adapter.RecyclerViewAdapter
import com.example.camping.util.inheritance.BaseFragment
import com.example.camping.data.Repository
import com.example.camping.data.retrofit.CustomClient
import com.example.camping.data.retrofit.CustomInterceptor
import com.example.camping.data.vo.Item
import com.example.camping.databinding.FragmentListBinding
import com.example.camping.util.data.*
import com.example.camping.util.data.LOG.ERROR
import com.example.camping.viewModel.AroundViewModel
import com.example.camping.viewModel.ViewModelFactory

class AroundFragment: BaseFragment<FragmentListBinding, AroundViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_list

    private lateinit var repository: Repository
    private lateinit var gpsTracker: GPSTracker
    private lateinit var adapter: RecyclerViewAdapter
    private var latitude: Double? = null
    private var longitude: Double? = null

    private var pageNo = 0

    override fun setRepository() {
        repository = Repository(
            CustomClient.getClient(
                CustomInterceptor.Builder(ParameterType.AROUND)
                    .pageNo(pageNo)
                    .build()
            ), null
        )
        setLocation()
    }

    private fun setLocation() {
        gpsTracker = GPSTracker(requireContext())
        latitude = gpsTracker.getLatitude()
        longitude = gpsTracker.getLongitude()
    }

    override fun setViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(repository, Geocoder(requireContext())))[AroundViewModel::class.java]
    }

    override fun viewInitialize() {
        binding.viewModel = viewModel
        binding.btnBackStack.visibility = View.GONE
        setRecyclerView()
        if (isInit.compareAndSet(true, false))
            getList()
    }

    private fun setRecyclerView() {
        adapter = RecyclerViewAdapter(requireContext(), RecyclerViewType.AROUND)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)
    }

    override fun viewEvent() {
        viewModel.fragmentCall.observe(viewLifecycleOwner, {
            when (it.fragmentEventType) {
                FragmentEventType.PAGE_NO -> setPageNo(it.pageNo!!)
                FragmentEventType.SUCCESS_LOAD -> onSuccessLoad()
                FragmentEventType.EMPTY_LOAD -> onEmptyLoad()
                FragmentEventType.FAIL_LOAD -> onFailLoad()

                else -> Log.d(ERROR, "viewEvent: Not contain FragmentEventType")
            }
        })
        recyclerViewEvent()
    }

    private fun recyclerViewEvent() {
        // 리사이클러뷰 스크롤 최하단 시 재요청
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1

                if (lastVisibleItemPosition >= 3 && lastVisibleItemPosition == itemTotalCount)
                    getList()
            }
        })

        // 리사이클러뷰 클릭 이벤트
        adapter.setOnItemClickListener(object : RecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(data: Item) {
                val value = DetailFragmentSafeArgs(data)
                navController.navigate(AroundFragmentDirections.actionAroundFragmentToDetailFragment(value))
            }
        })
    }

    private fun getList() {
        binding.progressBar.visibility = View.VISIBLE
        if (latitude == null || longitude == null)
            onFailLoad()
        else
            viewModel.getAroundList(latitude!!, longitude!!)
    }
    private fun setPageNo(pageNo: Int) {
        this.pageNo = pageNo
    }

    private fun onSuccessLoad() {
        gpsTracker.stopUsingGps()
        binding.progressBar.visibility = View.GONE
    }

    private fun onEmptyLoad() {
        binding.progressBar.visibility = View.GONE
        binding.txtEmpty.visibility = View.VISIBLE
    }

    private fun onFailLoad() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.txtError.visibility = View.VISIBLE
    }
}