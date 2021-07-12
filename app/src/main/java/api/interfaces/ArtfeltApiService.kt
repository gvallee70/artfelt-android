package api.interfaces

import retrofit2.Response
import api.models.UserBody
import retrofit2.http.*

interface ArtfeltApiService {

    @Headers("Content-Type: application/json")
    @POST("/v1/auth/sign-up")
    suspend fun signUp(@Body userBody: UserBody): Response<UserBody>

    @Headers("Content-Type: application/json")
    @POST("/v1/auth/sign-in")
    suspend fun signIn(@Body userBody: UserBody): Response<UserBody>

    @Headers( "Content-Type: application/json")
    @GET("/v1/user/info")
    suspend fun getSelfInfos(@Header("token") token: String): Response<UserBody>


}