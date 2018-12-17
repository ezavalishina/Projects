package ru.focus.zavalishina.eventchecker.data.web.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ConfirmationResponse {
    @SerializedName("result")
    @Expose
    var result: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}