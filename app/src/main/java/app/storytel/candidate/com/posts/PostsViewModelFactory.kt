package app.storytel.candidate.com.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.storytel.candidate.com.postdetails.PostDetailsViewModel
import app.storytel.candidate.com.utils.ApiService

class PostsViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostsViewModel::class.java)) {
            return PostsViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown VM name")
    }

}
