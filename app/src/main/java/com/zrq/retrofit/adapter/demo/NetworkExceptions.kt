package com.zrq.retrofit.adapter.demo

import com.zrq.retrofit.adapter.exception.ResponseBodyEmptyException
import com.zrq.retrofit.adapter.exception.ResponseCodeErrorException
import java.net.UnknownHostException

/**
 * 描述：网络异常相关
 *
 * @author zhangrq
 * createTime 2021/5/17 19:04
 */

class RulesException(message: String) : RuntimeException(message)

const val CODE_EXCEPTION_UNKNOWN = -100 // unknown
const val CODE_EXCEPTION_RESPONSE_CODE_ERROR = -101 // response code error
const val CODE_EXCEPTION_RESPONSE_BODY_EMPTY = -102 // response body empty
const val CODE_EXCEPTION_RULES_ERROR = -103 // rules error
const val CODE_EXCEPTION_NETWORK_ERROR = -104 // network_error


fun Throwable.toCodeMessage(): Pair<Int, String> {
    return when (this) {
        // 响应码错误
        is ResponseCodeErrorException -> Pair(CODE_EXCEPTION_RESPONSE_CODE_ERROR, "响应码错误, code=$code")
        // 响应body为空
        is ResponseBodyEmptyException -> Pair(CODE_EXCEPTION_RESPONSE_BODY_EMPTY, "响应体为空")
        // 规则异常
        is RulesException -> Pair(CODE_EXCEPTION_RULES_ERROR, message.toString())
        // 无网络
        is UnknownHostException -> Pair(CODE_EXCEPTION_NETWORK_ERROR, "无网络")
        // 未知
        else -> Pair(CODE_EXCEPTION_UNKNOWN, "unknown Exception ${toString()}")
    }
}