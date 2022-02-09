package com.example.camping.util.data

import com.example.camping.data.vo.Item

class FragmentCall(
    val fragmentEventType: FragmentEventType,
    val param: ParameterType?,
    val value: String?,
    val item: Item?,
    val pageNo: Int?,
    val isSelected: Boolean?
) {
    class Builder(private val fragmentEventType: FragmentEventType) {
        private var param: ParameterType? = null
        private var value: String? = null
        private var item: Item? = null
        private var pageNo: Int? = null
        private var bool: Boolean? = null

        fun param(param: ParameterType?): Builder {
            this.param = param
            return this
        }

        fun value(value: String?): Builder {
            this.value = value
            return this
        }

        fun item(item: Item?): Builder {
            this.item = item
            return this
        }

        fun pageNo(pageNo: Int?): Builder {
            this.pageNo = pageNo
            return this
        }

        fun bool(bool: Boolean?): Builder {
            this.bool = bool
            return this
        }

        fun build() = FragmentCall(fragmentEventType, param, value, item, pageNo, bool)
    }
}

enum class FragmentEventType {
    BACK_STACK,
    PAGE_NO,
    SUCCESS_LOAD,
    EMPTY_LOAD,
    FAIL_LOAD,
    ON_ITEM_CLICK,
    START_LIST_FRAGMENT,
    START_SELECT_AREA_FRAGMENT,
    FAVORITE_CLICK,
    ASK_REAL_REMOVE_ABOUT_FAVORITE,
    OVER_SIZE,
}