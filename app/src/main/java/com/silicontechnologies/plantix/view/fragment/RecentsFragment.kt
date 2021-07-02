package com.silicontechnologies.plantix.view.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import com.silicontechnologies.plantix.R
import com.silicontechnologies.plantix.base.BaseFragment
import com.silicontechnologies.plantix.view.HistoryAdapter
import kotlinx.android.synthetic.main.fragment_recents.rv_recent_list

class RecentsFragment : BaseFragment() {

    private var recentAdapter: HistoryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recentAdapter = HistoryAdapter(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_recents, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_recent_list.layoutManager = LinearLayoutManager(context)
        rv_recent_list.hasFixedSize()
        rv_recent_list.adapter = recentAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_nearbylocation, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}