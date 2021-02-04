package app.storytel.candidate.com.posts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.storytel.candidate.com.R
import app.storytel.candidate.com.postdetails.PostDetailsActivity
import app.storytel.candidate.com.utils.ApiClient
import app.storytel.candidate.com.utils.Resource
import app.storytel.candidate.com.utils.Status
import app.storytel.candidate.com.utils.ViewModelFactory

class PostsActivity : AppCompatActivity() {

    private val TAG = "PostsActivity"

    var mRecyclerView: RecyclerView? = null

    private val postsViewModel: PostsViewModel by lazy {
        ViewModelProviders.of(
                this,
                ViewModelFactory(ApiClient.apiService)
        ).get(PostsViewModel::class.java)
    }

    private val mPostAdapter: PostAdapter by lazy {
        PostAdapter(onItemClicked = { post, photo ->
            val intent = Intent(this, PostDetailsActivity::class.java)
            intent.putExtra("postId", post.id)
            startActivity(intent)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        setRecyclerView()
        val progressBar: ProgressBar = findViewById(R.id.indeterminateBar)
        val retryLayout: ConstraintLayout = findViewById(R.id.layout_error)
        val retryButton: Button = findViewById(R.id.button_retry)
        retryButton.setOnClickListener { postsViewModel.retryGettingPostsAndPhotos() }

        postsViewModel.getPostsAndPhotos().observe(this, {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showLoadingView(progressBar, retryLayout)
                    }
                    Status.SUCCESS -> {
                        setDataOnRecyclerView(progressBar, retryLayout, result)
                    }
                    Status.ERROR -> {
                        showErrorView(progressBar, retryLayout, it)
                    }
                }

            }
        })
    }


    private fun setRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view)
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRecyclerView?.layoutManager = manager
        mRecyclerView?.adapter = mPostAdapter
    }

    private fun showLoadingView(progressBar: ProgressBar,
                                retryLayout: ConstraintLayout) {
        progressBar.visibility = View.VISIBLE
        mRecyclerView?.visibility = View.GONE
        retryLayout.visibility = View.GONE
    }

    private fun setDataOnRecyclerView(progressBar: ProgressBar,
                                      retryLayout: ConstraintLayout,
                                      result: Resource<PostAndImages>) {
        progressBar.visibility = View.GONE
        mRecyclerView?.visibility = View.VISIBLE
        retryLayout.visibility = View.GONE
        result.data?.let { postsAndImages -> mPostAdapter.setData(postsAndImages) }
    }

    private fun showErrorView(progressBar: ProgressBar,
                              retryLayout: ConstraintLayout,
                              it: Resource<PostAndImages>) {
        progressBar.visibility = View.GONE
        retryLayout.visibility = View.VISIBLE
        Log.d(TAG, "error occurred while fetching with message:" + it.message)
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
