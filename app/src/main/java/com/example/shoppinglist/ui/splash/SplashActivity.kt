package com.example.shoppinglist.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.example.shoppinglist.R
import com.example.shoppinglist.ui.shoppingList.ShoppingActivity
import com.example.shoppinglist.utils.ThemeUtil

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Set default theme first thing
        ThemeUtil.applyTheme(ThemeUtil.MODE_SYSTEM)

        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display
        enableEdgeToEdge()

        // Set content view for splash activity
        setContentView(R.layout.activity_splash)

        // Make sure system bars are properly handled with edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Keep the splash screen visible for some time
        splashScreen.setKeepOnScreenCondition { true }

        // Navigate to main activity after delay
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, ShoppingActivity::class.java))
            finish()
        }, 2000) // 2 seconds delay
    }
}