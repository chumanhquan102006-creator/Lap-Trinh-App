package com.example.admobassignment

import android.app.Application
import android.os.Handler
import android.os.Looper
import com.google.android.gms.ads.MobileAds

class AdsDemoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // App starts Mobile Ads SDK once; only Google test ads are used.
        MobileAds.initialize(this) {
            Handler(Looper.getMainLooper()).post {
                DemoAdsManager.preload(this)
            }
        }
    }
}
