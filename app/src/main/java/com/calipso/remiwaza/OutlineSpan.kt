package com.calipso.remiwaza

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.ReplacementSpan

class OutlineSpan(
    private val strokeWidth: Float,
    private val strokeColor: Int,
    private val textColor: Int
) : ReplacementSpan() {

    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        return paint.measureText(text, start, end).toInt()
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val originalColor = paint.color
        val originalStyle = paint.style

        // Dibujar contorno
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth
        paint.color = strokeColor
        canvas.drawText(text, start, end, x, y.toFloat(), paint)

        // Dibujar texto
        paint.style = Paint.Style.FILL
        paint.color = textColor
        canvas.drawText(text, start, end, x, y.toFloat(), paint)

        // Restaurar color y estilo originales
        paint.color = originalColor
        paint.style = originalStyle
    }
}
