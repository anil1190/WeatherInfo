package com.example.collabratest.database

import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.collabratest.modals.UserEntity
import com.example.collabratest.uitls.Constants.USER_TABLE

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(userEntity: UserEntity)

    @Query("SELECT * FROM $USER_TABLE ORDER BY userId DESC")
    fun getAllUsers() : MutableList<UserEntity>

}