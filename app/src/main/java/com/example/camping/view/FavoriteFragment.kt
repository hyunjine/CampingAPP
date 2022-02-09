package com.example.camping.view

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.camping.R
import com.example.camping.adapter.RecyclerViewAdapter
import com.example.camping.data.Repository
import com.example.camping.data.room.MDataBase
import com.example.camping.data.vo.Item
import com.example.camping.databinding.FragmentListBinding
import com.example.camping.util.data.*
import com.example.camping.util.inheritance.BaseFragment
import com.example.camping.viewModel.FavoriteViewModel
import com.example.camping.viewModel.ViewModelFactory

class FavoriteFragment: BaseFragment<FragmentListBinding, FavoriteViewModel>() {
    private lateinit var repository: Repository
    private lateinit var adapter: RecyclerViewAdapter

    override val layoutResourceId: Int
        get() = R.layout.fragment_list

    override fun setRepository() {
        repository = Repository(null, MDataBase.getInstance(requireContext()))
    }

    override fun setViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(repository))[FavoriteViewModel::class.java]
    }

    override fun viewInitialize() {
        binding.viewModel = viewModel
        binding.btnBackStack.visibility = View.GONE
        setRecyclerView()
        if (isInit.compareAndSet(true, false))
            getList()
    }

    private fun setRecyclerView() {
        adapter = RecyclerViewAdapter(requireContext(), RecyclerViewType.FAVORITE)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)
    }

    override fun viewEvent() {
        viewModel.fragmentCall.observe(viewLifecycleOwner, {
            when (it.fragmentEventType) {
                FragmentEventType.SUCCESS_LOAD -> onSuccessLoad()
                FragmentEventType.EMPTY_LOAD -> onEmptyLoad()
                FragmentEventType.FAIL_LOAD -> onFailLoad()

                else -> Log.e(LOG.ERROR, "viewEvent: Not contain FragmentEventType")
            }
        })
        recyclerViewEvent()
    }

    private fun recyclerViewEvent() {
        // 리사이클러뷰 클릭 이벤트
        adapter.setOnItemClickListener(object : RecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(item: Item) {
                val value = DetailFragmentSafeArgs(item)
                navController.navigate(FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(value))
            }
        })
    }
    private fun getList() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getFavoriteList()
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