package com.example.litelentanews.util

import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import java.net.URI

fun URI.buildCustomUri():String {
    return scheme+"://m."+host.replace("www.","")+path
}

fun newStaticLayout(
        source: CharSequence,
        paint: TextPaint,
        width: Int,
        alignment: Layout.Alignment,
        spacingmult: Float,
        spacingadd: Float,
        includepad: Boolean
): StaticLayout {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        StaticLayout.Builder.obtain(source, 0, source.length, paint, width).apply {
            setAlignment(alignment)
            setLineSpacing(spacingadd, spacingmult)
            setIncludePad(includepad)
        }.build()
    } else {
        @Suppress("DEPRECATION")
        (StaticLayout(source, paint, width, alignment, spacingmult, spacingadd, includepad))
    }
}