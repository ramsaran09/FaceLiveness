package com.muthuram.faceliveness.helper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View


class ProgressCircle : View {
    private var mRingBias = 0.15f
    private var mSectionRatio = 6.0f
    private val mSectionRect = RectF()
    private var mSectionHeight = 0f
    private var mRadius = 0f
    private var mMinProgress = 0
    private var mMaxProgress = 100
    private var mCenterX = 0f
    private var mCenterY = 0f
    private val mPaint = Paint()

    private var mProgress = 50
    private var hasReachedEnd: Boolean = false
    private var dashColor: Int = Color.parseColor("#F59E0B")
    private var colorOnComplete: Int = Color.parseColor("#22C55E")

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context?) : super(context)

    private fun updateDimensions(width: Int, height: Int) {
        // Update center position
        mCenterX = width / 2.0f
        mCenterY = height / 2.0f

        // Find shortest dimension
        val diameter = width.coerceAtMost(height)
        val outerRadius = (diameter / 2).toFloat()
        val sectionHeight = outerRadius * mRingBias
        val sectionWidth = sectionHeight / mSectionRatio
        mRadius = outerRadius - sectionHeight / 2
        mSectionRect.set(
            -(sectionWidth * 0.5f),
            -sectionHeight / 2,
            sectionWidth * 0.5f,
            (sectionHeight * 0.75f)
        )
        mSectionHeight = sectionHeight
    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        if (width > height)
            super.onMeasure(heightMeasureSpec, heightMeasureSpec)
        else
            super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        updateDimensions(getWidth(), getHeight())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateDimensions(w, h)
    }

    override fun onDraw(canvas: Canvas) {
        // Center our canvas
        canvas.translate(mCenterX, mCenterY)
        val relativeProgress = mProgress - mMinProgress
        val relativeMax = mMaxProgress - mMinProgress
        val rotation = 360.0f / relativeMax.toFloat()
        for (i in 0 until relativeMax) {
            canvas.save()
            canvas.rotate(i.toFloat() * rotation)
            canvas.translate(0f, -mRadius)
            if (i < relativeProgress) {
                val bias = i.toFloat() / (relativeMax.toFloat() - 1f)
                val color = interpolateColor(bias)
                mPaint.color = color
                canvas.drawRect(mSectionRect, mPaint)
            }
            canvas.restore()
        }
        super.onDraw(canvas)
    }

    private fun interpolate(a: Float, b: Float, bias: Float): Float {
        return a + (b - a) * bias
    }

    private fun interpolateColor(bias: Float): Int {
        val colorA = if (hasReachedEnd) colorOnComplete else dashColor
        val colorB = if (hasReachedEnd) colorOnComplete else dashColor
        val hsvColorA = FloatArray(3)
        Color.colorToHSV(colorA, hsvColorA)
        val hsvColorB = FloatArray(3)
        Color.colorToHSV(colorB, hsvColorB)
        hsvColorB[0] = interpolate(hsvColorA[0], hsvColorB[0], bias)
        hsvColorB[1] = interpolate(hsvColorA[1], hsvColorB[1], bias)
        hsvColorB[2] = interpolate(hsvColorA[2], hsvColorB[2], bias)

        // NOTE For some reason the method HSVToColor fail in edit mode. Just use the start color for now
        return if (isInEditMode) colorA else Color.HSVToColor(hsvColorB)
    }

    fun update(progressToApply: Int, hasReachedEnd: Boolean) {
        mProgress = progressToApply
        this.hasReachedEnd = hasReachedEnd
        invalidate()
    }

    init {
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.FILL
    }
}