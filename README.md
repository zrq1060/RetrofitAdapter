# RetrofitAdapter

> 可以根据`返回值类型`，区分`响应规则`的，`kotlin携程`网络库

## 适用于：

* 多部门，多规则，每个部门有每个部门的网络请求规则。返回值内容、规则都不同。
* 多模块，多规则，每个模块可使用一个或多个规则。
* 公司目前只有一个规则，但不保证后续没新增。

## 使用

### 1.引入

project **build.gradle**
```gradle
allprojects {
    repositories {
        mavenCentral()
    }
}
```

app **build.gradle**

```gradle
implementation 'io.github.zrq1060:retrofit-adapter:0.0.1'
```

### 2.增加CoroutinesResponseCallAdapterFactory

```kotlin
Retrofit.Builder()
	.addCallAdapterFactory(CoroutinesResponseCallAdapterFactory.create())
	.build()
```

### 3.使用ApiResponse作为返回值，声明接口

```kotlin
/**
 * 获取用户信息
 */
@GET("getUser")
suspend fun getUser(): ApiResponse<XXX<User>>
```

> `XXX`为公司网络请求规则通用包装类：如规定，会返回`code`、`message`、`data`。且当`code`为`200`时代表**成功**，其它代表**失败**并提示`message`。则`XXX`可以为：

```kotlin
data class BaseData<T>(val code: Int?, val message: String?, val data: T?)
```

则网络请求接口定义为：
```kotlin
/**
 * 获取用户信息
 */
@GET("getUser")
suspend fun getUser(): ApiResponse<BaseData<User>>
```

### 4.声明规则

声明规则，使其对`返回值类型`为`XXX`的网络响应结果进行处理，如以上面的例子为例，`BaseData`作为`返回值类型`，进行处理，代码如下（**！！！看代码注释**）：

> 例子规则：
> 1. `网络成功`并且`code为200`并且`data不为空`，则代表成功（`Success`）。
> 2.  `网络成功`并且`code不为200`，则代表失败（`Error`）。
> 3. `网络失败`、`ResponseCode失败`、`ResponseBody为空`等，则代表异常（`Exception`）。

创建自定义类，实现`ApiResponseResultHandler`接口。
```kotlin
class BaseDataApiResponseResultHandler : ApiResponseResultHandler {
    /**
     * 优先级，值越大优先级越高越优先处理，值相同按先添加的先判断。
     */
    override fun priority(): Int {
        return 0
    }

    /**
     * 此处理器，是否处理此返回值类型，如果返回为true，则会调用
     * [handleOnResponse]、[handleOnFailure]进行后续处理。
     */
    override fun shouldHandle(resultClass: Class<*>): Boolean {
        return resultClass == BaseData::class.java
    }

    /**
     * 处理网络请求-响应结果
     */
    override fun <T> handleOnResponse(response: Response<T>): ApiResponse<T> {
        return if (response.isSuccessful) {
            // 网络成功
            val body = response.body()
            if (body != null) {
                // body不为空
                val baseData = body as BaseData<*>
                val baseDataCode = baseData.code
                if (baseDataCode == null) {
                    // 公司code规则异常
                    ApiResponse.exception(RulesException("BaseData code is null"))
                } else if (baseDataCode == 200) {
                    // 公司code规则成功
                    val data = baseData.data
                    if (data != null) {
                        // 公司data规则成功
                        ApiResponse.success(body)
                    } else {
                        // 公司data规则异常
                        ApiResponse.exception(RulesException("BaseData data is null"))
                    }
                } else {
                    // 公司code规则失败
                    ApiResponse.error(baseDataCode, baseData.message ?: "")
                }
            } else {
                // body为空
                ApiResponse.exception(ResponseBodyEmptyException())
            }
        } else {
            // 网络失败
            ApiResponse.exception(ResponseCodeErrorException(response.code()))
        }
    }

    /**
     * 处理网络请求-失败结果
     */
    override fun <T> handleOnFailure(t: Throwable): ApiResponse<T> {
        return ApiResponse.exception(t)
    }
}
```

> 说明：
> 1. `ApiResponse`为`API`响应类。含有子类`Success`、`Error`、`Exception`三种结果。 分别代表着**成功**、**失败**、**异常**。


### 5.添加规则

