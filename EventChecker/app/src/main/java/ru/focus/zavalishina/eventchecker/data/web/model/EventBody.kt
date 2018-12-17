package ru.focus.zavalishina.eventchecker.data.web.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EventBody {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("presentationCities")
    @Expose
    var cities: List<CityBody>? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("format")
    @Expose
    var format: Int? = null

    @SerializedName("presentationDate")
    @Expose
    var date: DateBody? = null

    @SerializedName("cardImage")
    @Expose
    var cardImage: String? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("iconStatus")
    @Expose
    var iconStatus: String? = null

    @SerializedName("eventFormat")
    @Expose
    var eventFormat: String? = null

    @SerializedName("eventFormatEng")
    @Expose
    var eventFormatEng: String? = null
}