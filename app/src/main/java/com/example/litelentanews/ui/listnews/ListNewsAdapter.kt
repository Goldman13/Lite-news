package com.example.litelentanews.ui.listnews

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.view.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.getColorOrThrow
import androidx.core.content.res.getDimensionOrThrow
import androidx.core.content.res.getDimensionPixelSizeOrThrow
import androidx.core.graphics.PaintCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import androidx.core.graphics.withTranslation
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavDestination
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableResource
import com.example.litelentanews.R
import com.example.litelentanews.data.db.NewsEntity
import com.example.litelentanews.databinding.CardNewsBinding
import com.example.litelentanews.domain.model.NewsItemModel
import com.example.litelentanews.ui.GlideApp
import com.example.litelentanews.util.newStaticLayout
import com.google.android.material.behavior.SwipeDismissBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ListNewsAdapter(
        bottomSheetBehavior: BottomSheetBehavior<*>,
        val query: (String,View,ActionMode.Callback)->Unit,
        val changeFavorite: (String, Boolean)->Unit
)
    : ListAdapter<NewsItemModel,ListNewsViewHolder>(NewsDiffCallback()) {

    private var actionMode:ActionMode? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListNewsViewHolder {
        val binding = CardNewsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListNewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListNewsViewHolder, position: Int) {
        val item = getItem(position)
        holder.title.text = item.title
        holder.favorite = item.favorite
        holder.guid = item.guid

        GlideApp.with(holder.itemView).load(item.linkImage).into(holder.image)
        holder.itemView.setOnClickListener {
            query(item.link, it, actionModeCallback)
        }
    }

    val actionModeCallback = object:ActionMode.Callback{
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            val inflater: MenuInflater = mode.menuInflater
            inflater.inflate(R.menu.lenta_news_context_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            actionMode = null
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
}

class NewsDiffCallback:DiffUtil.ItemCallback<NewsItemModel>(){
    override fun areItemsTheSame(oldItem: NewsItemModel, newItem: NewsItemModel): Boolean {
        return oldItem.guid == newItem.guid
    }

    override fun areContentsTheSame(oldItem: NewsItemModel, newItem: NewsItemModel): Boolean {
        return oldItem.guid == newItem.guid
    }
}

class ListNewsViewHolder(binding:CardNewsBinding):RecyclerView.ViewHolder(binding.root){
    val title = binding.newsTitle
    val image = binding.newsImageTitle
    var favorite:Boolean = false
    lateinit var guid:String
}

class CustomItemDecoration(val context: Context, list:List<NewsItemModel>):RecyclerView.ItemDecoration(){

    companion object{
        private const val TITLE_TAG = "TITLE"
    }

    private val paint: TextPaint
    private val textWidth: Int
    private val textHeight: Int

    init {
        val attrs = context.obtainStyledAttributes(
                R.style.DateTitles,
                R.styleable.DateTitle
        )

        paint = TextPaint(Paint.ANTI_ALIAS_FLAG or Paint.SUBPIXEL_TEXT_FLAG).apply {
            textSize = attrs.getDimensionOrThrow(R.styleable.DateTitle_android_textSize)
            bgColor = Color.BLUE
            color = attrs.getColorOrThrow(R.styleable.DateTitle_android_textColor)
            typeface = Typeface.DEFAULT_BOLD
        }

        textWidth = attrs.getDimensionPixelSizeOrThrow(R.styleable.DateTitle_android_width)
        textHeight = attrs.getDimensionPixelSizeOrThrow(R.styleable.DateTitle_android_height)
        attrs.recycle()
    }

    val mapDateHeader = list.mapIndexed { index, newsItemModel ->
        index to getDateString(newsItemModel.date)
    }.distinctBy { it.second }.map{ it.first to createDateTitleLayout(it.second)}.toMap()

    private fun getDateString(date:Long):String{
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(date),ZoneId.systemDefault())
                .toLocalDate()
                .atStartOfDay()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    }

    private fun createDateTitleLayout(date:String):StaticLayout{
        return newStaticLayout(
                date,
                paint,
                textWidth,
                Layout.Alignment.ALIGN_CENTER,
                1f,
                0f,
                false
        )
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        outRect.top = if (mapDateHeader.containsKey(position)) textHeight else 0
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        val layoutManager = parent.layoutManager ?: return

        parent.forEach {child ->
            val layout = mapDateHeader[parent.getChildAdapterPosition(child)]
            if (layout != null) {
                val dx = (parent.width-textWidth).toFloat()
                val dy = layoutManager.getDecoratedTop(child) + child.translationY
                c.withTranslation(dx,dy) {
                    layout.draw(this)
                }
            }
        }
    }
}

class ItemTouchHelperCallBack(val adapter:ListNewsAdapter):ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){

    var vectorDrawableFalse: VectorDrawable? = null
    var vectorDrawableTrue: VectorDrawable? = null
    var bitmap:Bitmap? = null
    var isFinished = false
    private var previousDx = 0.0f

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        vectorDrawableFalse = vectorDrawableFalse?:ResourcesCompat.getDrawable(recyclerView.context.resources,R.drawable.ic_favorite_false_50,null) as VectorDrawable
        vectorDrawableTrue = vectorDrawableTrue?:ResourcesCompat.getDrawable(recyclerView.context.resources,R.drawable.ic_favorite_true_50,null) as VectorDrawable

        if(dX == 0.0f){
            previousDx = dX
            bitmap = null
            isFinished = false
        }

        val isFavorite = (viewHolder as ListNewsViewHolder).favorite

        bitmap = bitmap?:if(isFavorite)
            createBitmap(vectorDrawableTrue!!)
        else
            createBitmap(vectorDrawableFalse!!)

        val p = Paint()

        if(isCurrentlyActive){
            if (!isFinished && Math.abs(dX)>viewHolder.itemView.width-viewHolder.itemView.width/3){
                adapter.changeFavorite(viewHolder.guid,!isFavorite)
                (viewHolder as ListNewsViewHolder).favorite = !isFavorite
                bitmap = null
                isFinished = true
            }else if(!isFinished && previousDx>dX && Math.abs(dX)>viewHolder.itemView.width/2){
                bitmap = bitmap!!.scale(bitmap!!.width + 5,bitmap!!.height+5)
            }
        }

        bitmap?.let {
            c.drawBitmap(bitmap!!, viewHolder.itemView.right - 200f, viewHolder.itemView.top + (viewHolder.itemView.height / 2f - bitmap!!.height / 2f), p)
        }
        previousDx = dX
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun createBitmap(vectorDrawable: VectorDrawable):Bitmap{
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        vectorDrawable.draw(canvas)
        return bitmap
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.notifyItemChanged(viewHolder.adapterPosition)
    }
}