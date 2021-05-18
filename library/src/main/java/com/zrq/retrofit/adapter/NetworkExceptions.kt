package com.zrq.retrofit.adapter

/**
 * 描述：网络异常相关
 *
 * @author zhangrq
 * createTime 2021/5/17 19:04
 */
class ResponseCodeErrorException(code: Int) : RuntimeException("response code error, code=$code")