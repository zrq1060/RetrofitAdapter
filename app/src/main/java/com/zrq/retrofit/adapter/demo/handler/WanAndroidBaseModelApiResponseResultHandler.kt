package com.zrq.retrofit.adapter.demo.handler

import com.zrq.retrofit.adapter.ApiResponse
import com.zrq.retrofit.adapter.ApiResponseResultHandler
import com.zrq.retrofit.adapter.demo.RulesException
import com.zrq.retrofit.adapter.demo.entity.wanandroid.WanAndroidBaseModel
import com.zrq.retrofit.adapter.exception.ResponseBodyEmptyException
import com.zrq.retrofit.adapter.exception.ResponseCodeErrorException
import retrofit2.Response

/**
 * 描述：[WanAndroidBaseModel]逻辑处理类，返回值不为空，并且[WanAndroidBaseModel.errorCode]为0，并且[WanAndroidBaseModel.data]不为空，代表成功
 *
 * @author zhangrq
 * createTime 2021/5/17 15:15
 */
class WanAndroidBaseModelApiResponseResultHandler : ApiResponseResultHandler {
    override fun priority(): Int {
        return 0
    }

    override fun shouldHandle(resultClass: Class<*>): Boolean {
        return resultClass == WanAndroidBaseModel::class.java
    }

    override fun <T> handleOnResponse(response: Response<T>): ApiResponse<T> {
        return if (response.isSuccessful) {
            // 网络成功
            val body = response.body()
            if (body != null) {
                // body不为空
                val baseData = body as WanAndroidBaseModel<*>
                val baseDataCode = baseData.errorCode
                if (baseDataCode == 0) {
                    // 公司规则成功
                    val data = baseData.data
                    if (data != null) {
                        // data有值
                        ApiResponse.success(body)
                    } else {
                        // data无值
                        ApiResponse.exception(RulesException("BaseData data is null"))
                    }
                } else {
                    // 公司规则失败
                    if (baseDataCode == null) {
                        ApiResponse.exception(RulesException("BaseData code is null"))
                    } else {
                        ApiResponse.error(baseDataCode, baseData.errorMsg ?: "")
                    }
                }
            } else {
                // body为空
                ApiResponse.exception(ResponseBodyEmptyException())
            }
        } else {
            // 网络失败
            ApiResponse.exception(ResponseCodeErrorException(response.code()))
        }
    }

    override fun <T> handleOnFailure(t: Throwable): ApiResponse<T> {
        return ApiResponse.exception(t)
    }
}