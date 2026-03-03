package com.drag0n.weatherforecastkmp.presentation.others

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.webkit.WebView

@SuppressLint("ClickableViewAccessibility")
actual fun configureNativeSettings(nativeView: Any) {
    if (nativeView is WebView) {
        nativeView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
        }

        nativeView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // ЖЕСТКАЯ БЛОКИРОВКА: запрещаем Drawer и Pager перехватывать это касание
                    v.parent?.requestDisallowInterceptTouchEvent(true)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // Возвращаем управление родителям
                    v.parent?.requestDisallowInterceptTouchEvent(false)
                }
            }
            // Возвращаем false, чтобы WebView сама обработала этот жест (зум/скролл)
            false
        }
    }
}