package com.zrq.retrofit.adapter.demo

import com.zrq.retrofit.adapter.ApiResponse
import com.zrq.retrofit.adapter.asyncAll
import com.zrq.retrofit.adapter.coroutines.CoroutinesResponseCallAdapterFactory
import com.zrq.retrofit.adapter.demo.entity.TestNetAllData
import com.zrq.retrofit.adapter.demo.entity.apiopen.ApiOpenBaseModel
import com.zrq.retrofit.adapter.demo.entity.apiopen.GetImagesData
import com.zrq.retrofit.adapter.demo.entity.apiopen.GetTimeData
import com.zrq.retrofit.adapter.demo.entity.wanandroid.FeedArticleListData
import com.zrq.retrofit.adapter.demo.entity.wanandroid.WanAndroidBaseModel
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

    /* -------------------ApiOpen接口------------------- */
    // 获取ApiOpen接口列表数据，返回ApiResponse。
    suspend fun getApiOpenList_ApiResponse(page: Int, size: Int = 10) =
        api.getImages(page, size)

    // 获取ApiOpen接口列表数据，返回ApiResponse，并指定规则。
    suspend fun getApiOpenList_ApiResponse_ApiResponseHandler(page: Int, size: Int = 10) =
        api.getImagesApiResponseHandler(page, size)

    // 获取ApiOpen接口列表数据，返回ApiResponse，并成功后直接获取map转换后的值（此为内部的list）。
    suspend fun getApiOpenList_ApiResponse_Map(page: Int, size: Int = 10) =
        api.getImages(page, size).map { it?.result?.list }

    // 获取ApiOpen接口列表数据，返回Result，并且成功的Result.Success内data可能为空。
    // -为什么ApiResponse要转换为Result？
    // --因为Repository类内部网络请求可能使用ApiResponse作为返回数据、可能使用直接返回的结果作为返回数据、可能使用的数据库的结果作为返回数据，达不到统一，
    // --为了统一，所以需要使用Result类，作为统一Repository类方法返回值标准。
    suspend fun getApiOpenList_Result(page: Int, size: Int = 10) =
        api.getImages(page, size).toResult()

    // 获取ApiOpen接口列表数据，返回Result。并且成功的Result.Success内的data不为空。
    suspend fun getApiOpenList_Result_NotNull(page: Int, size: Int = 10) =
        api.getImages(page, size).toResultNotNull()

    // 获取ApiOpen接口列表数据，返回Result，并且成功的Result.Success内的data（ApiOpenBaseModel）内的result不为空，并返回此。
    suspend fun getApiOpenList_Result_ApiOpen(page: Int, size: Int = 10) =
        api.getImages(page, size).toResultApiOpen()

    /* -------------------WanAndroid接口------------------- */
    // 获取WanAndroid接口列表数据，返回ApiResponse。
    suspend fun getWanAndroidList_ApiResponse(page: Int) =
        api.getFeedArticleList(page)

    // 获取WanAndroid接口列表数据，返回Result，并且成功的Result.Success内的data（WanAndroidBaseModel）内的data不为空，并返回此。
    suspend fun getWanAndroidList_Result_WanAndroid(page: Int) =
        api.getFeedArticleList(page).toResultWanAndroid()

    /* -------------------例子：网络B需要网络A的结果，A、B线性依次执行，返回网络B的数据------------------- */
    /**
     * [linear]可链接多个，例如：api.getA().linear{ api.getB() }.linear{ api.getC() }
     * A、B、C依次线性执行，A失败返回A的错误信息，B、C不会执行。B失败返回B的错误信息，C不会执行。
     */
    suspend fun getABLinearSimple(page: Int, size: Int = 10) = api.getImages(page, size).linear {
        // 为了演示使用方便，这里没有判空。
        // 假设B接口需要A接口的id值
        val id = this!!.result!!.list!!.first().id
        api.getTime()
    }.toResultApiOpen()

    // 在getABLinearSimple方法的基础了，处理了空。
    suspend fun getABLinear(page: Int, size: Int = 10) = api.getImages(page, size).linear {
        // 处理了空
        val id = this?.result?.list?.firstOrNull()?.id
        if (id != null) {
            // 数据ok，请求第二个接口。
            api.getTime()
        } else {
            // 数据不ok，返回失败。
            ApiResponse.exception(RulesException("数据错误"))
        }
    }.toResultApiOpen()

    /* -------------------A、B、C并发执行，返回三个的整体数据------------------- */

    suspend fun getABCAsync(page: Int, size: Int = 10) =
        asyncAll(
            { api.getImages(page, size) },// 接口A
            { api.getTime() },// 接口B
            { api.getFeedArticleList(page) },// 接口C
            onSuccess = { values ->
                // -A成功，走的ApiOpenApiResponseResultHandler规则，result一定不为空。
                val getImagesData = (values[0] as ApiOpenBaseModel<GetImagesData>).result!!
                // -B成功，走的ApiOpenApiResponseResultHandler规则，result一定不为空。
                val getTimeData = (values[1] as ApiOpenBaseModel<GetTimeData>).result!!
                // -C成功，走的WanAndroidApiResponseResultHandler规则，data一定不为空。
                val feedArticleListData =
                    (values[2] as WanAndroidBaseModel<FeedArticleListData>).data!!
                // 封装整体数据，进行返回。
                ApiResponse.success(TestNetAllData(getImagesData, getTimeData, feedArticleListData))
            }).toResultNotNull()
}