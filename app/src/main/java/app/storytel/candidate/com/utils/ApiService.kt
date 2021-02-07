package app.storytel.candidate.com.utils

import app.storytel.candidate.com.postdetails.Comment
import app.storytel.candidate.com.posts.Photo
import app.storytel.candidate.com.posts.Post
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("posts")
    fun getPosts(): Call<List<Post>>
    @GET("photos")
    fun getPhotos(): Call<List<Photo>>
    @GET ("posts/{id}/comments")
    fun getComments(@Path("id") id: Int): Call<List<Comment>>
}
