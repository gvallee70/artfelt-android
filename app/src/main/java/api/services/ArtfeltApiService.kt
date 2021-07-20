package api.services

import api.models.artwork.Artwork
import api.models.auth.changepassword.ChangePasswordRequest
import retrofit2.Response
import api.models.auth.signin.SignInRequest
import api.models.auth.signin.SignInResponse
import api.models.auth.signup.SignUpRequest
import api.models.auth.signup.SignUpResponse
import api.models.request.BecomeArtistRequest
import api.models.user.update.UpdateUserInfoResponse
import api.models.user.User
import org.json.JSONObject
import retrofit2.http.*

interface ArtfeltApiService {

    /* AUTH */
    @POST("/v1/auth/sign-up")
    suspend fun signUp(@Body request: SignUpRequest): Response<SignUpResponse>

    @POST("/v1/auth/sign-in")
    suspend fun signIn(@Body request: SignInRequest): Response<SignInResponse>

    @POST("/v1/auth/change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<String>

    @GET("/v1/auth/refresh-token")
    suspend fun refreshAuthToken(): Response<Void>



    /* USER */
    @GET("/v1/user/info")
    suspend fun getSelfInfo(): Response<User>

    @PATCH("/v1/user/{id}")
    suspend fun updateUserInfo(@Path("id") id: String, @Body request: User): Response<UpdateUserInfoResponse>



    /* ARTWORK */
    @GET("/v1/artwork")
    suspend fun getAllArtworks(): Response<ArrayList<Artwork>>



    /* REQUEST */
    @POST("/v1/request")
    suspend fun becomeArtistRequest(@Body request: BecomeArtistRequest): Response<JSONObject>



}