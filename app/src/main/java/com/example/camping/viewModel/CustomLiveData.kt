package com.example.camping.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.annotation.MainThread
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class ListLiveData<T> : MutableLiveData<ArrayList<T>>() {
    init {
        postValue(ArrayList())
    }

    fun add(item: T) {
        val items = value
        items!!.add(item)
        postValue(items)
    }

    fun addAll(list: ArrayList<T>) {
        val items = value
        items!!.addAll(list)
        postValue(items)
    }

    fun clear() {
        val items = value
        items!!.clear()
        postValue(items)
    }
}

open class SingleLiveEvent<T> : MutableLiveData<T>() {
    companion object {
        private const val TAG = "SingleLiveEvent"
        private const val MESSAGE = "Multiple observers registered but only one will be notified of changes."
    }

    private val mPending: AtomicBoolean = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.w(TAG, MESSAGE)
        }

        // Observe the internal MutableLiveData
        super.observe(owner, Observer { t ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(@Nullable t: T?) {
        mPending.set(true)
        super.setValue(t)
    }
}
