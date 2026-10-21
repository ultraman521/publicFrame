# PublicFrame

PublicFrame is a reusable Android foundation with a Kotlin application layer and a Java compatibility module for the original MVVMHabit implementation.

## Modules

- `framework`: Kotlin public framework module with initialization configuration and a shared `UiState` model.
- `mvvmhabit`: legacy MVVM, networking, cache, and utility implementation kept as a compatibility layer.
- `app`: Kotlin sample application and business-facing code.

## Getting started

Open the project in Android Studio, configure the Android SDK, and provide a machine-local `local.properties`. Initialize the framework with `PublicFrame.initialize(application, configuration)` and inject an HTTP(S) API base URL ending with `/` when networking is needed.

The application entry points, sample ViewModel, and URL configuration have been migrated to Kotlin. The legacy Java implementation remains temporarily so existing callers can migrate incrementally.

## License

The project source is released under the MIT License. See `THIRD_PARTY_NOTICES.md` for third-party code and dependency notices.
