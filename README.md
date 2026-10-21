# PublicFrame

PublicFrame 是一个可复用的 Android 基础框架示例，使用 Kotlin 作为应用层语言，并保留旧版 Java MVVMHabit 兼容模块。

## 模块

- `framework`：Kotlin 公共框架模块，提供初始化配置和统一 `UiState`。
- `mvvmhabit`：原有 MVVM、网络、缓存和工具实现，作为兼容层逐步迁移。
- `app`：Kotlin 示例应用，只承载示例入口和业务代码。

## 快速开始

1. 使用 Android Studio 打开项目根目录。
2. 配置 Android SDK，并创建本机专用的 `local.properties`。
3. 在应用启动时调用 `PublicFrame.initialize(application, configuration)`。
4. 通过 `FrameworkConfiguration.apiBaseUrl` 注入以 `/` 结尾的 HTTP(S) API 地址。

## Kotlin 迁移策略

应用入口、Application、示例 ViewModel 和 URL 配置已经迁移到 Kotlin。`mvvmhabit` 中的旧 Java 实现暂时保留，以保证已有调用方可以逐步迁移；后续可按包逐批转换。

## 许可证

项目源码采用 MIT License。第三方代码和依赖请参阅 `THIRD_PARTY_NOTICES.md`。
