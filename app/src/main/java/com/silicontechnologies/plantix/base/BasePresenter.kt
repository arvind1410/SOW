package com.silicontechnologies.plantix.base

interface BasePresenter<in V : BaseView> {

    fun attachView(view: V)

    fun detachView()

}