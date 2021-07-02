package com.silicontechnologies.plantix.mapper

import com.silicontechnologies.plantix.entity.History
import com.silicontechnologies.plantix.model.HistoryModel

class HistoryMapper {

    companion object {
        fun converModelToEntity(model: HistoryModel): History {
            val history = History()
            history.date = model.date
            history.imageName = model.imageName
            history.isNormal = model.isNormal
            history.isValidImage = model.isValidImage
            history.uri = model.uri
            return History()
        }

        fun convertEntityToModel(entity: History): HistoryModel {
            val model = HistoryModel()
            model.date = entity.date
            model.imageName = entity.imageName
            model.isNormal = entity.isNormal
            model.isValidImage = entity.isValidImage
            model.uri = entity.uri
            return model
        }

        fun convertModelsToEntity(models: List<HistoryModel>): List<History> {
            var entities = ArrayList<History>()
            for (item in models) {
                entities.add(converModelToEntity(item))
            }
            return entities
        }
    }

}