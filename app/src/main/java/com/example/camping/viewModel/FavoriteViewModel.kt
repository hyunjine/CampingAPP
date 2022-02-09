package com.example.camping.viewModel

import android.util.Log
import com.example.camping.data.Repository
import com.example.camping.util.data.LOG
import com.example.camping.util.inheritance.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FavoriteViewModel(private val repository: Repository): BaseViewModel() {
    fun getFavoriteList(){
        setActionBar()

        addDisposable(
            repository.getAllFavorite()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        if (it.isEmpty())
                            onEmptyLoad()
                        else {
                            _list.postValue(it.toCollection(ArrayList()))
                            onSuccessLoad()
                        }
                    },
                    {
                        onFailLoad()
                        Log.e(LOG.ERROR, "getFavoriteList: ${it.message}")
                    }
                )
        )
    }

    private fun setActionBar() {
        _actionBar.postValue("찜 목록")
    }
}