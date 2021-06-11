package api.interfaces

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import api.models.User

interface ArtfeltApiService {

    @Headers("Content-Type: application/json")
    @POST("/v1/auth/sign-up")
    suspend fun signUp(@Body user: User): Response<User>

    @Headers("Content-Type: application/json")
    @POST("/v1/auth/sign-in")
    suspend fun signIn(@Body user: User): Response<User>

}