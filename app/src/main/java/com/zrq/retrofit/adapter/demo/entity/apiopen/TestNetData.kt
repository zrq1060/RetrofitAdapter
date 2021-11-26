package com.zrq.retrofit.adapter.demo.entity.apiopen

import com.zrq.retrofit.adapter.demo.entity.wanandroid.FeedArticleData

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2021/5/17 16:45
 */
data class TestNetItem(val sid: String?, val text: String?)

data class TestNetDes(val sid: String?, val name: String?, val text: String?)

data class TestNetAllData(
    val testNetItemList: List<TestNetItem>?,
    val testNetDes: TestNetDes?,
    val feedArticleData: FeedArticleData?
)