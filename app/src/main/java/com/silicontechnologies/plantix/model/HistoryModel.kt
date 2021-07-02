package com.silicontechnologies.plantix.model

import io.realm.RealmModel
import java.util.Date

class HistoryModel : RealmModel {

    var date: Date? = null
    var uri: String? = null
    var isNormal: Boolean = false
    var isValidImage: Boolean = false;
    var imageName: String? = null

}