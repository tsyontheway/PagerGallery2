package com.example.pagerGallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.pager_photo_view.view.*

class PagerPhotoListAdapter : ListAdapter<PhotoItem, PagerPhotoViewHolder>(DIFFCallBack) {

    object DIFFCallBack : DiffUtil.ItemCallback<PhotoItem>() {
        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem.photoId == newItem.photoId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerPhotoViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.pager_photo_view, parent, false)
            .apply {
                return PagerPhotoViewHolder(this)
            }
    }

    override fun onBindViewHolder(holder: PagerPhotoViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(getItem(position).previewUrl)
            .placeholder(R.drawable.ic_photo_24)
//            .listener(object : RequestListener<Drawable> {
//                override fun onLoadFailed(
//                    e: GlideException?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    return false
//                }
//
//                override fun onResourceReady(
//                    resource: Drawable?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    dataSource: DataSource?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    return false
//                }
//
//            })
            .into(holder.itemView.pagerPhoto)
    }


}

class PagerPhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)