package com.example.shoppinglist

import android.app.Application
import androidx.room.Room
import com.example.shoppinglist.data.db.ShoppingDatabase
import com.example.shoppinglist.data.repository.ShoppingRepository

class ShoppingApp : Application() {

    // Simple manual dependency injection instead of Kodein
    companion object {
        lateinit var database: ShoppingDatabase
            private set

        lateinit var repository: ShoppingRepository
            private set
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize database
        database = Room.databaseBuilder(
            applicationContext,
            ShoppingDatabase::class.java,
            "ShoppingDB.db"
        ).build()

        // Initialize repository
        repository = ShoppingRepository(database)

//        // Enable night mode based on system default
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}