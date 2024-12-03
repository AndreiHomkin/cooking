package com.example.cook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var adapter: ItemsAdapter
    private lateinit var itemsArrayList: ArrayList<Item>
    private lateinit var recycler: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var dbHelper: DbHelper
    private lateinit var filteredList: ArrayList<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = view.findViewById(R.id.recyclerView)
        searchView = view.findViewById(R.id.searchView)

        dbHelper = DbHelper(requireContext(), null)
        itemsArrayList = ArrayList(dbHelper.getAllFoods())
        filteredList = ArrayList(itemsArrayList)

        val layoutManager = GridLayoutManager(context, 2)
        recycler = view.findViewById(R.id.recyclerView)
        recycler.layoutManager = layoutManager
        recycler.hasFixedSize()
        adapter = ItemsAdapter(filteredList)
        recycler.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }
    private fun filterList(query: String?) {
        val searchQuery = query?.lowercase() ?: ""
        filteredList.clear()

        if (searchQuery.isEmpty()) {
            filteredList.addAll(itemsArrayList)
        } else {
            for (item in itemsArrayList) {
                if (item.name.lowercase().contains(searchQuery)) {
                    filteredList.add(item)
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}