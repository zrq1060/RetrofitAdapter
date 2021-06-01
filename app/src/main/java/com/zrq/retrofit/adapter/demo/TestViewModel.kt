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

    /**
     * 获取BaseResult的列表数据，返回ApiResponse，命令式处理
     */
    fun getBaseResultList_ApiResponse_Imperative() {
        viewModelScope.launch {
            when (val result = repository.getBaseResultList_ApiResponse(1)) {
                is ApiResponse.Success -> {
                    // 成功，返回数据为BaseResult
                    setResultHint("Success=${result.data?.result}")
                }
//                is ApiResponse.Failure -> {
//                    // 失败，包含Error、Exception
//                    setResultHint("Failure")
//                }
                is ApiResponse.Failure.Error -> {
                    // 失败-错误，服务器返回的错误code及message信息
                    setResultHint("Failure=Error=code=${result.code}=message=${result.message}")
                }
                is ApiResponse.Failure.Exception -> {
                    // 失败-异常
                    setResultHint("Failure=Exception=throwable=${result.throwable}")
                }
            }
        }
    }

    /**
     * 获取BaseResult的列表数据，返回ApiResponse，声明式处理
     */
    fun getBaseResultList_ApiResponse_Declarative() {
        viewModelScope.launch {
            repository.getBaseResultList_ApiResponse(1)
                .onSuccess {
                    // 成功，返回数据为BaseResult
                    setResultHint("Success=${data?.result}")
                }.onFailure {
                    // 失败，包含Error、Exception
                    setResultHint("Failure")
                }.onError {
                    // 失败-错误，服务器返回的错误code及message信息
                    setResultHint("Failure=Error=code=${code}=message=${message}")
                }.onException {
                    // 失败-异常
                    setResultHint("Failure=Exception=throwable=${throwable}")
                }
        }
    }

    /**
     * 获取BaseResult的列表数据，返回Result，返回数据为BaseResult，BaseResult可能为空
     */
    fun getBaseResultList_Result() {
        viewModelScope.launch {
            when (val result = repository.getBaseResultList_Result(1)) {
                is Result.Success -> {
                    // 成功，返回数据为BaseResult，BaseResult可能为空
                    setResultHint("Success=${result.data?.result}")
                }
                is Result.Failure -> {
                    // 失败
                    setResultHint("Failure=isError=${result.isError}=code=${result.code}=message=${result.message}")
                }
            }
        }
    }

    /**
     * 获取BaseResult的列表数据，返回Result，返回数据为BaseResult，BaseResult不为空
     */
    fun getBaseResultList_Result_NotNull() {
        viewModelScope.launch {
            when (val result = repository.getBaseResultList_Result_NotNull(1)) {
                is Result.Success -> {
                    // 成功，返回数据为BaseResult，BaseResult不为空
                    setResultHint("Success=${result.data.result}")
                }
                is Result.Failure -> {
                    // 失败
                    setResultHint("Failure=isError=${result.isError}=code=${result.code}=message=${result.message}")
                }
            }
        }
    }

    /**
     * 获取BaseResult的列表数据，返回Result，返回数据为BaseResult内的result
     */
    fun getBaseResultList_Result_BaseResultOfResult() {
        viewModelScope.launch {
            when (val result = repository.getBaseResultList_Result_BaseResultOfResult(1)) {
                is Result.Success -> {
                    // 成功，返回数据为BaseResult内的result
                    setResultHint("Success=${result.data}")
                }
                is Result.Failure -> {
                    // 失败
                    setResultHint("Failure=isError=${result.isError}=code=${result.code}=message=${result.message}")
                }
            }
        }
    }

    /**
     * 获取BaseData的数据，返回ApiResponse，命令式处理
     */
    fun getBaseDataData_ApiResponse_Imperative() {
        viewModelScope.launch {
            when (val result = repository.getBaseDataData_ApiResponse(1)) {
                is ApiResponse.Success -> {
                    // 成功，返回数据为BaseData，BaseData可能为空
                    setResultHint("Success=${result.data?.data}")
                }
                is ApiResponse.Failure -> {
                    // 失败，包含Error、Exception
                    setResultHint("Failure")
                }
            }
        }
    }

    /**
     * 获取BaseData的数据，返回Result，返回数据为BaseData内的data
     */
    fun getBaseDataData_Result_BaseDataOfData() {
        viewModelScope.launch {
            when (val result = repository.getBaseDataData_Result_BaseDataOfData(1)) {
                is Result.Success -> {
                    // 成功，返回数据为BaseData内的data
                    setResultHint("Success=${result.data}")
                }
                is Result.Failure -> {
                    // 失败
                    setResultHint("Failure=isError=${result.isError}=code=${result.code}=message=${result.message}")
                }
            }
        }
    }

    /**
     * 获取A、B线性执行的结果-简单，未判断
     */
    fun getABLinearSimple() {
        viewModelScope.launch {
            when (val result = repository.getABLinearSimple(1)) {
                is Result.Success -> {
                    // 成功，返回数据为第二个接口的BaseResult
                    setResultHint("Success=${result.data?.result}")
                }
                is Result.Failure -> {
                    // 失败
                    setResultHint("Failure=isError=${result.isError}=code=${result.code}=message=${result.message}")
                }
            }
        }
    }

    /**
     * 获取A、B线性执行的结果
     */
    fun getABLinear() {
        viewModelScope.launch {
            when (val result = repository.getABLinear(1)) {
                is Result.Success -> {
                    // 成功，返回数据为第二个接口的BaseResult内result
                    setResultHint("Success=${result.data}")
                }
                is Result.Failure -> {
                    // 失败
                    setResultHint("Failure=isError=${result.isError}=code=${result.code}=message=${result.message}")
                }
            }
        }
    }

    /**
     * 获取A、B、C并发执行的结果-简单，未判断
     */
    fun getABCAsyncSimple() {
        viewModelScope.launch {
            when (val result = repository.getABCAsyncSimple(1)) {
                is Result.Success -> {
                    // 成功，返回数据为三个接口数据的集合TestNetAllData。
                    setResultHint("Success=${result.data}")
                }
                is Result.Failure -> {
                    // 失败
                    setResultHint("Failure=isError=${result.isError}=code=${result.code}=message=${result.message}")
                }
            }
        }
    }

    /**
     * 获取A、B、C并发执行的结果
     */
    fun getABCAsync() {
        viewModelScope.launch {
            when (val result = repository.getABCAsync(1)) {
                is Result.Success -> {
                    // 成功，返回数据为三个接口数据的集合TestNetAllData。
                    setResultHint("Success=${result.data}")
                }
                is Result.Failure -> {
                    // 失败
                    setResultHint("Failure=isError=${result.isError}=code=${result.code}=message=${result.message}")
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

    private fun setResultHint(result: String) {
        Log.e("TestViewModel", "result==$result")
        hintText.value = result
    }
}