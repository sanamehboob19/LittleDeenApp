package com.ccx.islamic.kids.littledeenapp.ui.customViews


import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.PathParser
import com.ccx.islamic.kids.littledeenapp.model.UrduAlphabetModel
import com.ccx.islamic.kids.littledeenapp.data.UrduPathsData
import java.util.Random
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin




class UrduTracingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var onCompleted: (() -> Unit)? = null
    private var currentLetter: UrduAlphabetModel? = null

    private data class StrokeInfo(
        val path: Path,
        val measure: PathMeasure,
        val length: Float,
        val guideDots: List<PointF>,
        val isDot: Boolean // 🎯 True for 1-pixel dots (Nuqtas)
    )

    private val allStrokes = mutableListOf<StrokeInfo>()
    private val completedStrokesPath = Path()
    private val activeTracedSegment = Path()

    private var currentStrokeIndex = 0
    private var currentTracedLength = 0f
    private val avatarPos = floatArrayOf(0f, 0f)
    private var isCompleted = false
    private var isDragging = false

    private val tubeBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        color = Color.parseColor("#E0E0E0")
    }

    private val tubeWhiteCorePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        color = Color.WHITE
    }

    private val guideDotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#BDBDBD")
    }

    private val glitterFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    private val avatarBodyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#FFD600")
    }

    private val avatarBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        color = Color.parseColor("#FF6D00")
    }

    private val sparklePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private data class Sparkle(var x: Float, var y: Float, val vx: Float, val vy: Float, var alpha: Int, val size: Float, val color: Int)
    private val sparkles = mutableListOf<Sparkle>()
    private val random = Random()

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    fun loadLetter(model: UrduAlphabetModel) {
        currentLetter = model
        isCompleted = false
        isDragging = false
        currentStrokeIndex = 0
        currentTracedLength = 0f
        completedStrokesPath.reset()
        activeTracedSegment.reset()
        sparkles.clear()

        glitterFillPaint.color = Color.parseColor(model.glitterColorHex)

        if (width > 0 && height > 0) buildStrokes()
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0 && currentLetter != null) buildStrokes()
    }

    private fun buildStrokes() {
        val letter = currentLetter?.letter ?: return
        allStrokes.clear()
        completedStrokesPath.reset()
        activeTracedSegment.reset()

        val padX = width * 0.12f
        val padY = height * 0.12f
        val targetRect = RectF(padX, padY, width - padX, height - padY)
        val srcRect = RectF(0f, 0f, 100f, 100f)

        val matrix = Matrix()
        matrix.setRectToRect(srcRect, targetRect, Matrix.ScaleToFit.CENTER)

        val tubeWidth = (targetRect.width() * 0.18f).coerceIn(48f, 100f)
        tubeBorderPaint.strokeWidth = tubeWidth + 12f
        tubeWhiteCorePaint.strokeWidth = tubeWidth
        glitterFillPaint.strokeWidth = tubeWidth - 4f

        val strokeStrings = UrduPathsData.getStrokes(letter)

        for (strokeSvg in strokeStrings) {
            val rawPath = PathParser.createPathFromPathData(strokeSvg)
            val scaledPath = Path()
            rawPath.transform(matrix, scaledPath)

            val measure = PathMeasure(scaledPath, false)
            val len = measure.length
            val dots = mutableListOf<PointF>()

            // 🎯 Detect if this stroke is a Dot (Nuqta)
            val isDotStroke = len <= 25f

            if (len > 0 && !isDotStroke) {
                val dotSpacing = 32f
                val count = (len / dotSpacing).toInt()
                val pos = floatArrayOf(0f, 0f)

                for (i in 0..count) {
                    measure.getPosTan(i * dotSpacing, pos, null)
                    dots.add(PointF(pos[0], pos[1]))
                }
            }

            allStrokes.add(StrokeInfo(scaledPath, measure, len, dots, isDotStroke))
        }

        if (allStrokes.isNotEmpty()) {
            allStrokes[0].measure.getPosTan(0f, avatarPos, null)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (allStrokes.isEmpty()) return

        // 1. Draw hollow guide tubes
        for (stroke in allStrokes) {
            canvas.drawPath(stroke.path, tubeBorderPaint)
            canvas.drawPath(stroke.path, tubeWhiteCorePaint)
        }

        // 2. Draw guidance dots only on lines/curves (not inside single dots)
        val dotRadius = width * 0.012f
        for (stroke in allStrokes) {
            if (!stroke.isDot) {
                for (dot in stroke.guideDots) {
                    canvas.drawCircle(dot.x, dot.y, dotRadius, guideDotPaint)
                }
            }
        }

        // 3. Draw traced completed strokes
        if (!completedStrokesPath.isEmpty) canvas.drawPath(completedStrokesPath, glitterFillPaint)
        if (!activeTracedSegment.isEmpty) canvas.drawPath(activeTracedSegment, glitterFillPaint)

        // 4. Render sparkles
        renderSparkles(canvas)

        // 5. Draw Avatar Star on current active position
        if (!isCompleted) {
            drawAvatarStar(canvas, avatarPos[0], avatarPos[1], width * 0.045f)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isCompleted || allStrokes.isEmpty() || currentStrokeIndex >= allStrokes.size) return false

        val x = event.x
        val y = event.y
        val stroke = allStrokes[currentStrokeIndex]

        // 🎯 1-CLICK / 1-TAP TO COMPLETE DOTS (NUQTAS)
        if (stroke.isDot) {
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                val dist = hypot((x - avatarPos[0]).toDouble(), (y - avatarPos[1]).toDouble()).toFloat()
                // If kid touches or taps anywhere near the dot circle
                if (dist < width * 0.20f) {
                    completeCurrentStroke()
                    return true
                }
            }
            return true
        }

        // Standard dragging for normal letters & lines
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val dist = hypot((x - avatarPos[0]).toDouble(), (y - avatarPos[1]).toDouble()).toFloat()
                if (dist < width * 0.16f) {
                    isDragging = true
                    spawnSparkles(avatarPos[0], avatarPos[1], 4)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (isDragging) advanceActiveStroke(x, y)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isDragging = false
            }
        }
        invalidate()
        return true
    }

    private fun advanceActiveStroke(touchX: Float, touchY: Float) {
        val stroke = allStrokes[currentStrokeIndex]
        val searchAheadMax = 48f
        val step = 6f
        var bestDist = Float.MAX_VALUE
        var bestLength = currentTracedLength
        val testPos = floatArrayOf(0f, 0f)

        var l = currentTracedLength
        val limit = (currentTracedLength + searchAheadMax).coerceAtMost(stroke.length)

        while (l <= limit) {
            stroke.measure.getPosTan(l, testPos, null)
            val d = hypot((touchX - testPos[0]).toDouble(), (touchY - testPos[1]).toDouble()).toFloat()
            if (d < bestDist) {
                bestDist = d
                bestLength = l
            }
            l += step
        }

        if (bestDist < width * 0.15f && bestLength > currentTracedLength) {
            currentTracedLength = bestLength
            stroke.measure.getPosTan(currentTracedLength, avatarPos, null)

            activeTracedSegment.reset()
            stroke.measure.getSegment(0f, currentTracedLength, activeTracedSegment, true)
            spawnSparkles(avatarPos[0], avatarPos[1], 3)

            // Forgiving 92% completion threshold for kids
            if (currentTracedLength >= stroke.length * 0.92f) {
                completeCurrentStroke()
            }
        }
    }

    // 🎯 Instant stroke completion (used for dots on tap and strokes upon reaching the end)
    private fun completeCurrentStroke() {
        val stroke = allStrokes[currentStrokeIndex]
        completedStrokesPath.addPath(stroke.path)
        activeTracedSegment.reset()
        currentTracedLength = 0f
        spawnSparkles(avatarPos[0], avatarPos[1], 8)

        if (currentStrokeIndex < allStrokes.size - 1) {
            currentStrokeIndex++
            allStrokes[currentStrokeIndex].measure.getPosTan(0f, avatarPos, null)
            isDragging = false
            invalidate()
        } else {
            isCompleted = true
            isDragging = false
            invalidate()
            onCompleted?.invoke()
        }
    }

    private fun drawAvatarStar(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        canvas.drawCircle(cx, cy, radius * 1.2f, avatarBodyPaint)
        canvas.drawCircle(cx, cy, radius * 1.2f, avatarBorderPaint)

        val starPath = Path()
        val spikes = 5
        val innerR = radius * 0.5f
        var rot = Math.PI / 2 * 3
        val step = Math.PI / spikes

        starPath.moveTo(cx, cy - radius)
        for (i in 0 until spikes) {
            starPath.lineTo(cx + cos(rot).toFloat() * radius, cy + sin(rot).toFloat() * radius)
            rot += step
            starPath.lineTo(cx + cos(rot).toFloat() * innerR, cy + sin(rot).toFloat() * innerR)
            rot += step
        }
        starPath.close()

        val starPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = Color.parseColor("#E65100")
        }
        canvas.drawPath(starPath, starPaint)
    }

    private fun spawnSparkles(x: Float, y: Float, count: Int) {
        val color = Color.parseColor(currentLetter?.glitterColorHex ?: "#FFD700")
        for (i in 0 until count) {
            val angle = random.nextDouble() * 2 * Math.PI
            val speed = 2f + random.nextFloat() * 4f
            sparkles.add(
                Sparkle(
                    x, y,
                    (cos(angle) * speed).toFloat(),
                    (sin(angle) * speed).toFloat(),
                    255,
                    5f + random.nextFloat() * 8f,
                    if (random.nextBoolean()) Color.WHITE else color
                )
            )
        }
    }

    private fun renderSparkles(canvas: Canvas) {
        if (sparkles.isEmpty()) return
        val iter = sparkles.iterator()
        while (iter.hasNext()) {
            val s = iter.next()
            s.x += s.vx
            s.y += s.vy
            s.alpha -= 14
            if (s.alpha <= 0) {
                iter.remove()
            } else {
                sparklePaint.color = s.color
                sparklePaint.alpha = s.alpha
                canvas.drawCircle(s.x, s.y, s.size / 2f, sparklePaint)
            }
        }
        postInvalidateOnAnimation()
    }
}




