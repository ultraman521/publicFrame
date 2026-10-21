# PublicFrame

可复用的 **Kotlin Android 公共脚手架**：双模块结构（框架库 + 示例应用），
基于 AGP 9 内建 Kotlin、协程与 ViewBinding，开箱即用。

## 模块

| 模块 | 说明 |
|---|---|
| `framework` | 脚手架库（`com.publicframe.framework`）：初始化入口、网络层、MVVM 基类、页面状态、常用工具 |
| `app` | 示例应用（`com.publicframe.demo`）：演示各能力的标准用法，新项目可基于此改造 |

## 技术栈

- **构建**：Gradle 9.8 / AGP 9.4（内建 Kotlin，无需 kotlin-android 插件）/ version catalog，JDK 17+
- **架构**：MVVM（ViewModel + StateFlow + ViewBinding）
- **网络**：Retrofit（suspend）+ OkHttp + Gson，统一信封 `ApiResponse` 与异常翻译
- **图片**：Coil
- **SDK**：compileSdk 37 / targetSdk 36 / minSdk 23

## 快速开始

1. 安装 JDK 17+（推荐 21），配置 `JAVA_HOME`。
2. 准备 Android SDK（platform 37），在根目录创建本机专属的 `local.properties`：
   ```properties
   sdk.dir=F:/AndroidSDK
   ```
3. 构建运行：
   ```bash
   ./gradlew :app:assembleDebug
   ./gradlew test
   ```

> 国内网络下载 Gradle 发行版慢时，可用镜像手动下载后放入
> `~/.gradle/wrapper/dists/` 对应目录（如 `mirrors.cloud.tencent.com/gradle/`）。

## 接入脚手架

### 1. 初始化（Application）

```kotlin
class DemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PublicFrame.initialize(
            this,
            FrameworkConfiguration(
                apiBaseUrl = "https://api.example.com/",  // 以 / 结尾
                enableLogging = true,                     // 发布版置 false
                commonHeaders = mapOf("Accept-Language" to "zh-CN")
            )
        )
    }
}
```

`enableCrashHandler = true`（默认）会把未捕获异常写入 `filesDir/crash/`。

### 2. 网络请求

```kotlin
interface DemoApi {
    @GET("banner/json")
    suspend fun banners(): ApiResponse<List<Banner>>
}

val api = ApiClient.service<DemoApi>()

// 解包：业务失败(data==null 或 code!=0)抛 ApiException
val banners = ApiClient.unwrap { api.banners() }
```

运行时切换域名（替代 retrofit-url-manager）：

```kotlin
DomainManager.set("https://api.another.com/")
DomainManager.set(null)   // 恢复默认
```

### 3. 页面与状态

```kotlin
class MainViewModel : BaseViewModel() {
    private val api = ApiClient.service<DemoApi>()
    private val _state = MutableStateFlow<UiState<List<Banner>>>(UiState.Loading)
    val state = _state.asStateFlow()

    fun load() = launchWithState(_state) { ApiClient.unwrap { api.banners() } }
    // Loading → Content / Empty / Error(用户可读文案) 全自动流转
}

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val viewModel: MainViewModel by viewModels()

    override fun onBindingCreated(binding: ActivityMainBinding) {
        collectWithLifecycle(viewModel.state) { state ->
            binding.stateView.render(state) { viewModel.load() }  // Error 态重试
        }
    }
}
```

`MultiStateView` 在 XML 中包裹内容布局即得 加载/空/错误(带重试)/内容 四态。

## framework 能力清单

| 能力 | 入口 |
|---|---|
| 初始化与全局配置 | `PublicFrame` / `FrameworkConfiguration` |
| 日志门面（受 enableLogging 门控） | `FrameLog` |
| 网络客户端 + 响应信封 + 异常文案 | `ApiClient` / `ApiResponse` / `unwrap` / `HttpError` |
| 运行时域名切换 | `DomainManager` |
| MVVM 基类（ViewBinding 泛型） | `BaseActivity` / `BaseFragment` / `BaseViewModel` |
| 页面状态与四态控件 | `UiState` / `MultiStateView` |
| 工具 | `ToastUtils` / `SPUtils` / `DensityUtils` |
| 崩溃落盘 | `CrashHandler`（自动安装） |

## 迁移说明

本项目由旧版 Java/MVVMHabit/RxJava2 工程重写而来，旧实现已移除；
历史代码保留在 git 历史（`Snapshot before Kotlin scaffold rewrite` 提交）中。

## 许可证

MIT License。第三方依赖见 `THIRD_PARTY_NOTICES.md`。
