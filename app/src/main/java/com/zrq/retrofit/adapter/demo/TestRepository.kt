package com.zrq.retrofit.adapter.demo

import android.text.TextUtils
import com.zrq.retrofit.adapter.ApiResponse
import com.zrq.retrofit.adapter.asyncAll
import com.zrq.retrofit.adapter.coroutines.CoroutinesResponseCallAdapterFactory
import com.zrq.retrofit.adapter.demo.entity.apiopen.ApiOpenBaseResult
import com.zrq.retrofit.adapter.demo.entity.apiopen.TestNetAllData
import com.zrq.retrofit.adapter.demo.entity.apiopen.TestNetDes
import com.zrq.retrofit.adapter.demo.entity.apiopen.TestNetItem
import com.zrq.retrofit.adapter.demo.entity.wanandroid.FeedArticleData
import com.zrq.retrofit.adapter.demo.entity.wanandroid.WanAndroidBaseData
import com.zrq.retrofit.adapter.linear
import com.zrq.retrofit.adapter.map
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2021/5/17 14:22
 */
@Suppress("FunctionName", "UNCHECKED_CAST")
class TestRepository {

    private val api: TestService = Retrofit.Builder()
        .baseUrl("https://api.apiopen.top/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory.create())
        .build()
        .create(TestService::class.java)

    /* -------------------BaseResult------------------- */
    suspend fun getBaseResultList_ApiResponse(page: Int): ApiResponse<ApiOpenBaseResult<List<TestNetItem>>> {
        return api.getJoke(page)
    }

    suspend fun getBaseResultList_ApiResponse_ApiResponseHandler(page: Int): ApiResponse<ApiOpenBaseResult<List<TestNetItem>>> {
        return api.getJokeUseApiResponseHandler(page)
    }

    suspend fun getBaseResultList_ApiResponse_Map(page: Int) =
        api.getJoke(page).map { it!!.result!! }

    suspend fun getBaseResultList_Result(page: Int): Result<ApiOpenBaseResult<List<TestNetItem>>?> {
        return api.getJoke(page).toResult()
    }

    suspend fun getBaseResultList_Result_NotNull(page: Int): Result<ApiOpenBaseResult<List<TestNetItem>>> {
        return api.getJoke(page).toResultNotNull()
    }

    suspend fun getBaseResultList_Result_BaseResultOfResult(page: Int): Result<List<TestNetItem>> {
        return api.getJoke(page).toResultBaseResultOfResult()
    }

    /* -------------------BaseData------------------- */
    suspend fun getBaseDataData_ApiResponse(page: Int): ApiResponse<WanAndroidBaseData<FeedArticleData>> {
        return api.getFeedArticleList(page)
    }

    suspend fun getBaseDataData_Result_BaseDataOfData(page: Int): Result<FeedArticleData> {
        return api.getFeedArticleList(page).toResultBaseDataOfData()
    }

    /* -------------------A、B线性依次执行，返回B的数据------------------- */
    /**
     * [linear]可链接多个，例如：api.getA().linear{ api.getB() }.linear{ api.getC() }
     */
    suspend fun getABLinearSimple(page: Int) = api.getJoke(page).linear {
        // 为了演示使用方便，这里没有判空
        api.getSingleJoke(this!!.result!!.first().sid!!)
    }.toResult()

    suspend fun getABLinear(page: Int) = api.getJoke(page).linear {
        // 为了演示使用方便，这里没有判空
        val sid = this?.result?.firstOrNull()?.sid ?: ""
        if (!TextUtils.isEmpty(sid)) {
            // 数据ok，请求第二个接口
            api.getSingleJoke(sid)
        } else {
            // 数据不ok，返回失败
            ApiResponse.exception(RulesException(""))
        }
    }.toResultBaseResultOfResult()


    /* -------------------A、B、C并发执行，返回三个的整体数据------------------- */
    suspend fun getABCAsyncSimple(page: Int) =
        asyncAll(
            { api.getJoke(page) },// 接口A
            { api.getSingleJoke("31577089") },// 接口B
            { api.getFeedArticleList(page) },// 接口C
            onSuccess = { values ->
                // 为了演示使用方便，这里没有判断
                val testNetItemList = (values[0] as? ApiOpenBaseResult<List<TestNetItem>>)?.result
                val testNetDes = (values[1] as? ApiOpenBaseResult<TestNetDes>)?.result
                val feedArticleData = (values[2] as? WanAndroidBaseData<FeedArticleData>)?.data
                ApiResponse.success(TestNetAllData(testNetItemList, testNetDes, feedArticleData))
            }).toResult()

    suspend fun getABCAsync(page: Int) =
        asyncAll(
            { api.getJoke(page) },// 接口A
            { api.getSingleJoke("31577089") },// 接口B
            { api.getFeedArticleList(page) },// 接口C
            onSuccess = { values ->
                // 为了演示使用方便，这里没有判断
                // -A成功，走的BaseResultApiResponseCallHandler规则，result一定不为空
                val testNetItemList = (values[0] as ApiOpenBaseResult<List<TestNetItem>>).result!!
                // -B成功，走的BaseResultApiResponseCallHandler规则，result一定不为空
                val testNetDes = (values[1] as ApiOpenBaseResult<TestNetDes>).result!!
                // -C成功，走的BaseDataApiResponseCallHandler规则，data一定不为空
                val feedArticleData = (values[2] as WanAndroidBaseData<FeedArticleData>).data!!
                ApiResponse.success(TestNetAllData(testNetItemList, testNetDes, feedArticleData))
            }).toResultNotNull()
}