package com.example.sup_fitness.db

import android.widget.NumberPicker
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userinfo")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "weight") val weight: NumberPicker,
    @ColumnInfo(name = "date") val date: String,
)