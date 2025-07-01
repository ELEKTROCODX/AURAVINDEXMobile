# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


####################################
# AURA VINDEX - ProGuard rules
####################################

# --- General settings ---
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod

# --- Gson (Serialización/Deserialización por reflexión) ---
-keep class com.elektrocodx.auravindex.model.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# --- Retrofit & API Interfaces ---
-keep interface retrofit2.Call
-keep interface retrofit2.http.* { *; }
-keep class com.elektrocodx.auravindex.api.** { *; }

# --- Coroutines ---
-keepclassmembers class kotlinx.coroutines.internal.MainDispatcherLoader {
    <clinit>();
}
-dontwarn kotlinx.coroutines.**

# --- Room ---
-keep class androidx.room.** { *; }
-keepclassmembers class * {
    @androidx.room.* <methods>;
    @androidx.room.* <fields>;
}
-dontwarn androidx.room.**

# --- ViewModel and Jetpack (Lifecycle, etc.) ---
-keep class androidx.lifecycle.ViewModel
-keep class com.elektrocodx.auravindex.viewmodel.** { *; }

# --- Navigation (safe args, routes, etc.) ---
-keepclassmembers class * implements androidx.navigation.NavArgs {
    <init>(...);
}
-dontwarn androidx.navigation.**

# --- Jetpack Compose ---
-dontwarn kotlin.Unit
-dontwarn androidx.compose.**
-keep class androidx.compose.** { *; }

# --- Kotlin (general) ---
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**

# --- Others ---
-keep class com.elektrocodx.auravindex.domain.** { *; }

# --- Deactivate aggressive optimizations (optional but recommended for apps with reflection) ---
-dontoptimize
-dontpreverify