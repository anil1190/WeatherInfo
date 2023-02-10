package com.example.collabratest.modals

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.collabratest.uitls.Constants.USER_TABLE

@Entity(tableName = USER_TABLE)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId : Int = 0,
    @ColumnInfo(name = "name")
    val userName : String = "",
    @ColumnInfo(name = "email")
    val userEmail : String = "",
    @ColumnInfo(name = "password")
    val userPassword : String = ""
)
