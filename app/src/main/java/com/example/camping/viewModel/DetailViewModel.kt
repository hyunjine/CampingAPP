package com.example.camping.viewModel

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.camping.data.Repository
import com.example.camping.data.vo.Item
import com.example.camping.util.data.FragmentCall
import com.example.camping.util.data.FragmentEventType
import com.example.camping.util.data.LOG.ERROR
import com.example.camping.util.data.LOG.TAG
import com.example.camping.util.inheritance.BaseViewModel
import com.example.camping.view.ViewPagerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: Repository,
    private val item: Item
) : BaseViewModel() {
    private val _fragments = ListLiveData<Fragment>()
    val fragments: LiveData<ArrayList<Fragment>>
        get() = _fragments

    private val _detail = MutableLiveData<Item>()
    val detail: LiveData<Item>
        get() = _detail

    fun getImageList(contentId: String) {
        addDisposable(
            repository.getImageList(contentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        setList(it)
                    },
                    {
                        onFailLoad()
                        Log.e(ERROR, "DetailFragmentViewModel: ${it.message}")
                    }
                )
        )
    }

    private fun setList(imageList: ArrayList<String>) {
        imageList.forEach { image ->
            val viewPagerFragment = ViewPagerFragment(null,null, image)
            _fragments.add(viewPagerFragment)
        }
        _detail.postValue(item)
        onSuccessLoad()
    }

    fun isExist(contentId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            onSelected(repository.isExist(contentId))
        }
    }

    fun onClickFavorite(view: View) {
        val currentState = !view.isSelected
        CoroutineScope(Dispatchers.IO).launch {
            // 찜 선택 시
            if (currentState) {
                if (repository.getFavoriteSize()!! >= 10) {
                    _fragmentCall.postValue(
                        FragmentCall.Builder(FragmentEventType.OVER_SIZE)
                            .build()
                    )
                } else {
                    insertFavorite()
                    onSelected(currentState)
                }
            }
            // 찜 해제 시
            else {
                _fragmentCall.postValue(
                    FragmentCall.Builder(FragmentEventType.ASK_REAL_REMOVE_ABOUT_FAVORITE)
                        .build()
                )
            }
        }
    }

    private fun insertFavorite() {
        repository.insertFavorite(item)
    }

    fun deleteFavorite() = repository.deleteFavorite(item.contentId)

    fun onSelected(isSelected: Boolean) {
        _fragmentCall.postValue(
            FragmentCall.Builder(FragmentEventType.FAVORITE_CLICK)
                .bool(isSelected)
                .build()
        )
    }
}