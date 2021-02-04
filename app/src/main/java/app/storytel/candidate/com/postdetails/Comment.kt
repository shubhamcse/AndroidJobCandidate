package app.storytel.candidate.com.postdetails

data class Comment (
    val postId:Int,
    var id:Int,
    var name: String,
    var email: String,
    var body: String
)
