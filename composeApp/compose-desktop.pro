# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# Reference: https://github.com/Kotlin/kotlinx.coroutines/blob/13f27f729547e5c22d17d5b5de3582d450b037b4/kotlinx-coroutines-core/jvm/resources/META-INF/com.android.tools/proguard/coroutines.pro

-printmapping build/proguard-mapping.txt

# Keep Ktor classes
-keep class io.ktor.** { *; }
-dontnote io.ktor.**
-dontwarn io.ktor.**
-keep class kotlinx.io.** { *; }
-dontwarn kotlinx.io.**

# Keep all DTO classes in the package
-keep class com.rwmobi.kunigami.data.source.network.dto.** { *; }
-dontnote com.rwmobi.kunigami.data.source.network.dto.**
-keep class com.rwmobi.kunigami.domain.model.** { *; }
-dontnote com.rwmobi.kunigami.domain.model.**

# Apollo workarounds
-dontnote okio.**
-keep class okio.** { *; }
-keep class com.rwmobi.kunigami.graphql.type.** { *; }
-keep class com.apollographql.apollo.** { *; }
-dontnote com.apollographql.**
-keep class okhttp3.** { *; }
-keep class org.bouncycastle.** { *; }
-dontnote okhttp3.internal.platform.**
-dontwarn okhttp3.internal.platform.**

# Keep the class and fields of kotlin.time.Instant
-keep class kotlin.time.Instant { *; }
-dontnote kotlin.time.Instant

# Kotlinx Serialization
-keep class kotlinx.serialization.** { *; }
-dontwarn kotlinx.serialization.**
-keepnames class kotlinx.serialization.internal.** { *; }

# Do not warn about missing annotations and metadata
-dontwarn kotlin.Metadata
-dontwarn kotlin.jvm.internal.**
-dontwarn kotlin.reflect.jvm.internal.**

# Keep necessary Kotlin attributes
-keepattributes Signature, *Annotation*

# JNA classes
-keep class com.sun.jna.** { *; }
-keepclassmembers class * extends com.sun.jna.** { public *; }
-keep class * implements com.sun.jna.** { *; }
-dontnote com.sun.**

# Logging classes, if logging is required
-keep class org.slf4j.** { *; }
-keep class org.slf4j.impl.** { *; }
-keep class ch.qos.logback.** { *; }
-dontwarn org.slf4j.**

# OSHI classes
-keep class oshi.** { *; }
-dontnote oshi.**

# Keep the entire MacOSThemeDetector class and its nested classes
-keep class com.jthemedetecor.** { *; }
-keep class com.jthemedetecor.MacOSThemeDetector$* { *; }

# Annotated interfaces (including methods which are also kept in implementing classes)
-keepattributes Annotation
-keepattributes *Annotation*

# ServiceLoader support for kotlinx.coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Keeping the implementations of exception handlers and Main dispatchers
-keep class * implements kotlinx.coroutines.internal.MainDispatcherFactory
-keep class * implements kotlinx.coroutines.CoroutineExceptionHandler

# Most of volatile fields are updated with AFU and should not be mangled
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# Same story for the standard library's SafeContinuation that also uses AtomicReferenceFieldUpdater
-keepclassmembers class kotlin.coroutines.SafeContinuation {
    volatile <fields>;
}

# These classes are only required by kotlinx.coroutines.debug.AgentPremain, which is only loaded when
# kotlinx-coroutines-core is used as a Java agent, so these are not needed in contexts where ProGuard is used.
-dontwarn java.lang.instrument.ClassFileTransformer
-dontwarn sun.misc.SignalHandler
-dontwarn java.lang.instrument.Instrumentation
-dontwarn sun.misc.Signal

# Only used in `kotlinx.coroutines.internal.ExceptionsConstructor`.
# The case when it is not available is hidden in a `try`-`catch`, as well as a check for Android.
-dontwarn java.lang.ClassValue

# An annotation used for build tooling, won't be directly accessed.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Additional dontwarn rules for common issues
-dontwarn java.awt.**
-dontwarn javax.annotation.**

# RoomDB
# Room persistence library
-keep class androidx.room.** { *; }
-dontnote androidx.room.**
-keep class androidx.sqlite.db.** { *; }
-keep class androidx.sqlite.** { *; }
-dontnote androidx.sqlite.db.**

# Keep generated Room classes
-keepclassmembers class * {
    @androidx.room.* <fields>;
    @androidx.room.* <methods>;
}

# Keep the implementation of the Room database
-keep class * extends androidx.room.RoomDatabase { *; }

# Keep the DAO classes
-keep interface * extends androidx.room.RoomDatabase { *; }
-keep class com.rwmobi.kunigami.data.source.local.database.dao.** { *; }
-dontnote com.rwmobi.kunigami.data.source.local.database.dao.**
-keep class com.rwmobi.kunigami.data.source.local.database.model.** { *; }
