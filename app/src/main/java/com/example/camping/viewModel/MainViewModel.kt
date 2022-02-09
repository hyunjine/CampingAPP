package com.example.camping.viewModel

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.example.camping.util.inheritance.BaseViewModel
import com.example.camping.data.Repository
import com.example.camping.data.vo.Item
import com.example.camping.util.data.FragmentCall
import com.example.camping.util.data.FragmentEventType
import com.example.camping.util.data.LOG.ERROR
import com.example.camping.util.data.ParameterType
import com.example.camping.view.ViewPagerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList

interface OnViewPagerItemClick{
    fun onItemClick(item: Item)
}

class MainViewModel : BaseViewModel(), OnViewPagerItemClick{
    private val _fragments = ListLiveData<Fragment>()
    val fragments: LiveData<ArrayList<Fragment>>
        get() = _fragments

    fun getRandomSite(repository: Repository, keyword: String) {
        addDisposable(
            repository.getRandomKeyWordList(keyword,"15", "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        if (it.isEmpty())
                            onEmptyLoad()
                        else {
                            it.forEach { items ->
                                _fragments.add(ViewPagerFragment(this, items, null))
                            }
                            onSuccessLoad()
                        }
                    },
                    {
                        onFailLoad()
                        Log.d(ERROR, "MainFragmentViewModel: ${it.message}")
                    }
                )
        )
    }

    fun getPetList(repository: Repository) {
        addDisposable(
            repository.getInitPetList("100", "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _list.addAll(it)
                    },
                    {
                        onFailLoad()
                        Log.d(ERROR, "setPetList: ${it.message}")
                    }
                )
        )
    }

    fun onClickAutoCamping() {
        _fragmentCall.postValue(FragmentCall.Builder(FragmentEventType.START_SELECT_AREA_FRAGMENT)
            .param(ParameterType.TYPE)
            .value("야영장")
            .build())
    }

    fun onClickGlamPing() {
        _fragmentCall.postValue(FragmentCall.Builder(FragmentEventType.START_SELECT_AREA_FRAGMENT)
            .param(ParameterType.TYPE)
            .value("글램핑")
            .build())
    }

    fun onClickCaravan() {
        _fragmentCall.postValue(FragmentCall.Builder(FragmentEventType.START_SELECT_AREA_FRAGMENT)
            .param(ParameterType.TYPE)
            .value("카라반")
            .build())
    }

    fun onClickSea() {
        _fragmentCall.postValue(FragmentCall.Builder(FragmentEventType.START_LIST_FRAGMENT)
            .param(ParameterType.KEY_WORD)
            .value("바다")
            .build())
    }

    fun onClickMountain() {
        _fragmentCall.postValue(FragmentCall.Builder(FragmentEventType.START_LIST_FRAGMENT)
            .param(ParameterType.KEY_WORD)
            .value("산")
            .build())
    }

    fun onClickValley() {
        _fragmentCall.postValue(FragmentCall.Builder(FragmentEventType.START_LIST_FRAGMENT)
            .param(ParameterType.KEY_WORD)
            .value("계곡")
            .build())
    }

    fun onClickFishing() {
        _fragmentCall.postValue(FragmentCall.Builder(FragmentEventType.START_LIST_FRAGMENT)
            .param(ParameterType.KEY_WORD)
            .value("낚시")
            .build())
    }

    fun onClickPet() {
        _fragmentCall.postValue(FragmentCall.Builder(FragmentEventType.START_SELECT_AREA_FRAGMENT)
            .param(ParameterType.PET)
            .value("애완동물 동반")
            .build())
    }
    override fun onItemClick(item: Item) {
        _fragmentCall.postValue(FragmentCall.Builder(FragmentEventType.ON_ITEM_CLICK)
            .item(item)
            .build())
    }
}