在**网络请求前**添加规则，如在`Application`添加。

```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // 增加自定义规则
        ApiResponseHandlerManager
	        .add(BaseDataApiResponseResultHandler())
	        // .add(OtherApiResponseResultHandler())
    }
}
```

> 说明：
> 1. `ApiResponseHandlerManager`可添加**多个**`ApiResponseResultHandler`。如果**优先级相同**，则**先添加**的，**先判断**。
> 2. 如果**没有符合返回值类型**的规则，则默认**网络成功**代表**成功**，**网络失败**代表**失败**。


### 6.接收结果

#### 声明式处理


```
viewModelScope.launch {
    api.getUser()
        .onSuccess {
            // 成功，此方法内可直接拿到-成功数据data
        }.onError {
            // 失败-错误，此方法内可直接拿到-code、message
        }.onException {
            // 失败-异常，此方法内可直接拿到-throwable
        }
}
```

#### 命令式处理

```
viewModelScope.launch {
    when (val result = api.getUser()) {
        is ApiResponse.Success -> {
            // 成功，可通过result拿到-成功数据data
        }
        is ApiResponse.Failure.Error -> {
            // 失败-错误，可通过result拿到-code、message
        }
        is ApiResponse.Failure.Exception -> {
            // 失败-异常，可通过result拿到-throwable
        }
    }
}
```

### 7.指定规则
上面是根据**返回值类型**，去**获取**对应**添加的处理逻辑**，具有**通用性**。此为在**指定**的**网络请求接口定义**上标注`ApiResponseHandler`注解，以**指定规则**，具有**特定性**。

#### 声明规则
如以上面的例子为例，之前是`code为200`代表**成功**，现在改成为`code为0`代表**成功**，进行处理，代码如下：

创建自定义类，实现`ApiResponseAnnotationHandler`接口。

```
class BaseDataApiResponseAnnotationHandler : ApiResponseAnnotationHandler {

    /**
     * 处理网络请求-响应结果
     */
    override fun <T> handleOnResponse(response: Response<T>): ApiResponse<T> {
        return if (response.isSuccessful) {
            // 网络成功
            val body = response.body()
            if (body != null) {
                // body不为空
                val baseData = body as BaseData<*>
                val baseDataCode = baseData.code
                if (baseDataCode == null) {
                    // 公司code规则异常
                    ApiResponse.exception(RulesException("BaseData code is null"))
                } else if (baseDataCode == 0) {
                    // 公司code规则成功
                    val data = baseData.data
                    if (data != null) {
                        // 公司data规则成功
                        ApiResponse.success(body)
                    } else {
                        // 公司data规则异常
                        ApiResponse.exception(RulesException("BaseData data is null"))
                    }
                } else {
                    // 公司code规则失败
                    ApiResponse.error(baseDataCode, baseData.message ?: "")
                }
            } else {
                // body为空
                ApiResponse.exception(ResponseBodyEmptyException())
            }
        } else {
            // 网络失败
            ApiResponse.exception(ResponseCodeErrorException(response.code()))
        }
    }

    /**
     * 处理网络请求-失败结果
     */
    override fun <T> handleOnFailure(t: Throwable): ApiResponse<T> {
        return ApiResponse.exception(t)
    }
}
```
> 说明：
> 1. `ApiResponseAnnotationHandler`接口比`ApiResponseResultHandler`接口少`priority()`、`shouldHandle(resultClass)`这两个方法，因为**能知道**它作用在哪个**网络请求接口**，所以不需要再进行匹配（`shouldHandle(resultClass)`方法完成）和解决多个匹配问题（`priority()`方法完成）。


#### 指定规则

在**指定**的**网络请求接口定义**上标注`ApiResponseHandler`注解，并指定创建的规则（如：`BaseDataApiResponseAnnotationHandler`）。

```kotlin
/**
 * 获取用户信息
 */
@GET("getUser")
@ApiResponseHandler(BaseDataApiResponseAnnotationHandler::class)
suspend fun getUser(): ApiResponse<BaseData<User>>
```

> 说明：
> 1. **指定规则**，**不需要**添加到`ApiResponseHandlerManager`中。
> 2. 如果有**指定规则**，则以**指定规则**为主，**添加规则**的则无效。


## 联系方式
QQ：273902141

QQ群：644493632