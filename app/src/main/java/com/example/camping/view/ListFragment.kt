package com.example.camping.view

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.camping.R
import com.example.camping.adapter.RecyclerViewAdapter
import com.example.camping.util.inheritance.BaseFragment
import com.example.camping.data.Repository
import com.example.camping.data.retrofit.*
import com.example.camping.data.vo.Item
import com.example.camping.databinding.FragmentListBinding
import com.example.camping.util.data.*
import com.example.camping.util.data.LOG.TAG
import com.example.camping.viewModel.ListViewModel
import com.example.camping.viewModel.ViewModelFactory

class ListFragment : BaseFragment<FragmentListBinding, ListViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_list

    private val action: ListFragmentArgs by navArgs()
    private val data: ListFragmentSafeArgs
        get() = action.data!!

    private lateinit var client: CampingService
    private lateinit var repository: Repository

    private lateinit var adapter: RecyclerViewAdapter

    private var pageNo = 0

    override fun setClient() {
        client = when (data.param) {
                ParameterType.AREA -> {
                    CustomClient.getClient(CustomInterceptor.Builder(data.param)
                        .pageNo(pageNo)
                        .area(data.value)
                        .build())
                }
                ParameterType.FAL_NAME -> {
                    CustomClient.getClient(CustomInterceptor.Builder(data.param)
                        .pageNo(pageNo)
                        .falName(data.value)
                        .build())
                }
                ParameterType.KEY_WORD -> {
                    CustomClient.getClient(
                        CustomInterceptor.Builder(data.param)
                            .pageNo(pageNo)
                            .build()
                    )
                }
                ParameterType.TYPE -> {
                    CustomClient.getClient(CustomInterceptor.Builder(data.param)
                        .pageNo(pageNo)
                        .type(data.value)
                        .area(data.extraValue)
                        .build())
                }
                ParameterType.PET -> {
                    CustomClient.getClient(CustomInterceptor.Builder(data.param)
                        .pageNo(pageNo)
                        .area(data.extraValue)
                        .build())
                }
            else -> throw IllegalArgumentException("Other type insert")
        }
    }

    override fun setRepository() {
        repository = Repository(client, null)
    }

    override fun setViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(repository, data)
        )[ListViewModel::class.java]
    }

    override fun viewInitialize() {
        binding.viewModel = viewModel
        setRecyclerView()
        if (isInit.compareAndSet(true, false))
            getList()
    }

    private fun setRecyclerView() {
        adapter = RecyclerViewAdapter(requireContext(), RecyclerViewType.LIST)

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
                FragmentEventType.BACK_STACK -> backStack()

                else -> Log.e(LOG.ERROR, "viewEvent: Not contain FragmentEventType")
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

                if (lastVisibleItemPosition >= 3 && lastVisibleItemPosition == itemTotalCount) {
                    setClient()
                    getList()
                }
            }
        })
        // 리사이클러뷰 클릭 이벤트
        adapter.setOnItemClickListener(object : RecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(item: Item) {
                navController.navigate(ListFragmentDirections.actionListFragmentToDetailFragment(
                    DetailFragmentSafeArgs(item)
                ))
            }
        })
    }

    private fun getList() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getList()
    }

    private fun setPageNo(pageNo: Int) {
        this.pageNo = pageNo
    }

    private fun onSuccessLoad() {
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