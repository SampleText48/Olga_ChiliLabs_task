package com.example.olga_chililabs_task.ui

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.util.Log
import android.util.TypedValue
import android.view.*
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.olga_chililabs_task.R
import com.example.olga_chililabs_task.network.*
import com.facebook.drawee.backends.pipeline.*
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.request.*
import kotlin.math.roundToInt

//code based on https://discover.hubpages.com/technology/Working-With-Android-JetPack-Paging-Library

//RecyclerView list adapter to display received images in a grid pattern using CardView elements
class InfiniteListAdapter(
    private val context: Context,
    val clickListener: (GiphySearchImagePoko.GiphySearchDatum) -> Unit
) : PagedListAdapter<GiphySearchImagePoko.GiphySearchDatum, ViewHolder>(DIFF) {

    private var mWidth: Int = 0

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<GiphySearchImagePoko.GiphySearchDatum>() {
            override fun areItemsTheSame(oldItem: GiphySearchImagePoko.GiphySearchDatum, newItem: GiphySearchImagePoko.GiphySearchDatum): Boolean {
                Log.e("ITEM", oldItem.url)
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GiphySearchImagePoko.GiphySearchDatum, newItem: GiphySearchImagePoko.GiphySearchDatum): Boolean {
                return oldItem.url.contentEquals(newItem.url)
            }
        }

        const val SMALL_ITEM_HEIGHT = 180
        const val LARGE_ITEM_HEIGHT = 250
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val context = recyclerView.context as Activity
        val windowDimensions = Point()
        context.windowManager.defaultDisplay.getSize(windowDimensions)
        mWidth = (windowDimensions.x * 0.5f).roundToInt()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = when (viewType) {
            0 -> {
                (LayoutInflater.from(parent.context)
                    .inflate(R.layout.small_item_layout, parent, false))
            }
            else -> {
                (LayoutInflater.from(parent.context)
                    .inflate(R.layout.large_item_layout, parent, false))
            }
        }

        val params: ViewGroup.LayoutParams = ViewGroup.LayoutParams(
            mWidth,
            dpToPixels((if (viewType == 0) SMALL_ITEM_HEIGHT else LARGE_ITEM_HEIGHT).toFloat(), context).toInt()
        )
        view.layoutParams = params
        return GenericViewHolder(view)

    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 4 == 0) 0 else 1
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder as GenericViewHolder
        holder.view.findViewById<SimpleDraweeView>(R.id.image).setOnClickListener{
            clickListener(getItem(position)!!)
        }
        if (getItem(position) == null) {
            val imageRequest = ImageRequestBuilder.newBuilderWithResourceId(R.drawable.bg_white_drawable).build()
            holder.view.findViewById<SimpleDraweeView>(R.id.image).setImageRequest(imageRequest)
        } else {
            val controller = Fresco.newDraweeControllerBuilder()
                .setUri(getItem(position)?.images?.downSampledFixedWidth?.url?:"")
                .setAutoPlayAnimations(true)
                .build()
            holder.view.findViewById<SimpleDraweeView>(R.id.image).setController(controller)
        }

    }

    private fun dpToPixels(value: Float, context: Context) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, value,
        context.resources.displayMetrics
    )

    data class GenericViewHolder(val view: View) : RecyclerView.ViewHolder(view)


}