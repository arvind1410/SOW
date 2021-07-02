package com.silicontechnologies.plantix.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.silicontechnologies.plantix.R
import com.silicontechnologies.plantix.base.BaseFragment
import com.silicontechnologies.plantix.entity.History
import com.silicontechnologies.plantix.mapper.HistoryMapper
import com.silicontechnologies.plantix.model.HistoryModel
import com.silicontechnologies.plantix.view.HistoryAdapter
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_recents.rv_recent_list


class HistoryFragment : BaseFragment() {

    private var historyList: List<History> = ArrayList()
    private var historyAdapter: HistoryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        historyAdapter = HistoryAdapter(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        rv_recent_list.adapter = historyAdapter
    }

    fun getHistoryFromCache() {
        var realm = Realm.getDefaultInstance()
        var models = realm.where(HistoryModel::class.java).findAll();
        var entities = HistoryMapper.convertModelsToEntity(models)
        realm.close()
        historyList = entities
    }

}