package com.zrq.retrofit.adapter.demo

import com.zrq.retrofit.adapter.ApiResponse
import com.zrq.retrofit.adapter.ApiResponseHandler
import com.zrq.retrofit.adapter.demo.entity.apiopen.ApiOpenBaseModel
import com.zrq.retrofit.adapter.demo.entity.apiopen.GetImagesData
import com.zrq.retrofit.adapter.demo.entity.apiopen.TestNetDes
import com.zrq.retrofit.adapter.demo.entity.wanandroid.FeedArticleListData
import com.zrq.retrofit.adapter.demo.entity.wanandroid.WanAndroidBaseModel
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
     * 获取图片列表
     */
    @GET("api/getImages")
    suspend fun getImages(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ApiResponse<ApiOpenBaseModel<GetImagesData>>

    /**
     * 获取图片列表-使用ApiResponseHandler注解指定的逻辑
     */
    @GET("getJoke")
    @ApiResponseHandler(NetworkApiResponseHandler::class)
    suspend fun getImagesApiResponseHandler(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ApiResponse<ApiOpenBaseModel<GetImagesData>>

    /**
     * 获取详情
     */
    @GET("getSingleJoke")
    suspend fun getSingleJoke(@Query("sid") sid: String): ApiResponse<ApiOpenBaseModel<TestNetDes>>

    /**
     * WanAndroid，获取feed文章列表
     */
    @GET("https://www.wanandroid.com/article/list/{num}/json")
    suspend fun getFeedArticleList(@Path("num") num: Int): ApiResponse<WanAndroidBaseModel<FeedArticleListData>>
}