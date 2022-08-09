package com.zrq.retrofit.adapter.demo.handler

import com.zrq.retrofit.adapter.ApiResponse
import com.zrq.retrofit.adapter.ApiResponseResultHandler
import com.zrq.retrofit.adapter.demo.RulesException
import com.zrq.retrofit.adapter.demo.entity.apiopen.ApiOpenBaseModel
import com.zrq.retrofit.adapter.exception.ResponseBodyEmptyException
import com.zrq.retrofit.adapter.exception.ResponseCodeErrorException
import retrofit2.Response

/**
 * 描述：[ApiOpenBaseModel]逻辑处理类，返回值不为空，并且[ApiOpenBaseModel.code]为200，并且[ApiOpenBaseModel.result]不为空，代表成功
 *
 * @author zhangrq
 * createTime 2021/5/17 15:15
 */
class ApiOpenBaseModelApiResponseResultHandler : ApiResponseResultHandler {
    override fun priority(): Int {
        return 0
    }

    override fun shouldHandle(resultClass: Class<*>): Boolean {
        return resultClass == ApiOpenBaseModel::class.java
    }

    override fun <T> handleOnResponse(response: Response<T>): ApiResponse<T> {
        return if (response.isSuccessful) {
            // 网络成功
            val body = response.body()
            if (body != null) {
                // body不为空
                val baseResult = body as ApiOpenBaseModel<*>
                val baseResultCode = baseResult.code
                if (baseResultCode == 200) {
                    // 公司规则成功
                    val result = baseResult.result
                    if (result != null) {
                        // result有值
                        ApiResponse.success(body)
                    } else {
                        // result无值
                        ApiResponse.exception(RulesException("BaseResult result is null"))
                    }
                } else {
                    // 公司规则失败
                    if (baseResultCode == null) {
                        ApiResponse.exception(RulesException("BaseResult code is null"))
                    } else {
                        ApiResponse.error(baseResultCode, baseResult.message ?: "")
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