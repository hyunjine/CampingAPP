package com.example.camping.view

import android.app.Dialog
import android.app.SearchManager
import android.content.Context
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.example.camping.R
import com.example.camping.data.vo.Item

class NoticeDialog(context: Context, layoutResourceId: Int) {
    interface OnRemoveClickListener {
        fun onRemoveClick()
    }

    private lateinit var listener: OnRemoveClickListener

    fun setRemoveClickListener(listener: OnRemoveClickListener) {
        this.listener = listener
    }

    init {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layoutResourceId)

        val params = dialog.window?.attributes

        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT

        dialog.window?.attributes = params
        dialog.show()

        if (layoutResourceId == R.layout.dialog_notice) {
            dialog.findViewById<Button>(R.id.btn_ok).setOnClickListener {
                dialog.dismiss()
            }
        }
        else if (layoutResourceId == R.layout.dialog_ask) {
            dialog.findViewById<Button>(R.id.btn_remove).setOnClickListener {
                listener.onRemoveClick()
                dialog.dismiss()
            }
            dialog.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
                dialog.dismiss()
            }
        }
    }
}