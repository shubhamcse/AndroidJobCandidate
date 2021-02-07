package app.storytel.candidate.com.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.storytel.candidate.com.utils.ApiService
import app.storytel.candidate.com.utils.Resource
import app.storytel.candidate.com.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class PostsViewModel(private val apiService: ApiService) : ViewModel() {

    private val postsAndImages = MutableLiveData<Resource<PostAndImages>>()

    private fun fetchPostsAndPhotos() {
        viewModelScope.launch(Dispatchers.IO) {
            postsAndImages.postValue(Resource(Status.LOADING, null, null))
            val postsResult = fetchPostsFromAPI()
            when (postsResult.status) {
                Status.SUCCESS -> {
                    val photosResult = fetchPhotosFromAPI()
                    when (photosResult.status) {
                        Status.SUCCESS -> {
                            postsAndImages.postValue(Resource(Status.SUCCESS, postsResult.data?.let { postsList ->
                                photosResult.data?.let { photosList ->
                                    PostAndImages(postsList,
                                            photosList)
                                }
                            }, postsResult.message))
                        }
                        Status.ERROR -> {
                            postsAndImages.postValue(Resource(Status.ERROR, null, photosResult.message))
                        }
                       else -> {
                            postsAndImages.postValue(Resource(Status.LOADING, null, photosResult.message))
                        }
                    }
                }
                Status.ERROR -> {
                    postsAndImages.postValue(Resource(Status.ERROR, null, postsResult.message))
                }
                Status.LOADING -> {
                    postsAndImages.postValue(Resource(Status.LOADING, null, postsResult.message))
                }
            }
        }

    }

    private fun fetchPostsFromAPI(): Resource<List<Post>?> {
        return try {
            val response = apiService.getPosts().execute()
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                Resource.error(response.body(), response.message())
            }
        } catch (e: IOException) {
            Resource.error(null, e.message ?: "")
        }

    }


    private fun fetchPhotosFromAPI(): Resource<List<Photo>?> {
        return try {
            val response = apiService.getPhotos().execute()
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                Resource.error(response.body(), response.message())
            }
        } catch (e: IOException) {
            Resource.error(null, e.message ?: "")
        }
    }

    fun getPostsAndPhotos(): LiveData<Resource<PostAndImages>> {
        postsAndImages.value?.data ?: run {
            fetchPostsAndPhotos()
        }
        return postsAndImages
    }

    fun retryGettingPostsAndPhotos() {
        fetchPostsAndPhotos()
    }
}

