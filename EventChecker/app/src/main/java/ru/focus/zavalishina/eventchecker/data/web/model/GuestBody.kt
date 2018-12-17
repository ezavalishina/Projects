package ru.focus.zavalishina.eventchecker.data.web.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GuestBody
(
        @SerializedName("id")
        @Expose
        var id: Int? = null,

        @SerializedName("phone")
        @Expose
        var phone: String? = null,

        @SerializedName("city")
        @Expose
        var city: String? = null,

        @SerializedName("company")
        @Expose
        var company: String? = null,

        @SerializedName("position")
        @Expose
        var position: String? = null,

        @SerializedName("addition")
        @Expose
        var addition: String? = null,

        @SerializedName("registeredDate")
        @Expose
        var registeredDate: String? = null,

        @SerializedName("agreedByManager")
        @Expose
        var agreedByManager: String? = null,

        @SerializedName("lastName")
        @Expose
        var lastName: String? = null,

        @SerializedName("firstName")
        @Expose
        var firstName: String? = null,

        @SerializedName("patronymic")
        @Expose
        var patronymic: String? = null,

        @SerializedName("email")
        @Expose
        var email: String? = null
)