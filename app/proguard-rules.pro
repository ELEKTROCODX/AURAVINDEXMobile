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
# Mantén anotaciones (útiles para Room, Gson, Retrofit, etc.)
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod

# --- Gson (Serialización/Deserialización por reflexión) ---
# Evita que se obfusquen las clases y campos usados con Gson
-keep class com.elektrocodx.auravindex.model.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# --- Retrofit & API Interfaces ---
# Mantiene las interfaces de Retrofit intactas
-keep interface retrofit2.Call
-keep interface retrofit2.http.* { *; }
-keep class com.elektrocodx.auravindex.api.** { *; }

# --- Coroutines (evita problemas con continuations) ---
-keepclassmembers class kotlinx.coroutines.internal.MainDispatcherLoader {
    <clinit>();
}
-dontwarn kotlinx.coroutines.**

# --- Room (usado para caché local) ---
# Room usa anotaciones y reflexión para generar código en tiempo de compilación
-keep class androidx.room.** { *; }
-keepclassmembers class * {
    @androidx.room.* <methods>;
    @androidx.room.* <fields>;
}
-dontwarn androidx.room.**

# --- ViewModel y Jetpack (Lifecycle, etc.) ---
-keep class androidx.lifecycle.ViewModel
-keep class com.elektrocodx.auravindex.viewmodel.** { *; }

# --- Navigation (safe args, rutas, etc.) ---
-keepclassmembers class * implements androidx.navigation.NavArgs {
    <init>(...);
}
-dontwarn androidx.navigation.**

# --- Parcelables (en caso uses algunos modelos así) ---
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# --- Jetpack Compose (si lo estás usando) ---
# Si usas Compose, incluye esto
-dontwarn kotlin.Unit
-dontwarn androidx.compose.**
-keep class androidx.compose.** { *; }

# --- Kotlin (general) ---
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**

# --- Otras clases del proyecto ---
# Evita obfuscar las entidades del dominio, útiles para debugging y errores
-keep class com.elektrocodx.auravindex.domain.** { *; }

# --- Desactiva optimizaciones conflictivas (opcional pero recomendable para apps con reflexión) ---
-dontoptimize
-dontpreverify