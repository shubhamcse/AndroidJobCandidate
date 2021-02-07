package app.storytel.candidate.com.postdetails

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.storytel.candidate.com.R
import app.storytel.candidate.com.databinding.ActivityDetailsBinding
import app.storytel.candidate.com.utils.ApiClient
import app.storytel.candidate.com.utils.Resource
import app.storytel.candidate.com.utils.Status
import app.storytel.candidate.com.utils.ViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.min

class PostDetailsActivity : AppCompatActivity() {

    companion object {

        const val EXTRAS_POST_ID = "postId"
        const val EXTRAS_POST_BODY = "postBody"
        const val EXTRAS_POST_PHOTO_URL = "postPhotoUrl"
        const val EXTRAS_POST_TITLE = "postPhotoTitle"
    }

    private val postDetailsAdapter by lazy { PostDetailsAdapter() }
    private val mRecyclerView: RecyclerView by lazy { findViewById(R.id.recycler_view) }
    private val binding:ActivityDetailsBinding by lazy { ActivityDetailsBinding.inflate(layoutInflater) }
    private val layoutErrorRetry by lazy { findViewById<RelativeLayout>(R.id.layout_error) }

    private val postsDetailsViewModel: PostDetailsViewModel by lazy {
        ViewModelProviders.of(
                this,
                ViewModelFactory(ApiClient.apiService)
        ).get(PostDetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        val extras = intent.extras

        val postID = extras?.getInt(EXTRAS_POST_ID)
        val postBody = extras?.getString(EXTRAS_POST_BODY)
        val postTitle = extras?.getString(EXTRAS_POST_TITLE)
        val postPhotoURL = extras?.getString(EXTRAS_POST_PHOTO_URL)

        setToolbar(binding, postTitle)

        binding.details.text = postBody

        setBackdrop(postPhotoURL, binding)

        setRecyclerView()

        getComments(postID)

        val retry = findViewById<Button>(R.id.button_retry)
        retry.setOnClickListener {
            postsDetailsViewModel.retryGettingComments(postID)
        }
    }

    private fun setToolbar(
            binding: ActivityDetailsBinding,
            postTitle: String?) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = postTitle
    }

    private fun setBackdrop(postPhotoURL: String?,
                            binding: ActivityDetailsBinding) {
        Glide.with(this)
                .load(postPhotoURL)
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.backdrop)
    }

    private fun getComments(postID: Int?) {
        postsDetailsViewModel.getComments(postID).observe(this, {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showLoadingView()
                    }
                    Status.SUCCESS -> {
                        showDataOnRecyclerView(result)
                    }
                    Status.ERROR -> {
                        showErrorLayout()
                    }
                }
            }
        })
    }

    private fun setRecyclerView() {
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = manager
        mRecyclerView.adapter = postDetailsAdapter
    }

    private fun showLoadingView() {
        with(binding) {
            indeterminateBar.visibility = View.VISIBLE
            layoutError.relativeErrorLayout.visibility = View.GONE
            appbar.visibility = View.VISIBLE
        }
    }

    private fun showDataOnRecyclerView(result: Resource<List<Comment>>) {
        with(binding) {
            indeterminateBar.visibility = View.GONE
            layoutError.relativeErrorLayout.visibility = View.GONE
            appbar.visibility = View.VISIBLE
        }

        if (result.data.isNullOrEmpty()) {
            //can show a empty view
        } else {
            postDetailsAdapter.setData(result.data.subList(0, min(result.data.size,3)))
        }
    }

    private fun showErrorLayout() {
        with(binding) {
            indeterminateBar.visibility = View.GONE
            layoutError.relativeErrorLayout.visibility = View.VISIBLE
            appbar.visibility = View.GONE
        }
    }

}


