package com.example.olga_chililabs_task.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

//code based on https://discover.hubpages.com/technology/Working-With-Android-JetPack-Paging-Library

//visual enhancements to RecyclerView
class RecyclerViewDecoration(
    private val sideOffset: Int = 30,
    private val crossSectionOffset: Int = 12,
    private val paintDivider: Paint = getPainter(3F),
    private val paintCorners: Paint = getPainter(1F),
    private val shouldDecorate: (Int) -> Boolean = fun(_: Int) = true
) : RecyclerView.ItemDecoration() {

    companion object {
        fun getPainter(width: Float): Paint {
            val defaultPainter = Paint(Paint.ANTI_ALIAS_FLAG)
            defaultPainter.color = Color.TRANSPARENT
            defaultPainter.style = Paint.Style.STROKE
            defaultPainter.strokeWidth = width
            return defaultPainter
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state:  RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildAdapterPosition(view) < 0){
            return
        }
        if (shouldDecorate(parent.getChildAdapterPosition(view))) {
            outRect.set(sideOffset, crossSectionOffset, sideOffset, crossSectionOffset)
        }else{
            outRect.set(0, 0, 0, 0)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val layoutManager = parent.layoutManager
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (layoutManager != null) {
                c.drawRect(
                    (layoutManager.getDecoratedLeft(child) + 16).toFloat(),
                    layoutManager.getDecoratedTop(child).toFloat(),
                    (layoutManager.getDecoratedRight(child) - sideOffset).toFloat(),
                    layoutManager.getDecoratedBottom(child).toFloat(),
                    paintDivider
                )

                c.drawRect(
                    (layoutManager.getDecoratedLeft(child) + sideOffset).toFloat(),
                    (layoutManager.getDecoratedTop(child) + crossSectionOffset).toFloat(),
                    (layoutManager.getDecoratedRight(child) + sideOffset).toFloat(),
                    (layoutManager.getDecoratedBottom(child) - crossSectionOffset).toFloat(),
                    paintCorners
                )
            }
        }
    }
}