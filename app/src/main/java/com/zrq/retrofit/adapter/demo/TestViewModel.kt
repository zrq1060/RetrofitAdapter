package com.zrq.retrofit.adapter.demo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zrq.retrofit.adapter.*
import kotlinx.coroutines.launch

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2021/5/17 14:22
 */
@Suppress("FunctionName")
class TestViewModel : ViewModel() {
    private val repository = TestRepository()
    val hintText = MutableLiveData<String>()
    val loading = MutableLiveData(false)

    /**
     * 获取ApiOpen接口列表数据，返回ApiResponse，命令式处理。
     */
    fun getApiOpenList_ApiResponse_Imperative() {
        viewModelScope.launch {
            showLoading()
            when (val result = repository.getApiOpenList_ApiResponse(1)) {
                is ApiResponse.Success -> {
                    // 成功，返回数据result.data为可空ApiOpenBaseModel类（范围大）。
                    setResultHint("Success=${result.data}")
                }
//                is ApiResponse.Failure -> {
//                    // 失败，包含下面的Error、Exception。
//                    setResultHint("Failure")
//                }
                is ApiResponse.Failure.Error -> {
                    // 失败-错误，服务器返回的错误code及message信息。
                    setResultHint("Failure=Error=code=${result.code}=message=${result.message}")
                }
                is ApiResponse.Failure.Exception -> {
                    // 失败-异常
                    setResultHint("Failure=Exception=throwable=${result.throwable}")
                }
            }
            hideLoading()
        }
    }

    /**
     * 获取ApiOpen接口列表数据，返回ApiResponse，声明式处理。
     */
    fun getApiOpenList_ApiResponse_Declarative() {
        viewModelScope.launch {
            showLoading()
            repository.getApiOpenList_ApiResponse(1)
                .onSuccess {
                    // 成功，返回数据this.data为可空ApiOpenBaseModel类（范围大）。
                    setResultHint("Success=${data}")
                    hideLoading()
                }.onFailure {
                    // 失败，包含下面的Error、Exception。
                    setResultHint("Failure")
                    hideLoading()
                }.onError {
                    // 失败-错误，服务器返回的错误code及message信息。
                    setResultHint("Failure=Error=code=${code}=message=${message}")
                }.onException {
                    // 失败-异常
                    setResultHint("Failure=Exception=throwable=${throwable}")
                }
        }
    }

    /**
     * 获取ApiOpen接口列表数据，返回ApiResponse，并指定规则。
     */
    fun getApiOpenList_ApiResponse_ApiResponseHandler() {
        viewModelScope.launch {
            showLoading()
            repository.getApiOpenList_ApiResponse_ApiResponseHandler(1)
                .onSuccess {
                    // 成功，返回数据this.data为可空ApiOpenBaseModel类（范围大）。
                    setResultHint("Success=${data}")
                    hideLoading()
                }.onFailure {
                    // 失败，包含下面的Error、Exception。
                    setResultHint("Failure")
                    hideLoading()
                }.onError {
                    // 失败-错误，服务器返回的错误code及message信息。
                    setResultHint("Failure=Error=code=${code}=message=${message}")
                }.onException {
                    // 失败-异常
                    setResultHint("Failure=Exception=throwable=${throwable}")
                }
        }
    }

    /**
     * 获取ApiOpen接口列表数据，返回ApiResponse，并成功后直接获取map转换后的值（此为内部的list）。
     */
    fun getApiOpenList_ApiResponse_Map() {
        viewModelScope.launch {
            showLoading()
            repository.getApiOpenList_ApiResponse_Map(1)
                .onSuccess {
                    // 成功，返回数据this.data为可空List<GetImagesItemData>数据（范围合适）。
                    setResultHint("Success=${data!!}")
                    hideLoading()
                }.onFailure {
                    // 失败，包含下面的Error、Exception。
                    setResultHint("Failure")
                    hideLoading()
                }.onError {
                    // 失败-错误，服务器返回的错误code及message信息。
                    setResultHint("Failure=Error=code=${code}=message=${message}")
                }.onException {
                    // 失败-异常
                    setResultHint("Failure=Exception=throwable=${throwable}")
                }
        }
    }

    /**
     * 获取ApiOpen接口列表数据，返回Result，并且成功的Result.Success内data可能为空。
     */
    fun getApiOpenList_Result() {
        viewModelScope.launch {
            showLoading()
            when (val result = repository.getApiOpenList_Result(1)) {
                is Result.Success -> {
                    // 成功，返回数据result.data为可空ApiOpenBaseModel类（范围大）。
                    setResultHint("Success=${result.data}")
                }
                is Result.Failure -> {
                    // 失败
                    setResultHint("Failure=isError=${result.isError}=code=${result.code}=message=${result.message}")
                }
            }
            hideLoading()
        }
    }

