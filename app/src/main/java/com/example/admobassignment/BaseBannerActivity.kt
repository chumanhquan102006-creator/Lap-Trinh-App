package com.example.admobassignment

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

/** Each screen owns one test banner fixed at the very bottom of its layout. */
abstract class BaseBannerActivity : Activity() {
    companion object {
        private const val TEST_BANNER_ID = "ca-app-pub-3940256099942544/9214589741"
    }

    private var banner: AdView? = null
    private val handler = Handler(Looper.getMainLooper())
    private val statusTicker = object : Runnable {
        override fun run() {
            findViewById<TextView>(R.id.adStatus)?.text = DemoAdsManager.status(this@BaseBannerActivity)
            handler.postDelayed(this, 1000L)
        }
    }

    protected fun setupBanner() {
        val container = findViewById<FrameLayout>(R.id.bannerContainer)
        val adView = AdView(this)
        banner = adView
        val screenWidthDp = (resources.displayMetrics.widthPixels /
            resources.displayMetrics.density).toInt().coerceAtLeast(1)
        adView.setAdSize(AdSize.getLargeAnchoredAdaptiveBannerAdSize(this, screenWidthDp))
        adView.adUnitId = TEST_BANNER_ID
        container.addView(adView)
        adView.loadAd(AdRequest.Builder().build())
    }

    override fun onResume() {
        super.onResume()
        banner?.resume()
        handler.post(statusTicker)
    }

    override fun onPause() {
        handler.removeCallbacks(statusTicker)
        banner?.pause()
        super.onPause()
    }

    override fun onDestroy() {
        banner?.destroy()
        banner = null
        super.onDestroy()
    }
}
