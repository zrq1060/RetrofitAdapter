package com.zrq.retrofit.adapter

import kotlin.reflect.KClass


/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2021/6/1 16:14
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiResponseHandler(val value: KClass<out ApiResponseAnnotationHandler>)