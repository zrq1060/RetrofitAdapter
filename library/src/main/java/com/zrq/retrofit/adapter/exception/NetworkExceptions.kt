package com.zrq.retrofit.adapter.exception

/**
 * 描述：网络异常相关
 *
 * @author zhangrq
 * createTime 2021/5/17 19:04
 */
class ResponseCodeErrorException(val code: Int) : RuntimeException("response code error, code=$code")

class ResponseBodyEmptyException : RuntimeException("response body empty")