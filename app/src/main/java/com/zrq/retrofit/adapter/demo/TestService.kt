package com.zrq.retrofit.adapter.demo

import com.zrq.retrofit.adapter.ApiResponse
import com.zrq.retrofit.adapter.ApiResponseHandler
import com.zrq.retrofit.adapter.demo.entity.apiopen.ApiOpenBaseResult
import com.zrq.retrofit.adapter.demo.entity.apiopen.TestNetDes
import com.zrq.retrofit.adapter.demo.entity.apiopen.TestNetItem
import com.zrq.retrofit.adapter.demo.entity.wanandroid.FeedArticleData
import com.zrq.retrofit.adapter.demo.entity.wanandroid.WanAndroidBaseData
import com.zrq.retrofit.adapter.demo.handler.NetworkApiResponseHandler
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2021/5/17 14:22
 */
interface TestService {
    /**
     * 获取列表
     */
    @GET("getJoke")
    suspend fun getJoke(@Query("page") page: Int): ApiResponse<ApiOpenBaseResult<List<TestNetItem>>>

    /**
     * 获取列表
     */
    @GET("getJoke")
    @ApiResponseHandler(NetworkApiResponseHandler::class)
    suspend fun getJokeUseApiResponseHandler(@Query("page") page: Int): ApiResponse<ApiOpenBaseResult<List<TestNetItem>>>

    /**
     * 获取详情
     */
    @GET("getSingleJoke")
    suspend fun getSingleJoke(@Query("sid") sid: String): ApiResponse<ApiOpenBaseResult<TestNetDes>>

    /**
     * WanAndroid，获取feed文章列表
     */
    @GET("https://www.wanandroid.com/article/list/{num}/json")
    suspend fun getFeedArticleList(@Path("num") num: Int): ApiResponse<WanAndroidBaseData<FeedArticleData>>
}