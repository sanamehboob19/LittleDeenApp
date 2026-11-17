package com.ccx.islamic.kids.littledeenapp.ui.customViews


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.ccx.islamic.kids.littledeenapp.model.PixelArtModel

class PixelGridView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var onCompleted: (() -> Unit)? = null
    var onWrongColor: (() -> Unit)? = null

    var isInteractive: Boolean = true
    var showHintDots: Boolean = false

    private var currentModel: PixelArtModel? = null
    private var gridSize = 10
    private var cellColors = IntArray(100) { Color.WHITE }
    private var paletteColors = listOf<Int>()

    var currentColor: Int = Color.parseColor("#FFD600")

    private var isCompleted = false
    private var lastTouchedCell = -1

    private val cellPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val gridLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f
        color = Color.parseColor("#EEEEEE")
    }

    private val hintDotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val cellRect = RectF()

    fun loadModel(model: PixelArtModel, isTargetPreview: Boolean = false) {
        currentModel = model
        gridSize = model.gridSize
        isInteractive = !isTargetPreview
        isCompleted = false
        lastTouchedCell = -1

        paletteColors = model.paletteColorsHex.map { Color.parseColor(it) }

        if (isTargetPreview) {
            cellColors = IntArray(model.matrix.size) { index ->
                val colorIdx = model.matrix[index]
                paletteColors.getOrElse(colorIdx) { Color.WHITE }
            }
        } else {
            cellColors = IntArray(model.matrix.size) { Color.WHITE }
            currentColor = paletteColors.getOrElse(1) { Color.parseColor("#FF1744") }
        }

        invalidate()
    }

    fun resetGrid() {
        if (!isInteractive) return
        cellColors.fill(Color.WHITE)
        isCompleted = false
        lastTouchedCell = -1
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val model = currentModel ?: return

        val usableSize = width.coerceAtMost(height).toFloat()
        val cellSize = usableSize / gridSize
        val startX = (width - usableSize) / 2f
        val startY = (height - usableSize) / 2f

        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                val index = row * gridSize + col
                val left = startX + (col * cellSize)
                val top = startY + (row * cellSize)
                val right = left + cellSize
                val bottom = top + cellSize

                cellRect.set(left, top, right, bottom)

                cellPaint.color = cellColors[index]
                canvas.drawRect(cellRect, cellPaint)
                canvas.drawRect(cellRect, gridLinePaint)

                if (isInteractive && showHintDots && !isCompleted && cellColors[index] == Color.WHITE) {
                    val targetColorIdx = model.matrix[index]
                    val targetColor = paletteColors.getOrElse(targetColorIdx) { Color.WHITE }

                    if (targetColor != Color.WHITE) {
                        hintDotPaint.color = targetColor
                        canvas.drawCircle(cellRect.centerX(), cellRect.centerY(), cellSize * 0.16f, hintDotPaint)
                    }
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isInteractive || isCompleted || currentModel == null) return false

        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                handleTouchAt(event.x, event.y)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                lastTouchedCell = -1
            }
        }
        return true
    }

    private fun handleTouchAt(touchX: Float, touchY: Float) {
        val usableSize = width.coerceAtMost(height).toFloat()
        val cellSize = usableSize / gridSize
        val startX = (width - usableSize) / 2f
        val startY = (height - usableSize) / 2f

        if (touchX < startX || touchX > startX + usableSize || touchY < startY || touchY > startY + usableSize) return

        val col = ((touchX - startX) / cellSize).toInt().coerceIn(0, gridSize - 1)
        val row = ((touchY - startY) / cellSize).toInt().coerceIn(0, gridSize - 1)
        val index = row * gridSize + col

        if (index != lastTouchedCell) {
            lastTouchedCell = index

            val model = currentModel ?: return
            val targetColorIdx = model.matrix[index]
            val expectedColor = paletteColors.getOrElse(targetColorIdx) { Color.WHITE }

            if (currentColor != expectedColor) {
                onWrongColor?.invoke()
                return
            }

            cellColors[index] = currentColor
            invalidate()
            checkCompletion()
        }
    }

    private fun checkCompletion() {
        val model = currentModel ?: return
        for (i in cellColors.indices) {
            val targetColorIdx = model.matrix[i]
            val expectedColor = paletteColors.getOrElse(targetColorIdx) { Color.WHITE }
            if (cellColors[i] != expectedColor) return
        }

        isCompleted = true
        invalidate()
        onCompleted?.invoke()
    }
}