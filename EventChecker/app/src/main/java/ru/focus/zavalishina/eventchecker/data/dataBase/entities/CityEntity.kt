package ru.focus.zavalishina.eventchecker.data.dataBase.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity
data class CityEntity(
        @PrimaryKey
        @SerializedName("id")
        @Expose
        val id: Int?,

        @SerializedName("nameRus")
        @Expose
        val nameRus: String?,

        @SerializedName("nameEng")
        @Expose
        val nameEng: String?,

        @SerializedName("icon")
        @Expose
        val icon: String?,

        @SerializedName("isActive")
        @Expose
        val isActive: Boolean?
)