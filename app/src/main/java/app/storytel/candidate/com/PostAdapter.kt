package app.storytel.candidate.com

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.storytel.candidate.com.DetailsActivity
import app.storytel.candidate.com.PostAdapter.PostViewHolder
import com.bumptech.glide.RequestManager
import java.util.*

class PostAdapter(private val mRequestManager: RequestManager, private val mActivity: Activity) :
        RecyclerView.Adapter<PostViewHolder>() {

    private var mData: PostAndImages? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.title.text = mData!!.mPosts.get(position).title
        holder.body.text = mData!!.mPosts.get(position).body
        val index = Random().nextInt(mData!!.mPhotos.size - 1)
        val imageUrl = mData!!.mPhotos[index].thumbnailUrl
        mRequestManager.load(imageUrl).into(holder.image)
        holder.body.setOnClickListener(
                View.OnClickListener { mActivity.startActivity(Intent(mActivity, DetailsActivity::class.java)) })
    }

    fun setData(data: PostAndImages?) {
        mData = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (mData == null) 0 else mData!!.mPosts.size
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView
        var body: TextView
        var image: ImageView

        init {
            title = itemView.findViewById(R.id.title)
            body = itemView.findViewById(R.id.body)
            image = itemView.findViewById(R.id.image)
        }
    }
}
