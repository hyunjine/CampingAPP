package com.example.camping.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.camping.R
import com.example.camping.adapter.RecentViewAdapter
import com.example.camping.databinding.FragmentSearchBinding
import com.example.camping.util.data.*
import com.example.camping.util.data.PREPERENCE.P_KEY
import com.google.android.material.tabs.TabLayout
import org.json.JSONArray

class SearchFragment : Fragment() {
    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController : NavController

    private lateinit var preference: SharedPreferences
    private lateinit var currentTabLayout : String
    private lateinit var tabLayoutName: Array<String>
    private lateinit var adapter: RecentViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container,false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(binding.root)
        preference = requireActivity().getSharedPreferences(resources.getString(R.string.app_name), Context.MODE_PRIVATE)
        tabLayoutName = resources.getStringArray(R.array.tabArray)
        currentTabLayout = tabLayoutName[0]
        setRecyclerView()
    }

    override fun onResume() {
        super.onResume()

        recyclerViewEvent()
        editTextEvent(binding.edtSearch)
        tabLayoutEvent(binding.tab)
        binding.searchFragment.setOnClickListener {
            hideKeyboardEvent()
        }
    }

    private fun setRecyclerView() {
        adapter = RecentViewAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)

        val arr = getRecentSearchWord() ?: return
        notifyDataSetChanged(arr)
    }

    private fun notifyDataSetChanged(arr: ArrayList<String>) {
        adapter.list = arr
        adapter.notifyDataSetChanged()
    }

    private fun getRecentSearchWord(): ArrayList<String>? {
        val savedData = preference.getString(P_KEY, "")
        if (savedData.isNullOrEmpty())
            return null

        val arrJson = JSONArray(savedData)
        val arr = ArrayList<String>()

        for (i in 0 until arrJson.length())
            arr.add(arrJson.optString(i))

        return arr
    }

    private fun recyclerViewEvent() {
        adapter.setOnItemClickListener(object : RecentViewAdapter.OnItemClickListener {
            override fun onItemClick(word: String, position: Int) {
                binding.edtSearch.setText(word)
            }
            override fun onRemoveClick(position: Int) {
                removeSharedPreference(position)
            }
        })
    }

    private fun removeSharedPreference(position: Int) {
        val arr = getRecentSearchWord()?: return
        arr.removeAt(position)
        notifyDataSetChanged(arr)
        saveSharedPreference(arr)
    }

    private fun saveSharedPreference(arr: ArrayList<String>) {
        val jsonArr = JSONArray()
        for (i in arr)
            jsonArr.put(i)
        val result = jsonArr.toString()

        with(preference.edit()) {
            putString(P_KEY, result)
            commit()
        }
    }
    private fun editTextEvent(editText : EditText) {
        // 엔터키 이벤트
        editText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event?.action == KeyEvent.ACTION_DOWN) {
                    if (editText.text.length < 2)
                        NoticeDialog(requireContext(), R.layout.dialog_notice)
                    else {
                        val input = editText.text.toString()
                        val value = if (currentTabLayout == resources.getString(R.string.falName))
                            ListFragmentSafeArgs(ParameterType.FAL_NAME, input, null)
                        else
                            ListFragmentSafeArgs(ParameterType.KEY_WORD, input, null)
                        saveRecentSearchWord(input)
                        hideKeyboardEvent()
                        navController.navigate(SearchFragmentDirections.actionSearchFragmentToListFragment(value))
                    }
                    return true
                }
                return false
            }
        })
        // X버튼 visible 이벤트
        editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnRemove.visibility = if(s.isNullOrEmpty()) View.GONE else View.VISIBLE
                if (s?.length!! < 2)
                    binding.edtSearch.setTextColor(resources.getColor(R.color.light_gray))
                else
                    binding.edtSearch.setTextColor(resources.getColor(R.color.black))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun afterTextChanged(s: Editable?) { }
        })
        // 내용물 삭제 이벤트
        binding.btnRemove.setOnClickListener {
            editText.text = null
        }
    }
    // 검색어 저장
    private fun saveRecentSearchWord(input: String) {
        val arr = getRecentSearchWord() ?: ArrayList()

        if (arr.contains(input))
            arr.remove(input)

        arr.add(0, input)
        saveSharedPreference(arr)
    }
    // 상단 탭 힌트 변경
    private fun tabLayoutEvent(tabLayout: TabLayout) {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.edtSearch.hint = when (tabLayoutName[tab!!.position]) {
                    resources.getString(R.string.falName) -> resources.getString(R.string.falHint)
                    resources.getString(R.string.keyWord)-> resources.getString(R.string.keyWordHint)
                    else -> ""
                }
                currentTabLayout = tabLayoutName[tab.position]
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }
            override fun onTabReselected(tab: TabLayout.Tab?) { }
        })
    }

    // 화면 터치 시 키패드 사라짐
    private fun hideKeyboardEvent() {
        val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (binding.edtSearch.hasFocus())
            inputManager.hideSoftInputFromWindow(activity?.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}