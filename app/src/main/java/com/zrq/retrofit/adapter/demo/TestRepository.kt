package com.zrq.retrofit.adapter.demo

import com.zrq.retrofit.adapter.ApiResponse
import com.zrq.retrofit.adapter.coroutines.CoroutinesResponseCallAdapterFactory
import com.zrq.retrofit.adapter.demo.entity.BaseData
import com.zrq.retrofit.adapter.demo.entity.BaseResult
import com.zrq.retrofit.adapter.demo.entity.FeedArticleData
import com.zrq.retrofit.adapter.demo.entity.TestNetItem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2021/5/17 14:22
 */
class TestRepository {

    private val api: TestService = Retrofit.Builder()
        .baseUrl("https://api.apiopen.top/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory.create())
        .build()
        .create(TestService::class.java)

    suspend fun getJoke(page: Int): ApiResponse<BaseResult<List<TestNetItem>>> {
        return api.getJoke(page)
    }

    suspend fun getFeedArticleList(page: Int): ApiResponse<BaseData<FeedArticleData>> {
        return api.getFeedArticleList(page)
    }
}