package com.example.camping.viewModel

import com.example.camping.util.data.FragmentCall
import com.example.camping.util.data.FragmentEventType
import com.example.camping.util.inheritance.BaseViewModel

class SelectAreaViewModel : BaseViewModel() {
    fun onClickGyeongGi() {
        _fragmentCall.postValue(
            FragmentCall.Builder(FragmentEventType.START_LIST_FRAGMENT)
            .value("경기")
            .build())
    }
    fun onClickChungCheong() {
        _fragmentCall.postValue(FragmentCall.Builder(FragmentEventType.START_LIST_FRAGMENT)
            .value("충청")
            .build())
    }
    fun onClickGyeongSang() {
        _fragmentCall.postValue(FragmentCall.Builder(FragmentEventType.START_LIST_FRAGMENT)
            .value("경상")
            .build())
    }
    fun onClickJeJu() {
        _fragmentCall.postValue(FragmentCall.Builder(FragmentEventType.START_LIST_FRAGMENT)
            .value("제주")
            .build())
    }
    fun onClickGangWon() {
        _fragmentCall.postValue(FragmentCall.Builder(FragmentEventType.START_LIST_FRAGMENT)
            .value("강원")
            .build())
    }
    fun onClickJeoLa() {
        _fragmentCall.postValue(FragmentCall.Builder(FragmentEventType.START_LIST_FRAGMENT)
            .value("전라")
            .build())
    }
}