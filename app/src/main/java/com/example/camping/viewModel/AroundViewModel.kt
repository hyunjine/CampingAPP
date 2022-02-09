package com.example.camping.viewModel

import android.location.Geocoder
import android.location.Location
import android.util.Log
import com.example.camping.data.Repository
import com.example.camping.data.vo.Item
import com.example.camping.util.data.LOG.ERROR
import com.example.camping.util.data.LOG.TAG
import com.example.camping.util.inheritance.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.math.round

class AroundViewModel (
    private val repository: Repository,
    private val geoCoder: Geocoder
) : BaseViewModel() {
    fun getAroundList(latitude: Double, longitude: Double) {
        setActionBar(latitude, longitude)

        addDisposable(
            repository.getAroundList(longitude.toString(), latitude.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        if (it.items.isEmpty())
                            onEmptyLoad()
                        else {
                            setPageNo(it.pageNo)
                            setList(it.items, latitude, longitude)
                            onSuccessLoad()
                        }
                    }, {
                        onFailLoad()
                        Log.d(ERROR, "AroundViewModel: ${it.message}")
                    })
        )
    }

    private fun setActionBar(latitude: Double, longitude: Double) {
        try {
            val address = geoCoder.getFromLocation(latitude, longitude, 5)
            for (i: Int in 0..4) {
                if (address[i].thoroughfare != null) {
                    _actionBar.postValue(address[i].thoroughfare)
                    break
                }
            }

        } catch (e: Exception) {
            Log.e(ERROR, "convertToAdress: ${e.message}")
        }
    }

    private fun setList(items: ArrayList<Item>, latitude: Double, longitude: Double) {
        val currentLocation = Location("current").apply {
            setLatitude(latitude)
            setLongitude(longitude)
        }
        items.forEach {
            it.distance = getDistance(it.mapY!!, it.mapX!!, currentLocation)
        }

        _list.addAll(items)
    }

    private fun getDistance(latitude: Double, longitude: Double, currentLocation: Location) : String{
        val location = Location("camping").apply {
            setLatitude(latitude)
            setLongitude(longitude)
        }
        val distance = currentLocation.distanceTo(location)
        return if (distance > 1000) {
            "거리: ${round((currentLocation.distanceTo(location)/100)) /10}km"
        } else {
            "거리: ${round((currentLocation.distanceTo(location)*10)) /10}m"
        }
    }
}
