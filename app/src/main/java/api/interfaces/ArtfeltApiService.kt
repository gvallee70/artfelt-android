package api.interfaces

import api.models.artwork.Artwork
import retrofit2.Response
import api.models.auth.signin.SignInRequest
import api.models.auth.signin.SignInResponse
import api.models.auth.signup.SignUpRequest
import api.models.auth.signup.SignUpResponse
import api.models.user.infos.UserInfosResponse
import retrofit2.Call
import retrofit2.http.*

interface ArtfeltApiService {

    /* AUTH */
    @Headers("Content-Type: application/json")
    @POST("/v1/auth/sign-up")
    suspend fun signUp(@Body request: SignUpRequest): Response<SignUpResponse>

    @Headers("Content-Type: application/json")
    @POST("/v1/auth/sign-in")
    suspend fun signIn(@Body request: SignInRequest): Response<SignInResponse>




    /* USER */
    @Headers( "Content-Type: application/json")
    @GET("/v1/user/info")
    suspend fun getSelfInfos(): Response<UserInfosResponse>




    /* ARTWORK */
    @Headers( "Content-Type: application/json")
    @GET("/v1/artwork")
    suspend fun getAllArtworks(): Response<ArrayList<Artwork>>





}