# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Preserve line number information for debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# --- Gson rules for ProGuard ---
# This prevents ProGuard from stripping classes and members needed by Gson for serialization.

# Keep all data model classes in your data package.
-keep class com.codewithmahad.derivativecalculator.data.** { *; }

# Keep the default constructor for all data model classes.
-keepclassmembers class com.codewithmahad.derivativecalculator.data.** { 
    <init>(); 
}

# Keep Gson-specific classes from being stripped, especially for handling lists (TypeToken).
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken
