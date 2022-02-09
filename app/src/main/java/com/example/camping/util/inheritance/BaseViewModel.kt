package com.example.camping.util.inheritance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.camping.data.vo.Item
import com.example.camping.util.data.FragmentCall
import com.example.camping.util.data.FragmentEventType
import com.example.camping.viewModel.ListLiveData
import com.example.camping.viewModel.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {
    protected val _fragmentCall = SingleLiveEvent<FragmentCall>()
    val fragmentCall: LiveData<FragmentCall>
        get() = _fragmentCall

    protected val _list = ListLiveData<Item>()
    val list : LiveData<ArrayList<Item>>
        get() = _list

    protected val _actionBar = MutableLiveData<String>()
    val actionBar : LiveData<String>
        get() = _actionBar

    private val compositeDisposable = CompositeDisposable()

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    protected fun onSuccessLoad() {
        _fragmentCall.postValue(
            FragmentCall.Builder(FragmentEventType.SUCCESS_LOAD)
                .build())
    }

    protected fun onEmptyLoad() {
        _fragmentCall.postValue(
            FragmentCall.Builder(FragmentEventType.EMPTY_LOAD)
                .build())
    }

    protected fun onFailLoad() {
        _fragmentCall.postValue(
            FragmentCall.Builder(FragmentEventType.FAIL_LOAD)
                .build())
    }

    protected fun setPageNo(pageNo: Int) {
        _fragmentCall.postValue(
            FragmentCall.Builder(FragmentEventType.PAGE_NO)
                .pageNo(pageNo)
                .build()
        )
    }

    fun backStack() {
        _fragmentCall.postValue(
            FragmentCall.Builder(FragmentEventType.BACK_STACK)
                .build()
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
