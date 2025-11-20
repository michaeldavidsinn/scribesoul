package com.example.scribesoul.utils

import android.graphics.BlurMaskFilter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp

fun Modifier.softShadow(
    color: Color = Color(0xFF000000),
    radius: Float = 40f,
    offsetY: Float = 6f,
    offsetX: Float = 0f,
    alpha: Float = 0.12f
) = this.drawBehind {
    val paint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        this.color = color.copy(alpha = alpha).toArgb()
        maskFilter = BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL)
    }

    drawIntoCanvas { canvas ->
        canvas.nativeCanvas.drawRoundRect(
            0f + offsetX,
            0f + offsetY,
            size.width + offsetX,
            size.height + offsetY,
            26.dp.toPx(), // same roundness as card
            26.dp.toPx(),
            paint
        )
    }
}