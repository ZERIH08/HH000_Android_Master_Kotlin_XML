package com.example.hh000_android_master_kotlin_xml.core.extentions

import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams

object MyExtView {
    //----------------------------------------------------------------------------------------------
    fun View.isVisible0(): Boolean {
        return visibility == View.VISIBLE
    }

    fun View.isGone0(): Boolean {
        return visibility == View.GONE
    }

    fun View.isInvisible0(): Boolean {
        return visibility == View.INVISIBLE
    }

    //----------------------------------------------------------------------------------------------
    fun View.makeVisible0() {
        visibility = View.VISIBLE
    }

    fun View.makeGone0() {
        visibility = View.GONE
    }

    fun View.makeInvisible0() {
        visibility = View.INVISIBLE
    }

    //----------------------------------------------------------------------------------------------
    fun View.setPaddingInPixels0(
        leftPaddingInPixel: Int?, topPaddingInPixel: Int?, rightPaddingInPixel: Int?, bottomPaddingInPixel: Int?
    ) {
        setPadding(
            leftPaddingInPixel ?: paddingLeft,
            topPaddingInPixel ?: paddingTop,
            rightPaddingInPixel ?: paddingRight,
            bottomPaddingInPixel ?: paddingBottom
        )
    }

    fun View.addPaddingInPixels0(
        leftPaddingInPixel: Int?, topPaddingInPixel: Int?, rightPaddingInPixel: Int?, bottomPaddingInPixel: Int?
    ) {
        setPadding(
            paddingLeft + (leftPaddingInPixel ?: 0),
            paddingTop + (topPaddingInPixel ?: 0),
            paddingRight + (rightPaddingInPixel ?: 0),
            paddingBottom + (bottomPaddingInPixel ?: 0)
        )
    }

    //----------------------------------------------------------------------------------------------
    fun View.setMarginInPixels0(
        leftMarginInPixel: Int? = null, topMarginInPixel: Int? = null, rightMarginInPixel: Int? = null, bottomMarginInPixel: Int? = null
    ) {
        updateLayoutParams<ViewGroup.MarginLayoutParams> {
            leftMargin = leftMarginInPixel ?: leftMargin
            topMargin = topMarginInPixel ?: topMargin
            rightMargin = rightMarginInPixel ?: rightMargin
            bottomMargin = bottomMarginInPixel ?: bottomMargin
        }
    }

    fun View.addMarginInPixels0(
        leftMarginInPixel: Int? = null, topMarginInPixel: Int? = null, rightMarginInPixel: Int? = null, bottomMarginInPixel: Int? = null
    ) {
        updateLayoutParams<ViewGroup.MarginLayoutParams> {
            leftMargin += (leftMarginInPixel ?: 0)
            topMargin += (topMarginInPixel ?: 0)
            rightMargin += (rightMarginInPixel ?: 0)
            bottomMargin += (bottomMarginInPixel ?: 0)
        }
    }
    //----------------------------------------------------------------------------------------------
}