    /**
     * 获取ApiOpen接口列表数据，返回Result。并且成功的Result.Success内的data不为空。
     */
    fun getApiOpenList_Result_NotNull() {
        viewModelScope.launch {
            showLoading()
            when (val result = repository.getApiOpenList_Result_NotNull(1)) {
                is Result.Success -> {
                    // 成功，返回数据result.data为不为空的ApiOpenBaseModel类（范围大）。
                    setResultHint("Success=${result.data}")
                }
                is Result.Failure -> {
                    // 失败
                    setResultHint("Failure=isError=${result.isError}=code=${result.code}=message=${result.message}")
                }
            }
            hideLoading()
        }
    }

    /**
     * 获取ApiOpen接口列表数据，返回Result，并且成功的Result.Success内的data（ApiOpenBaseModel）内的result不为空，并返回此。
     */
    fun getApiOpenList_Result_ApiOpen() {
        viewModelScope.launch {
            showLoading()
            when (val result = repository.getApiOpenList_Result_ApiOpen(1)) {
                is Result.Success -> {
                    // 成功，返回数据result.data为GetImagesData类（范围合适）。
                    setResultHint("Success=${result.data}")
                }
                is Result.Failure -> {
                    // 失败
                    setResultHint("Failure=isError=${result.isError}=code=${result.code}=message=${result.message}")
                }
            }
            hideLoading()
        }
    }

    /**
     * 获取WanAndroid接口列表数据，返回ApiResponse，命令式处理。
     */
    fun getWanAndroidList_ApiResponse_Imperative() {
        viewModelScope.launch {
            showLoading()
            when (val result = repository.getWanAndroidList_ApiResponse(1)) {
                is ApiResponse.Success -> {
                    // 成功，返回数据result.data为可空WanAndroidBaseModel类（范围大）。
                    setResultHint("Success=${result.data}")
                }
                is ApiResponse.Failure -> {
                    // 失败，包含Error、Exception。
                    setResultHint("Failure")
                }
            }
            hideLoading()
        }
    }

    /**
     * 获取WanAndroid接口列表数据，返回Result，并且成功的Result.Success内的data（WanAndroidBaseModel）内的data不为空，并返回此。
     */
    fun getWanAndroidList_Result_WanAndroid() {
        viewModelScope.launch {
            showLoading()
            when (val result = repository.getWanAndroidList_Result_WanAndroid(1)) {
                is Result.Success -> {
                    // 成功，返回数据result.data为FeedArticleListData类（范围合适）。
                    setResultHint("Success=${result.data}")
                }
                is Result.Failure -> {
                    // 失败
                    setResultHint("Failure=isError=${result.isError}=code=${result.code}=message=${result.message}")
                }
            }
            hideLoading()
        }
    }

    /**
     * 获取A、B线性依次执行，返回网络B的数据，未处理空。
     */
    fun getABLinearSimple() {
        viewModelScope.launch {
            showLoading()
            when (val result = repository.getABLinearSimple(1)) {
                is Result.Success -> {
                    // 成功，返回数据result.data为GetImagesData类（范围合适）。
                    setResultHint("Success=${result.data}")
                }
                is Result.Failure -> {
                    // 失败
                    setResultHint("Failure=isError=${result.isError}=code=${result.code}=message=${result.message}")
                }
            }
            hideLoading()
        }
    }

    /**
     * 获取A、B线性依次执行，返回网络B的数据，处理了空。
     */
    fun getABLinear() {
        viewModelScope.launch {
            showLoading()
            when (val result = repository.getABLinear(1)) {
                is Result.Success -> {
                    // 成功，返回数据result.data为GetImagesData类（范围合适）。
                    setResultHint("Success=${result.data}")
                }
                is Result.Failure -> {
                    // 失败
                    setResultHint("Failure=isError=${result.isError}=code=${result.code}=message=${result.message}")
                }
            }
            hideLoading()
        }
    }

    /**
     * 获取A、B、C并发执行，返回三个的整体数据的结果。
     */
    fun getABCAsync() {
        viewModelScope.launch {
            showLoading()
            when (val result = repository.getABCAsync(1)) {
                is Result.Success -> {
                    // 成功，返回数据result.data为三个接口数据的集合TestNetAllData类（范围合适）。
                    setResultHint("Success=${result.data}")
                }
                is Result.Failure -> {
                    // 失败
                    setResultHint("Failure=isError=${result.isError}=code=${result.code}=message=${result.message}")
                }
            }
            hideLoading()
        }
    }

    /**
     * 清空提示
     */
    fun clearHint() {
        hintText.value = ""
    }

    private fun setResultHint(result: String) {
        Log.e("TestViewModel", "result==$result")
        hintText.value = result
    }

    private fun showLoading() {
        loading.value = true
    }

    private fun hideLoading() {
        loading.value = false
    }
}