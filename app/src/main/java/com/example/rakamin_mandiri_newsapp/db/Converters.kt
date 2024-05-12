package com.example.rakamin_mandiri_newsapp.db

import androidx.room.TypeConverter
import com.example.rakamin_mandiri_newsapp.models.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String{
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source{
        return Source(name, name)
    }
}