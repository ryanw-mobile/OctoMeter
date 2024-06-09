# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#

-dontwarn javax.annotation.concurrent.GuardedBy
-dontwarn javax.annotation.Nullable
-dontwarn kotlinx.datetime.**
-dontwarn org.slf4j.**
-keep class org.slf4j.**{ *; }
-keep class com.sun.jna.* { *; }
-keep class * implements com.sun.jna.* { *; }
-dontwarn io.github.**
-dontnote com.sun.**
