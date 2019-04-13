package com.projek.putrautama.klikeatmerchant

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.FirebaseApp

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        FirebaseApp.initializeApp(this)
        startAnimation()
    }

    lateinit var splashThread: Thread

    private fun startAnimation() {
        splashThread = object : Thread() {
            override fun run() {
                try {
                    var waited = 0
                    while (waited < 2500) {
                        sleep(100)
                        waited += 100
                    }
                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    startActivity(intent)
                    this@SplashScreenActivity.finish()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    this@SplashScreenActivity.finish()
                }
            }
        }
        splashThread.start()
    }
}
