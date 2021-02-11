package app.storytel.candidate.com.postdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.storytel.candidate.com.postdetails.PostDetailsViewModel
import app.storytel.candidate.com.utils.ApiService

class PostDetailsViewModelFactory(private val apiService: ApiService, private val postId:Int?) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostDetailsViewModel::class.java)) {
            return PostDetailsViewModel(apiService, postId) as T
        }
        throw IllegalArgumentException("Unknown VM name")
    }

}
