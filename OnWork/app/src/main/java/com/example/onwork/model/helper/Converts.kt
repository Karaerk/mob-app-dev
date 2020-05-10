package com.example.onwork.model.helper

import androidx.room.TypeConverter
import com.example.onwork.model.DateFormatEnum
import java.util.*

class Converts {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromDateFormatEnum(value: Int): DateFormatEnum {
        return value.let { DateFormatEnum.values()[it] }
    }

    @TypeConverter
    fun dateFormatEnumToInt(dateFormatEnum: DateFormatEnum): Int {
        return dateFormatEnum.ordinal
    }
}