package com.example.pagerGallery

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.cell_recycler.view.*

class GalleryAdapter : ListAdapter<PhotoItem, MyViewHolder>(CallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val holder = MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cell_recycler, parent, false)
        )

        holder.itemView.setOnClickListener {
            Bundle().apply {
                putParcelableArrayList("PHOTO_LIST", ArrayList(currentList))
                putInt("PHOTO_POSITION", holder.adapterPosition)
                holder.itemView.findNavController()
                    .navigate(R.id.action_galleryFragment_to_pagerPhotoFragment, this)

            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val photoItem = getItem(position)
        with(holder.itemView) {
            shimmerLayoutCell.apply {
                setShimmerColor(0x55FFFFFF)
                setShimmerAngle(0)
                startShimmerAnimation()
            }
            textViewUser.text = photoItem.photoUser
            textViewLikes.text = photoItem.photoLikes.toString()
            textViewFavorites.text = photoItem.photoFavorites.toString()
            //解决瀑布流布局的图片高度重绘问题，提前从网上拿回图片高度，设定死
            holder.itemView.imageView.layoutParams.height = photoItem.photoHeight
        }

//        holder.itemView.shimmerLayoutCell.apply {
//            setShimmerColor(0x55FFFFFF)
//            setShimmerAngle(0)
//            startShimmerAnimation()
//        }


        //解决瀑布流布局的图片高度重绘问题，提前从网上拿回图片高度，设定死
//        holder.itemView.imageView.layoutParams.height = getItem(position).photoHeight
        Glide.with(holder.itemView)
            .load(getItem(position).previewUrl)
            .placeholder(R.drawable.photo_placeholder)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false.also { holder.itemView.shimmerLayoutCell?.stopShimmerAnimation() }
                }

            })
            .into(holder.itemView.imageView)
    }

    object CallBack : DiffUtil.ItemCallback<PhotoItem>() {
        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem.photoId == newItem.photoId
        }

    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


