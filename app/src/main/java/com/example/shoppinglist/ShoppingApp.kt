package com.example.shoppinglist

import android.app.Application
import androidx.room.Room
import com.example.shoppinglist.data.db.ShoppingDatabase
import com.example.shoppinglist.data.repository.ShoppingRepository

class ShoppingApp : Application() {
    companion object {
        lateinit var database: ShoppingDatabase
            private set

        lateinit var repository: ShoppingRepository
            private set
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            ShoppingDatabase::class.java,
            "ShoppingDB.db"
        ).build()
        repository = ShoppingRepository(database)
    }
}