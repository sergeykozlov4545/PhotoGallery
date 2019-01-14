package com.example.sergey.photogallery.feature.main

import android.content.Context
import android.support.v7.widget.AppCompatSeekBar
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar
import com.example.sergey.photogallery.R

class SeekBarWithLabels @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttrs: Int = 0
) : LinearLayout(context, attrs, defStyleAttrs) {

    companion object {
        private const val DEFAULT_MIN_VALUE = 0
        private const val DEFAULT_MAX_VALUE = 0
        private const val DEFAULT_CURRENT_VALUE = 0
    }

    private val sliderLabel: AppCompatTextView
    private val slider: AppCompatSeekBar
    private val minValueView: AppCompatTextView
    private val currentValueView: AppCompatTextView
    private val maxValueView: AppCompatTextView

    private var minValue: Int = DEFAULT_MIN_VALUE
    private var maxValue: Int = DEFAULT_MAX_VALUE
    private var currentValue: Int = DEFAULT_CURRENT_VALUE

    init {
        val layout = LayoutInflater.from(context)
                .inflate(R.layout.seekbar_with_labels, this)
        sliderLabel = layout.findViewById(R.id.sliderLabel)
        minValueView = layout.findViewById(R.id.minValueView)
        maxValueView = layout.findViewById(R.id.maxValueView)
        currentValueView = layout.findViewById(R.id.currentValueView)

        slider = layout.findViewById(R.id.slider)
        slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                currentValue = progress + minValue
                currentValueView.text = currentValue.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        attrs.takeIf { it != null }?.let {
            context.theme.obtainStyledAttributes(it, R.styleable.SeekBarWithLabels, 0, 0)
                    .run {
                        try {
                            sliderLabel.text = getString(R.styleable.SeekBarWithLabels_label)

                            minValue = getInteger(R.styleable.SeekBarWithLabels_minValue, DEFAULT_MIN_VALUE)
                            minValueView.text = minValue.toString()

                            maxValue = getInteger(R.styleable.SeekBarWithLabels_maxValue, DEFAULT_MAX_VALUE)
                            maxValueView.text = maxValue.toString()
                            slider.max = maxValue - minValue

                            currentValue = getInteger(R.styleable.SeekBarWithLabels_currentValue, DEFAULT_CURRENT_VALUE)
                            slider.progress = currentValue - minValue
                        } finally {
                            recycle()
                        }
                    }
        }
    }
}