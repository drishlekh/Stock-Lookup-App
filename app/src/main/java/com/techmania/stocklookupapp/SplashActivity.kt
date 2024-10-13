package com.techmania.stocklookupapp

//splash screen code for launcher activity of the app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        val welcomeText: TextView = findViewById(R.id.welcomeText)


        welcomeText.alpha = 1f

        //fade-out animation
        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.duration = 1000 // 1 second for fade-out
        fadeOut.startOffset = 2500 // Start fading out after 2.5 seconds
        fadeOut.fillAfter = true

        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                // starting MainActivity after the animation ends
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        // starting the animation
        welcomeText.startAnimation(fadeOut)
    }
}