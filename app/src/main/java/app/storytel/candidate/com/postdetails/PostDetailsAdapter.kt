package app.storytel.candidate.com.postdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.storytel.candidate.com.R
import kotlin.math.min

class PostDetailsAdapter:
        RecyclerView.Adapter<PostDetailsAdapter.CommentViewHolder>() {

    private var mComments:List<Comment> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.comment_item, parent, false))
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(mComments[position])
    }

    fun setData(data: List<Comment>) {
        mComments = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return min(mComments.size, 3)
    }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView = itemView.findViewById(R.id.title1)
        var description: TextView = itemView.findViewById(R.id.description1)

        fun bind(comment: Comment) {
            title.text = comment.name
            description.text = comment.body
        }
    }
}
