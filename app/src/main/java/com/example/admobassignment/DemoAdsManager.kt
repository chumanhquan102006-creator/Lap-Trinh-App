package com.example.admobassignment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

/**
 * A single shared interstitial for BOTH screens.
 * It can show at most once every 180 seconds, even if the app is restarted.
 * Navigation occurs only after the fullscreen ad is dismissed or cannot be shown.
 */
object DemoAdsManager {
    private const val TEST_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712"
    private const val PREFS = "admob_demo_frequency"
    private const val LAST_SHOWN = "last_interstitial_timestamp"
    private const val INTERVAL_MS = 3 * 60 * 1000L
    private const val WAIT_FOR_AD_MS = 7_000L

    private val handler = Handler(Looper.getMainLooper())
    private var ad: InterstitialAd? = null
    private var isLoading = false
    private var isShowing = false
    private var pending: PendingNavigation? = null

    private class PendingNavigation(
        val activity: Activity,
        val action: () -> Unit,
        val dialog: AlertDialog,
        val timeout: Runnable
    )

    fun preload(context: Context) {
        if (ad != null || isLoading) return
        isLoading = true
        InterstitialAd.load(
            context.applicationContext,
            TEST_INTERSTITIAL_ID,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(loaded: InterstitialAd) {
                    isLoading = false
                    ad = loaded
                    val request = pending ?: return
                    pending = null
                    handler.removeCallbacks(request.timeout)
                    request.dialog.dismiss()
                    if (!request.activity.isFinishing && !request.activity.isDestroyed) {
                        show(request.activity, request.action)
                    }
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    isLoading = false
                    ad = null
                    val request = pending ?: return
                    pending = null
                    handler.removeCallbacks(request.timeout)
                    request.dialog.dismiss()
                    if (!request.activity.isFinishing && !request.activity.isDestroyed) {
                        request.action()  // Never trap users when test ads fail to load.
                    }
                }
            }
        )
    }

    private fun timeRemaining(context: Context): Long {
        val last = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getLong(LAST_SHOWN, 0L)
        if (last == 0L) return 0L
        val elapsed = System.currentTimeMillis() - last
        return (INTERVAL_MS - elapsed).coerceAtLeast(0L)
    }

    fun status(context: Context): String {
        val left = timeRemaining(context)
        return if (left > 0L) {
            val seconds = (left + 999L) / 1000L
            "Đang giới hạn quảng cáo: còn ${seconds / 60} phút ${seconds % 60} giây"
        } else if (ad != null) {
            "Interstitial đã sẵn sàng — nhấn chuyển trang để xem quảng cáo"
        } else {
            "Đang tải Interstitial test từ Google (cần Internet)…"
        }
    }

    fun navigateWithAd(activity: Activity, action: () -> Unit) {
        // Double-taps cannot create multiple navigations while an ad is open/loading.
        if (isShowing || pending != null) return
        if (timeRemaining(activity) > 0L) {
            action() // Still within the three-minute cooldown.
            return
        }

        if (ad != null) {
            show(activity, action)
            return
        }

        // On the first launch, give the test ad a short chance to load.
        // When the network is unavailable, navigation still proceeds after timeout.
        val dialog = AlertDialog.Builder(activity)
            .setMessage("Đang chuẩn bị quảng cáo thử nghiệm…")
            .setCancelable(false)
            .create()
        dialog.show()
        lateinit var request: PendingNavigation
        val timeout = Runnable {
            if (pending === request) {
                pending = null
                dialog.dismiss()
                if (!activity.isFinishing && !activity.isDestroyed) action()
            }
        }
        request = PendingNavigation(activity, action, dialog, timeout)
        pending = request
        handler.postDelayed(timeout, WAIT_FOR_AD_MS)
        preload(activity)
    }

    private fun show(activity: Activity, action: () -> Unit) {
        val loaded = ad ?: run { action(); return }
        if (isShowing) return
        isShowing = true
        ad = null // An interstitial is single-use.
        var completed = false
        fun complete() {
            if (completed) return
            completed = true
            isShowing = false
            preload(activity.applicationContext)
            if (!activity.isFinishing && !activity.isDestroyed) action()
        }
        loaded.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                // Persist for a true max 1 ad / 3 minutes, across app restarts.
                activity.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                    .edit().putLong(LAST_SHOWN, System.currentTimeMillis()).apply()
            }

            override fun onAdDismissedFullScreenContent() {
                complete() // Only now may screen navigation take place.
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                complete() // Graceful fallback if an ad cannot be shown.
            }
        }
        loaded.show(activity)
    }
}
