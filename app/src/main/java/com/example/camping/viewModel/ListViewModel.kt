package com.example.camping.viewModel

import android.util.Log
import com.example.camping.data.Repository
import com.example.camping.data.vo.Item
import com.example.camping.data.vo.Response
import com.example.camping.util.data.LOG
import com.example.camping.util.data.LOG.TAG
import com.example.camping.util.data.ListFragmentSafeArgs
import com.example.camping.util.data.ParameterType
import com.example.camping.util.inheritance.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*

class ListViewModel (
    private val repository: Repository,
    private val data: ListFragmentSafeArgs
) : BaseViewModel() {
    fun getList() {
        setActionBar(data.value!!, data.extraValue)

        when (data.param) {
            ParameterType.AREA, ParameterType.FAL_NAME, ParameterType.PET, ParameterType.TYPE -> getBaseList()
            ParameterType.KEY_WORD -> getKeyWordList(data.value)
            else -> return
        }
    }

    private fun getBaseList() {
        addDisposable(
            repository.getDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        setList(it)
                    }, {
                        onFailLoad()
                        Log.d(LOG.ERROR, "getBaseList: ${it.message}")
                    })
        )
        Log.d(TAG, "getBaseList:")
    }

    private fun getKeyWordList(ketWord: String) {
        addDisposable(
            repository.getKeyWordList(ketWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        setList(it)
                    }, {
                        onFailLoad()
                        Log.d(LOG.ERROR, "getKeyWordList: ${it.message}")
                    })
        )
    }

    private fun setList(response: Response) {
        if (response.items.isEmpty())
            onEmptyLoad()
        else {
            setPageNo(response.pageNo)
            _list.addAll(response.items)
            onSuccessLoad()
        }
    }

    private fun setActionBar(value: String, extraValue: String?) {
        if (extraValue == null)
            _actionBar.postValue(value)
        else
            _actionBar.postValue("${value}/${extraValue}")
    }
}