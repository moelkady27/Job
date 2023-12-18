package com.example.job.service

import com.example.job.model.*
import com.example.job.request.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("register")
    fun register(
        @Body req: SignUpRequest
    ) : Call<RegisterResponse>

    @POST("login")
    fun login(
        @Body req: LoginRequest
    ) : Call<LoginResponse>

    @PUT("changePassword")
    fun changePassword(
        @Header("Authorization") token: String,
        @Body req: ChangeRequest
    ): Call<ChangeResponse>

    @PUT("updateUser")
    fun updateUser(
        @Header("Authorization") token: String,
        @Body req: UpdateRequest
    ): Call<UpdateResponse>

    @GET("getAllJobs")
    fun getAllJobs(
        @Header("Authorization") token: String
    ) : Call<GetAllJobsResponse>

    @GET("searchJobs")
    fun searchJobs(
        @Header("Authorization") token: String,
        @Query("q") query: String
    ): Call<SearchResponse>

    @POST("addBookmark")
    fun addBookmark(
        @Header("Authorization") token: String,
        @Body req: AddBookmarkRequest
    ): Call<AddBookmarkResponse>

    @GET("getAllBookmarks")
    fun getAllBookmarks(
        @Header("Authorization") token: String,
    ): Call<GetAllBookmarksResponse>

    @GET("getUser")
    fun getUser(
        @Header("Authorization") token: String
    ) : Call<GetUserProfileResponse>

    @DELETE("deleteUser")
    fun deleteUser(
        @Header("Authorization") token: String,
        @Query("userId") userId: String,
        @Query("password") password: String
    ) : Call<DeleteUserResponse>

    @DELETE("deleteBookmark")
    fun deleteBookmark(
        @Header("Authorization") token: String,
        @Query("bookmarkId") bookmarkId: String,
    ) : Call<DeleteBookmarkResponse>

    @GET("getAllChats")
    fun getAllChats(
        @Header("Authorization") token: String
    ) : Call<GetAllChatsResponse>

    @POST("sendMessage")
    fun sendMessage(
        @Header("Authorization") token: String,
        @Body req: SendMessageRequest
    ): Call<SendMessageResponse>

    @GET("getAllMessages")
    fun getAllMessages(
        @Header("Authorization") token: String,
        @Query("chatId") chatId: String,
        @Query("pages") pages: String
    ): Call<GetAllMessagesResponse>

    @DELETE("deleteMessage")
    fun deleteMessage(
        @Header("Authorization") token: String,
        @Query("messageId") messageId: String
    ) : Call<DeleteMessageResponse>

    @POST("createJob")
    fun createJob(
        @Header("Authorization") token: String,
        @Body req: CreateJobRequest
    ): Call<CreateJobResponse>

    @DELETE("deleteJob")
    fun deleteJob(
        @Header("Authorization") token: String,
        @Query("jobId") jobId: String
    ) : Call<DeleteJobResponse>

    @PUT("updateJob")
    fun updateJob(
        @Header("Authorization") token: String,
        @Body req: UpdateJobRequest
    ): Call<UpdateJobResponse>
}