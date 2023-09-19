package ru.agpu.artikproject.background_work.adapters.recycler_view

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.agpu.artikproject.R

class RecyclerViewPhotoAdapter(
    private val context: Context,
    private val datas: List<String?>?,
): RecyclerView.Adapter<RecyclerViewHolderPhoto>() {


    private val mLayoutInflater = LayoutInflater.from(context)

    override fun getItemCount() = datas?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolderPhoto {
        // Inflate view from recyclerview_item_layout.xml
        val recyclerViewItem: View =
            mLayoutInflater.inflate(R.layout.recyclerview_item_photo, parent, false)


        return RecyclerViewHolderPhoto(recyclerViewItem)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolderPhoto, position: Int) {
        // Cet item in countries via position
        val imageResUrl = datas?.get(position)

        val imageView = holder.itemView.findViewById<ImageView>(R.id.imageViewPhoto)

        // Отображаем картинку в адаптере
        Glide.with(context)
            .load(imageResUrl)
            .placeholder(R.drawable.agpu_ico)
            .centerCrop()
            .into(imageView)
    }
}
class RecyclerViewHolderPhoto(itemView: View): RecyclerView.ViewHolder(itemView)
