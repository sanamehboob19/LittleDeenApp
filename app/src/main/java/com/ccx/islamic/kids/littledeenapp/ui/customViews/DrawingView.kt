package com.ccx.islamic.kids.littledeenapp.ui.customViews


import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.Stack
import kotlin.math.abs
import kotlin.math.hypot



class DrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    enum class ToolMode {
        PENCIL,
        BRUSH,
        GLOW,
        LINE,
        RECTANGLE,
        CIRCLE,
        ERASER
    }

    var currentTool: ToolMode = ToolMode.PENCIL
    var currentColor: Int = Color.parseColor("#00F0FF")
        set(value) {
            field = value
            updatePaintColor()
        }

    var currentStrokeWidth: Float = 20f

    // Toggle: true for Neon Pad (White laser core + intense neon bloom)
    var isNeonCoreEnabled: Boolean = false

    private var canvasBackgroundColor: Int = Color.WHITE
    private var canvasBitmap: Bitmap? = null
    private var drawCanvas: Canvas? = null

    // Main colored glow paint
    private val drawPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    // Inner bright white laser core for neon effect
    private val innerLaserPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        color = Color.WHITE
    }

    private val canvasPaint = Paint(Paint.DITHER_FLAG)
    private val drawPath = Path()
    private var startX = 0f
    private var startY = 0f
    private var currentX = 0f
    private var currentY = 0f

    private val undoStack = Stack<Bitmap>()
    private val redoStack = Stack<Bitmap>()
    private val maxUndoSteps = 15

    init {
        // Software layer required for BlurMaskFilter glow effects on Canvas
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        updatePaintColor()
    }

    private fun updatePaintColor() {
        drawPaint.color = currentColor
    }

    fun setCanvasBackgroundColor(color: Int) {
        canvasBackgroundColor = color
        resetCanvas()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w <= 0 || h <= 0) return

        if (canvasBitmap == null) {
            initBitmapCanvas(w, h)
        } else {
            val newBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            val newCanvas = Canvas(newBitmap)
            newCanvas.drawColor(canvasBackgroundColor)

            val srcRect = Rect(0, 0, canvasBitmap!!.width, canvasBitmap!!.height)
            val dstRect = Rect(0, 0, w, h)
            newCanvas.drawBitmap(canvasBitmap!!, srcRect, dstRect, canvasPaint)

            canvasBitmap?.recycle()
            canvasBitmap = newBitmap
            drawCanvas = newCanvas
        }
    }

    private fun initBitmapCanvas(w: Int, h: Int) {
        canvasBitmap?.recycle()
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap!!)
        drawCanvas?.drawColor(canvasBackgroundColor)
        clearStacks()
    }

    fun resetCanvas() {
        drawCanvas?.drawColor(canvasBackgroundColor)
        clearStacks()
        invalidate()
    }

    private fun clearStacks() {
        while (undoStack.isNotEmpty()) undoStack.pop().recycle()
        while (redoStack.isNotEmpty()) redoStack.pop().recycle()
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            canvasBitmap?.let { redoStack.push(it.copy(Bitmap.Config.ARGB_8888, true)) }
            val prevState = undoStack.pop()
            drawCanvas?.drawColor(canvasBackgroundColor)
            drawCanvas?.drawBitmap(prevState, 0f, 0f, null)
            prevState.recycle()
            invalidate()
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            canvasBitmap?.let { undoStack.push(it.copy(Bitmap.Config.ARGB_8888, true)) }
            val nextState = redoStack.pop()
            drawCanvas?.drawColor(canvasBackgroundColor)
            drawCanvas?.drawBitmap(nextState, 0f, 0f, null)
            nextState.recycle()
            invalidate()
        }
    }

    fun restoreBitmap(bitmap: Bitmap) {
        if (width <= 0 || height <= 0) {
            post { restoreBitmap(bitmap) }
            return
        }
        if (canvasBitmap == null) initBitmapCanvas(width, height)

        val newCanvas = Canvas(canvasBitmap!!)
        newCanvas.drawColor(canvasBackgroundColor)
        val srcRect = Rect(0, 0, bitmap.width, bitmap.height)
        val dstRect = Rect(0, 0, width, height)
        newCanvas.drawBitmap(bitmap, srcRect, dstRect, canvasPaint)
        clearStacks()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvasBitmap?.let { canvas.drawBitmap(it, 0f, 0f, canvasPaint) }

        configurePaint()

        // Live dragging preview
        when (currentTool) {
            ToolMode.PENCIL, ToolMode.BRUSH, ToolMode.GLOW, ToolMode.ERASER -> {
                canvas.drawPath(drawPath, drawPaint)
                if (isNeonCoreEnabled && currentTool != ToolMode.ERASER) {
                    canvas.drawPath(drawPath, innerLaserPaint)
                }
            }
            ToolMode.LINE -> {
                canvas.drawLine(startX, startY, currentX, currentY, drawPaint)
                if (isNeonCoreEnabled) canvas.drawLine(startX, startY, currentX, currentY, innerLaserPaint)
            }
            ToolMode.RECTANGLE -> {
                val l = minOf(startX, currentX)
                val t = minOf(startY, currentY)
                val r = maxOf(startX, currentX)
                val b = maxOf(startY, currentY)
                canvas.drawRect(l, t, r, b, drawPaint)
                if (isNeonCoreEnabled) canvas.drawRect(l, t, r, b, innerLaserPaint)
            }
            ToolMode.CIRCLE -> {
                val radius = hypot((currentX - startX).toDouble(), (currentY - startY).toDouble()).toFloat()
                canvas.drawCircle(startX, startY, radius, drawPaint)
                if (isNeonCoreEnabled) canvas.drawCircle(startX, startY, radius, innerLaserPaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                canvasBitmap?.let {
                    if (undoStack.size >= maxUndoSteps) undoStack.removeAt(0).recycle()
                    undoStack.push(it.copy(Bitmap.Config.ARGB_8888, true))
                }
                while (redoStack.isNotEmpty()) redoStack.pop().recycle()

                startX = x
                startY = y
                currentX = x
                currentY = y

                drawPath.reset()
                drawPath.moveTo(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                currentX = x
                currentY = y

                if (currentTool == ToolMode.PENCIL || currentTool == ToolMode.BRUSH || currentTool == ToolMode.GLOW || currentTool == ToolMode.ERASER) {
                    val dx = abs(x - startX)
                    val dy = abs(y - startY)
                    if (dx >= 4 || dy >= 4) {
                        drawPath.quadTo(startX, startY, (x + startX) / 2, (y + startY) / 2)
                        startX = x
                        startY = y
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                configurePaint()
                when (currentTool) {
                    ToolMode.PENCIL, ToolMode.BRUSH, ToolMode.GLOW, ToolMode.ERASER -> {
                        drawPath.lineTo(currentX, currentY)
                        drawCanvas?.drawPath(drawPath, drawPaint)
                        if (isNeonCoreEnabled && currentTool != ToolMode.ERASER) {
                            drawCanvas?.drawPath(drawPath, innerLaserPaint)
                        }
                        drawPath.reset()
                    }
                    ToolMode.LINE -> {
                        drawCanvas?.drawLine(startX, startY, currentX, currentY, drawPaint)
                        if (isNeonCoreEnabled) drawCanvas?.drawLine(startX, startY, currentX, currentY, innerLaserPaint)
                    }
                    ToolMode.RECTANGLE -> {
                        val l = minOf(startX, currentX)
                        val t = minOf(startY, currentY)
                        val r = maxOf(startX, currentX)
                        val b = maxOf(startY, currentY)
                        drawCanvas?.drawRect(l, t, r, b, drawPaint)
                        if (isNeonCoreEnabled) drawCanvas?.drawRect(l, t, r, b, innerLaserPaint)
                    }
                    ToolMode.CIRCLE -> {
                        val radius = hypot((currentX - startX).toDouble(), (currentY - startY).toDouble()).toFloat()
                        drawCanvas?.drawCircle(startX, startY, radius, drawPaint)
                        if (isNeonCoreEnabled) drawCanvas?.drawCircle(startX, startY, radius, innerLaserPaint)
                    }
                }
                invalidate()
            }
        }
        return true
    }

    private fun configurePaint() {
        drawPaint.strokeWidth = currentStrokeWidth
        drawPaint.style = Paint.Style.STROKE
        drawPaint.strokeJoin = Paint.Join.ROUND
        drawPaint.strokeCap = if (currentTool == ToolMode.RECTANGLE) Paint.Cap.SQUARE else Paint.Cap.ROUND

        if (currentTool == ToolMode.ERASER) {
            drawPaint.color = canvasBackgroundColor
            drawPaint.maskFilter = null
        } else {
            drawPaint.color = currentColor
            if (isNeonCoreEnabled) {
                // Intense outer neon blur bloom
                drawPaint.maskFilter = BlurMaskFilter(currentStrokeWidth * 0.75f, BlurMaskFilter.Blur.SOLID)

                // Thin, crisp white laser core
                innerLaserPaint.strokeWidth = currentStrokeWidth * 0.28f
                innerLaserPaint.strokeCap = drawPaint.strokeCap
                innerLaserPaint.strokeJoin = drawPaint.strokeJoin
            } else {
                drawPaint.maskFilter = null
            }
        }
    }

    fun getCanvasBitmap(): Bitmap? {
        return canvasBitmap?.copy(Bitmap.Config.ARGB_8888, false)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        canvasBitmap?.recycle()
        clearStacks()
    }
}