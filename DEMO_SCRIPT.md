# Video demo AdMob (3–4 phút)

1. **0:00–0:25**: Giới thiệu đề bài, giao diện 2 màn hình, source code độc lập.
2. **0:25–1:00**: Chạy Màn hình 1, chỉ Banner test cố định ở cuối màn hình. Chờ dòng 'Interstitial đã sẵn sàng'.
3. **1:00–1:40**: Nhấn 'Sang màn hình 2', Interstitial hiện ra, chỉ sau khi nhấn X mới thấy Màn hình 2. Chỉ banner dưới Màn hình 2.
4. **1:40–2:10**: Nhấn 'Về màn hình 1' khi chưa đủ 3 phút, chuyển ngay không hiện Interstitial.
5. **2:10–3:15**: Mở `DemoAdsManager.kt`: giải thích timestamp và `INTERVAL_MS = 3 * 60 * 1000L`, `onAdDismissedFullScreenContent` mới gọi `action()`. Mở `BaseBannerActivity.kt` chỉ `setupBanner`.
6. **3:15–3:40**: Mở AndroidManifest và `AdsDemoApp.kt` để giải thích cấu hình và MobileAds initialize. Kết thúc và nộp link GitHub.

Ghi chú: Nếu muốn chứng minh đủ 3 phút, quay hai đoạn riêng: ngay sau lần đầu rồi sau ít nhất 3 phút. Không tự quảng cáo đã hiển thị nếu SDK chưa tải được.
