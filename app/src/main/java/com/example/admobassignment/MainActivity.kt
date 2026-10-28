package com.example.admobassignment

import android.content.Intent
import android.os.Bundle
import android.widget.Button

class MainActivity : BaseBannerActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBanner()
        DemoAdsManager.preload(this)
        findViewById<Button>(R.id.goNextButton).setOnClickListener {
            DemoAdsManager.navigateWithAd(this) {
                startActivity(Intent(this, SecondActivity::class.java))
            }
        }
    }
}
