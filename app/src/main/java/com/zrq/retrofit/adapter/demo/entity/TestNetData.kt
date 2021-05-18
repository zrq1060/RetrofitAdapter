package com.zrq.retrofit.adapter.demo.entity

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2021/5/17 16:45
 */
data class TestNetItem(val sid: String?, val text: String?)

data class TestNetDes(val sid: String?, val name: String?, val text: String?)

data class TestNetAllData(val testNetItem: List<TestNetItem>?, val testNetDes: TestNetDes?)