package com.example.admobassignment

import android.os.Bundle
import android.widget.Button

class SecondActivity : BaseBannerActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        setupBanner()
        findViewById<Button>(R.id.goBackButton).setOnClickListener {
            DemoAdsManager.navigateWithAd(this) { finish() }
        }
    }
}
