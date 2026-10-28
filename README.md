# AdMob Assignment Demo (Kotlin, Android)

Independent classroom assignment. It is **not** part of FoodManager.

## What is implemented
- **Screen 1 / Screen 2** (`MainActivity`, `SecondActivity`).
- A Google Mobile Ads **test banner anchored at the bottom of each screen**.
- Press a navigation button to show a **test interstitial** when eligible, then navigate **only after the user dismisses it**.
- A shared global cooldown: **at most 1 interstitial per 180 seconds**. The last-show time is stored in SharedPreferences, so restarting the app does not reset the timer.
- If there is no network or ad load/show fails, navigation still works. The first click can wait up to 7 seconds for the ad to be ready.

## How to open (Windows)

1. Extract the ZIP.
2. In PowerShell, enter the project folder and run `powershell -ExecutionPolicy Bypass -File .\setup-wrapper.ps1` once. This downloads `gradle-wrapper.jar` from the official Gradle 8.11.1 GitHub source. (The binary is not included in this source ZIP.)
3. Android Studio → **File → Open** → select the `AdMobAssignmentDemo` folder (with `settings.gradle.kts`).
4. Wait for Gradle sync and select **app** → run with a Google Play emulator or physical Android phone with Google Play services, connected to Internet.
5. On Screen 1, wait until the status says `Interstitial đã sẵn sàng`, then tap the navigation button.
6. Dismiss the interstitial (`×`) and see Screen 2. Tap back: **within 3 minutes, no interstitial** is allowed, the transition is immediate.
7. Wait 3 minutes from actual ad display and navigate again: the next ad can appear.

## Source file guide
- `AdsDemoApp.kt`: initializes Google Mobile Ads SDK and preloads interstitial.
- `DemoAdsManager.kt`: preloading, onAdDismissed callback, navigation, shared 3-minute cooldown, network fallback.
- `BaseBannerActivity.kt`: AdView at the bottom on both screens and AdView lifecycle.
- `MainActivity.kt`, `SecondActivity.kt`: navigation button handling.
- `res/layout/activity_main.xml`, `activity_second.xml`: both screen layouts and ad containers.
- `AndroidManifest.xml`: **test** application ID.

## Official TEST IDs — do not use for monetization
- App ID: `ca-app-pub-3940256099942544~3347511713`
- Banner: `ca-app-pub-3940256099942544/9214589741`
- Interstitial: `ca-app-pub-3940256099942544/1033173712`

Official docs:
- https://developers.google.com/admob/android/quick-start
- https://developers.google.com/admob/android/banner
- https://developers.google.com/admob/android/interstitial

When publishing a real app, replace test IDs with your own approved units and implement any required consent flow. **This project is intended for classroom demonstrations only.**

## Project versions
- Kotlin 2.1.20; Android Gradle Plugin 8.9.2; Gradle 8.11.1; Mobile Ads SDK 25.5.0.
- `minSdk 24`, `compileSdk 35`, `targetSdk 34`, JDK 17.

## Submission
One representative group member submits the GitHub repository URL and a short narrated screen recording showing both banners, the interstitial, the close-to-navigate behavior, and the 3-minute cooldown.
