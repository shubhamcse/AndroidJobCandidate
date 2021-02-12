package app.storytel.candidate.com.posts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import app.storytel.candidate.com.R
import app.storytel.candidate.com.databinding.ActivityScrollingBinding
import app.storytel.candidate.com.postdetails.PostDetailsActivity
import app.storytel.candidate.com.utils.ApiClient
import app.storytel.candidate.com.utils.Resource
import app.storytel.candidate.com.utils.Status

class PostsActivity : AppCompatActivity() {

    companion object {
        private val TAG = "PostsActivity"
    }

    private val postsViewModel: PostsViewModel by lazy {
        ViewModelProviders.of(
                this,
                PostsViewModelFactory(ApiClient.apiService)
        ).get(PostsViewModel::class.java)
    }

    private val binding: ActivityScrollingBinding by lazy { ActivityScrollingBinding.inflate(layoutInflater) }

    private val mPostAdapter: PostAdapter by lazy {
        PostAdapter(onItemClicked = { post, photo ->
            val intent = Intent(this, PostDetailsActivity::class.java)
            intent.apply {
                putExtra(PostDetailsActivity.EXTRAS_POST_ID, post.id)
                putExtra(PostDetailsActivity.EXTRAS_POST_BODY, post.body)
                putExtra(PostDetailsActivity.EXTRAS_POST_TITLE, post.title)
                putExtra(PostDetailsActivity.EXTRAS_POST_PHOTO_URL, photo.url)
            }
            startActivity(intent)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        setRecyclerView()

        binding.layoutError.buttonRetry
                .setOnClickListener { postsViewModel.retryGettingPostsAndPhotos() }

        postsViewModel.getPostsAndPhotos().observe(this, {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showLoadingView()
                    }
                    Status.SUCCESS -> {
                        setDataOnRecyclerView(result)
                    }
                    Status.ERROR -> {
                        showErrorView()
                        Log.d(TAG, "error occurred while fetching with message:" + it.message)
                    }
                }

            }
        })
    }


    private fun setRecyclerView() {
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        with(binding) {
            scrollingContent.recyclerView.layoutManager = manager
            scrollingContent.recyclerView.adapter = mPostAdapter
        }
    }

    private fun showLoadingView() {
        with(binding) {
            scrollingContent.indeterminateBar.visibility = View.VISIBLE
            appBar.visibility = View.VISIBLE
            scrollingContent.recyclerView.visibility = View.GONE
            layoutError.relativeErrorLayout.visibility = View.GONE
        }
    }

    private fun setDataOnRecyclerView(result: Resource<PostAndImages>) {
        with(binding) {
            scrollingContent.indeterminateBar.visibility = View.GONE
            appBar.visibility = View.VISIBLE
            scrollingContent.recyclerView.visibility = View.VISIBLE
            layoutError.relativeErrorLayout.visibility = View.GONE
        }
            result.data?.let { postsAndImages -> mPostAdapter.setData(postsAndImages) }

    }

    private fun showErrorView() {
        with(binding) {
            appBar.visibility = View.GONE
            scrollingContent.indeterminateBar.visibility = View.GONE
            scrollingContent.recyclerView.visibility = View.GONE
            layoutError.relativeErrorLayout.visibility = View.VISIBLE
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

}
