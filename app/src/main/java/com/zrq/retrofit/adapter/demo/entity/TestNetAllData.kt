package com.zrq.retrofit.adapter.demo.entity

import com.zrq.retrofit.adapter.demo.entity.apiopen.GetImagesData
import com.zrq.retrofit.adapter.demo.entity.apiopen.GetTimeData
import com.zrq.retrofit.adapter.demo.entity.wanandroid.FeedArticleListData

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2022/8/10 上午10:24
 */
data class TestNetAllData(
    val getImagesData: GetImagesData,
    val getTimeData: GetTimeData,
    val feedArticleListData: FeedArticleListData
)