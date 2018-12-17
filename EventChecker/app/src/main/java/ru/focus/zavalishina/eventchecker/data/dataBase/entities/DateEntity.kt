package ru.focus.zavalishina.eventchecker.data.dataBase.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity
data class DateEntity(
        @SerializedName("start")
        @Expose
        val start: String?,

        @SerializedName("end")
        @Expose
        val end: String?
)
{
    @PrimaryKey(autoGenerate = true)
    var dateId : Long? = null
}