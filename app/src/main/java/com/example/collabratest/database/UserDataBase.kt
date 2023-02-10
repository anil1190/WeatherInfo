package com.example.collabratest.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.collabratest.modals.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDataBase : RoomDatabase(){
    abstract fun userDao() : UserDao
}