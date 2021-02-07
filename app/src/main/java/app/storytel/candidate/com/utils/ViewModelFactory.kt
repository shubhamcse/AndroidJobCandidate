package app.storytel.candidate.com.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.storytel.candidate.com.postdetails.PostDetailsViewModel
import app.storytel.candidate.com.posts.PostsViewModel

class ViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostsViewModel::class.java)) {
            return PostsViewModel(apiService) as T
        } else if (modelClass.isAssignableFrom(PostDetailsViewModel::class.java)) {
            return PostDetailsViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown VM name")
    }

}
