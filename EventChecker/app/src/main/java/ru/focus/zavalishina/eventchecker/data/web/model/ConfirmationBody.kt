package ru.focus.zavalishina.eventchecker.data.web.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ConfirmationBody(
        @SerializedName("id")
        @Expose
        var id: Int? = null,

        @SerializedName("isVisited")
        @Expose
        var isVisited: Boolean? = null,

        @SerializedName("visitedDate")
        @Expose
        var visitedDate: String? = null
)
