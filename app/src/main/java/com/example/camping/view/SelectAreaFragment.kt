package com.example.camping.view

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.camping.R
import com.example.camping.util.inheritance.BaseFragment
import com.example.camping.databinding.FragmentSelectAreaBinding
import com.example.camping.util.data.FragmentEventType
import com.example.camping.util.data.LOG.ERROR
import com.example.camping.util.data.ListFragmentSafeArgs
import com.example.camping.util.data.SelectAreaFragmentSafeArgs
import com.example.camping.viewModel.SelectAreaViewModel

class SelectAreaFragment: BaseFragment<FragmentSelectAreaBinding, SelectAreaViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_select_area

    private val action: SelectAreaFragmentArgs by navArgs()
    private val data: SelectAreaFragmentSafeArgs
        get() = action.data

    override fun setViewModel() {
        viewModel = ViewModelProvider(this)[SelectAreaViewModel::class.java]
    }

    override fun viewInitialize() {
        binding.viewModel = viewModel
    }

    override fun viewEvent() {
        viewModel.fragmentCall.observe(viewLifecycleOwner, {
            when (it.fragmentEventType) {
                FragmentEventType.BACK_STACK -> backStack()
                FragmentEventType.START_LIST_FRAGMENT -> startListFragment(it.value!!)

                else -> Log.d(ERROR, "viewEvent: Not contain FragmentEventType")
            }
        })
    }

    private fun startListFragment(area: String) {
        val data = ListFragmentSafeArgs(data.param, data.value, area)
        navController.navigate(SelectAreaFragmentDirections.actionSelectAreaFragmentToListFragment(data))
    }
}