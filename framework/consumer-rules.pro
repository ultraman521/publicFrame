# Consumer rules applied to apps that depend on :framework.

# Retrofit / OkHttp (official recommended rules, trimmed).
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations, AnnotationDefault
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# Gson serializes model classes via reflection; models are usually kept
# with their own rules, but never strip the framework types referenced
# by ApiResponse deserialization.
-keep class com.publicframe.framework.net.ApiResponse { *; }
