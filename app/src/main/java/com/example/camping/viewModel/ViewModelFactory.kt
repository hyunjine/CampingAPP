package com.example.camping.viewModel

import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.camping.data.Repository
import com.example.camping.data.vo.Item
import com.example.camping.util.data.ListFragmentSafeArgs

class ViewModelFactory : ViewModelProvider.Factory {
    private lateinit var listFragmentSafeArgs: ListFragmentSafeArgs
    private val repository: Repository
    private lateinit var item: Item
    private lateinit var geoCoder: Geocoder


    // FavoriteFragment
    constructor(repository: Repository) {
        this.repository = repository
    }
    // AroundFragment
    constructor(repository: Repository, geoCoder: Geocoder) {
        this.repository = repository
        this.geoCoder = geoCoder
    }
    // ListFragment
    constructor(repository: Repository, listFragmentSafeArgs: ListFragmentSafeArgs) {
        this.repository = repository
        this.listFragmentSafeArgs = listFragmentSafeArgs
    }
    // DetailFragment
    constructor(repository: Repository, item: Item) {
        this.repository = repository
        this.item = item
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ListViewModel::class.java) -> {
                ListViewModel(repository, listFragmentSafeArgs) as T
            }
            modelClass.isAssignableFrom(AroundViewModel::class.java) -> {
                AroundViewModel(repository, geoCoder) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository, item) as T
            } else -> { throw IllegalArgumentException() }
        }
    }
}