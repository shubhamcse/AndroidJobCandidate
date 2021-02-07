package app.storytel.candidate.com.postdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.storytel.candidate.com.utils.ApiService
import app.storytel.candidate.com.utils.Resource
import app.storytel.candidate.com.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostDetailsViewModel(private val apiService: ApiService) : ViewModel() {

    private val comments = MutableLiveData<Resource<List<Comment>>>()

    private fun fetchCommentsFromAPI(postID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            apiService.getComments(postID)
                    .enqueue(object : Callback<List<Comment>> {
                        override fun onResponse(call: Call<List<Comment>>,
                                                response: Response<List<Comment>>) {
                            if (response.isSuccessful) {
                                comments.postValue(Resource(Status.SUCCESS, response.body(), null))
                            } else {
                                comments.postValue(Resource(Status.SUCCESS, response.body(), response.message()))
                            }
                        }

                        override fun onFailure(call: Call<List<Comment>>,
                                               t: Throwable) {
                            comments.postValue(Resource(Status.ERROR, null, t.message))
                        }
                    })
        }
    }

    private fun checkPostIDAndFetchComments(postID: Int?) {
        if (postID == null) {
            comments.postValue(Resource(Status.ERROR, null, "post id is null"))
        } else {
            fetchCommentsFromAPI(postID)
        }
    }

    fun getComments(postID: Int?): LiveData<Resource<List<Comment>>> {
        comments.value?.data ?: run {
            checkPostIDAndFetchComments(postID)
        }
        return comments
    }


    fun retryGettingComments(postID: Int?) {
        checkPostIDAndFetchComments(postID)
    }
}
