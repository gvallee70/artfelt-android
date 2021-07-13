package api.interfaces

import retrofit2.Response
import api.models.auth.signin.SignInRequest
import api.models.auth.signin.SignInResponse
import api.models.auth.signup.SignUpRequest
import api.models.auth.signup.SignUpResponse
import api.models.user.infos.UserInfosResponse
import retrofit2.http.*

interface ArtfeltApiService {

    @Headers("Content-Type: application/json")
    @POST("/v1/auth/sign-up")
    suspend fun signUp(@Body request: SignUpRequest): Response<SignUpResponse>

    @Headers("Content-Type: application/json")
    @POST("/v1/auth/sign-in")
    suspend fun signIn(@Body request: SignInRequest): Response<SignInResponse>

    @Headers( "Content-Type: application/json")
    @GET("/v1/user/info")
    suspend fun getSelfInfos(@Header("authorization") token: String): Response<UserInfosResponse>


}