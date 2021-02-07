package app.storytel.candidate.com.posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.storytel.candidate.com.R
import app.storytel.candidate.com.posts.PostAdapter.PostViewHolder
import com.bumptech.glide.Glide
import java.util.*

class PostAdapter(private val onItemClicked: (Post, Photo) -> Unit) :
        RecyclerView.Adapter<PostViewHolder>() {

    private var mData: PostAndImages = PostAndImages(arrayListOf(), arrayListOf())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = mData.mPosts[position]
        val photo = mData.mPhotos[Random().nextInt(mData.mPhotos.size - 1)]
        holder.bind(post, photo)
        holder.itemView.setOnClickListener {
            onItemClicked(post, photo)
        }
    }

    fun setData(data: PostAndImages) {
        mData = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mData.mPosts.size
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView = itemView.findViewById(R.id.title)
        var body: TextView = itemView.findViewById(R.id.body)
        var image: ImageView = itemView.findViewById(R.id.image)

        fun bind(post: Post, photo: Photo) {
            title.text = post.title
            body.text = post.body
            val imageUrl = photo.thumbnailUrl
            Glide.with(itemView.context)
                    .load(imageUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(image)
        }
    }
}
