package com.drag0n.weatherforecastkmp.presentation.others

import platform.WebKit.WKWebView

actual fun configureNativeSettings(nativeView: Any) {
    if (nativeView is WKWebView) {
        // На iOS свайпы в WKWebView обычно работают "из коробки" лучше,
        // но можно явно отключить задержку распознавания для скорости
        nativeView.scrollView.setDelaysContentTouches(false)
        nativeView.allowsBackForwardNavigationGestures = false // Чтобы свайп не листал историю браузера
    }
}