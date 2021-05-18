package com.zrq.retrofit.adapter.demo

/**
 * 描述：网络异常相关
 *
 * @author zhangrq
 * createTime 2021/5/17 19:04
 */
class ResponseBodyEmptyException : RuntimeException("response body empty")

class RulesException(message: String) : RuntimeException(message)