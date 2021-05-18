package com.zrq.retrofit.adapter.demo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zrq.retrofit.adapter.ApiResponse
import kotlinx.coroutines.launch

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2021/5/17 14:22
 */
class TestViewModel : ViewModel() {
    private val repository = TestRepository()
    val hintText = MutableLiveData<String>()

    /**
     * 获取一个网络
     */
    fun getOneNetRequestData1() {
        viewModelScope.launch {
            when (val result = repository.getJoke(1)) {
                is ApiResponse.Success -> {
                    // 网络成功，并且规则成功
                    setResultHint(true, "Success=${result.data}")
                }
                is ApiResponse.Failure.Error -> {
                    // 失败-错误，服务器返回的错误code及message信息
                    setResultHint(true, "Failure=Error=${result}")
                }
                is ApiResponse.Failure.Exception -> {
                    // 失败-异常，
                    setResultHint(true, "Failure=Exception=${result.throwable}")
                }
            }
        }
    }
    /**
     * 获取一个网络
     */
    fun getOneNetRequestData2() {
        viewModelScope.launch {
            when (val result = repository.getFeedArticleList(1)) {
                is ApiResponse.Success -> {
                    // 网络成功，并且规则成功
                    setResultHint(true, "Success=${result.data}")
                }
                is ApiResponse.Failure.Error -> {
                    // 失败-错误，服务器返回的错误code及message信息
                    setResultHint(true, "Failure=Error=${result}")
                }
                is ApiResponse.Failure.Exception -> {
                    // 失败-异常，
                    setResultHint(true, "Failure=Exception=${result.throwable}")
                }
            }
        }
    }
    /**
     * 清空提示
     */
    fun clearHint() {
        hintText.value = ""
    }

    private fun setResultHint(isClear: Boolean = false, result: String) {
        Log.e("TestViewModel", "result==$result")
        if (isClear) {
            hintText.value = "$result\n"
        } else {
            hintText.value = "${hintText.value} $result\n"
        }
    }
}