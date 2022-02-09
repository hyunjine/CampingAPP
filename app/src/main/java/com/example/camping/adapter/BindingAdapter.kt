package com.example.camping.adapter

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.camping.data.vo.Item

object BindingAdapter {

    // null check
    @BindingAdapter("nullCheck")
    @JvmStatic
    fun nullCheck(
        textView: TextView,
        data: String?
    ) {
        if (data != null)
            textView.text = data
        else
            textView.text = "홈페이지 문의"
    }

    // concat null check
    @SuppressLint("SetTextI18n")
    @BindingAdapter("nullCheck_1", "nullCheck_2")
    @JvmStatic
    fun concatTextNullCheck(
        textView: TextView,
        data_1: String?,
        data_2: String?
    ) {
        if (data_1 != null && data_2 == null)
            textView.text = data_1
        else if (data_2 != null && data_1 == null)
            textView.text = data_2
        else if (data_1 != null && data_2 != null)
            textView.text = "$data_1 $data_2"
        else
            textView.text = "홈페이지 문의"
    }

    // Int + 개
    @SuppressLint("SetTextI18n")
    @BindingAdapter("nullCheckInt")
    @JvmStatic
    fun nullCheckInt(
        textView: TextView,
        data: Int?
    ) {
        if (data != null)
            textView.text = "${data}개"
        else
            textView.text = "홈페이지 문의"
    }

    // convertYN
    @BindingAdapter("convertYN")
    @JvmStatic
    fun convertYN(
        textView: TextView,
        bool: String?
    ) {
        if (bool != null)
            textView.text = if (bool == "Y") "가능" else "불가능"
        else
            textView.text = "홈페이지 문의"
    }

    // RecyclerView
    @BindingAdapter("items")
    @JvmStatic
    fun setItems(
        recyclerView: RecyclerView,
        items: ArrayList<Item>?
    ) {
        if (items != null) {
            val mAdapter = recyclerView.adapter as RecyclerViewAdapter?
            mAdapter?.list = items
            mAdapter?.notifyDataSetChanged()
        }
    }

    @BindingAdapter("stringArr")
    @JvmStatic
    fun stringArr(
        recyclerView: RecyclerView,
        items: ArrayList<String>?
    ) {
        if (items != null) {
            val mAdapter = recyclerView.adapter as RecentViewAdapter?
            mAdapter?.list = items
            mAdapter?.notifyDataSetChanged()
        }
    }

    // ViewPager
    @BindingAdapter("image")
    @JvmStatic
    fun setImage(
        viewPager: ViewPager2,
        items: ArrayList<Fragment>?
    ) {
        if (items != null) {
            val mAdapter = viewPager.adapter as ViewPagerAdapter?
            mAdapter?.fragments = items
            mAdapter?.notifyDataSetChanged()
        }
    }
}