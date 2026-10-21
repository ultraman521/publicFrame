# PublicFrame

A reusable **Kotlin Android scaffold**: a two-module setup (framework library +
demo app) built on AGP 9 built-in Kotlin, coroutines, and ViewBinding.

## Modules

| Module | Description |
|---|---|
| `framework` | The scaffold library (`com.publicframe.framework`): initialization, networking, MVVM base classes, page states, and common utilities |
| `app` | Demo application (`com.publicframe.demo`) showing canonical usage of every capability |

## Stack

- **Build**: Gradle 9.8 / AGP 9.4 (built-in Kotlin, no kotlin-android plugin) / version catalog, JDK 17+
- **Architecture**: MVVM (ViewModel + StateFlow + ViewBinding)
- **Networking**: Retrofit (suspend) + OkHttp + Gson with a shared `ApiResponse` envelope and error translation
- **Images**: Coil
- **SDK**: compileSdk 37 / targetSdk 36 / minSdk 23

## Getting started

1. Install JDK 17+ (21 recommended) and set `JAVA_HOME`.
2. Provide an Android SDK (platform 37) and create a machine-local `local.properties`:
   ```properties
   sdk.dir=/path/to/AndroidSDK
   ```
3. Build:
   ```bash
   ./gradlew :app:assembleDebug
   ./gradlew test
   ```

## Using the framework

### Initialization

```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        PublicFrame.initialize(
            this,
            FrameworkConfiguration(
                apiBaseUrl = "https://api.example.com/",  // must end with '/'
                enableLogging = true
            )
        )
    }
}
```

`enableCrashHandler = true` (default) persists uncaught exceptions to `filesDir/crash/`.

### Networking

```kotlin
interface DemoApi {
    @GET("banner/json")
    suspend fun banners(): ApiResponse<List<Banner>>
}

val api = ApiClient.service<DemoApi>()
val banners = ApiClient.unwrap { api.banners() }   // throws ApiException on business failure
```

Runtime domain switching (replaces the old retrofit-url-manager):

```kotlin
DomainManager.set("https://api.another.com/")
DomainManager.set(null)   // restore
```

### Screens and state

```kotlin
class MainViewModel : BaseViewModel() {
    private val _state = MutableStateFlow<UiState<List<Banner>>>(UiState.Loading)
    val state = _state.asStateFlow()

    fun load() = launchWithState(_state) { ApiClient.unwrap { api.banners() } }
}

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun onBindingCreated(binding: ActivityMainBinding) {
        collectWithLifecycle(viewModel.state) { state ->
            binding.stateView.render(state) { viewModel.load() }
        }
    }
}
```

`MultiStateView` wraps your content layout in XML and provides
loading / empty / error (with retry) / content states.

## Migration note

This project was rewritten from a legacy Java/MVVMHabit/RxJava2 codebase; the
old implementation was removed and remains available in git history under the
`Snapshot before Kotlin scaffold rewrite` commit.

## License

MIT License. See `THIRD_PARTY_NOTICES.md` for third-party notices.
