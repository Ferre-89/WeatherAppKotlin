package com.example.weatherappkotlin.domain.commands

import com.example.weatherappkotlin.data.ForecastRequest
import com.example.weatherappkotlin.domain.mappers.ForecastDataMapper
import com.example.weatherappkotlin.domain.model.ForecastList

class RequestForecastCommand(private val zipCode: String) : Command<ForecastList> {
    override fun execute(): ForecastList {
        val forecastRequest = ForecastRequest(zipCode)
        return ForecastDataMapper().convertFromDataModel(forecastRequest.execute())
    }
}

