package app.storytel.candidate.com;

import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import java.util.Random;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private PostAndImages mData;
    private RequestManager mRequestManager;
    private Activity mActivity;

    public PostAdapter(RequestManager requestManager, Activity activity) {
        mRequestManager = requestManager;
        mActivity = activity;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.title.setText(mData.getMPosts().get(position).getTitle());
        holder.body.setText(mData.getMPosts().get(position).getBody());
        int index = new Random().nextInt(mData.getMPhotos().size() - 1);
        String imageUrl = mData.getMPhotos().get(index).getThumbnailUrl();
        mRequestManager.load(imageUrl).into(holder.image);
        holder.body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, DetailsActivity.class));
            }
        });
    }

    public void setData(PostAndImages data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.getMPosts().size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView body;
        ImageView image;

        public PostViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            body = itemView.findViewById(R.id.body);
            image = itemView.findViewById(R.id.image);
        }
    }
}
