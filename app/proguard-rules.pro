# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\android\AndroidStudioSDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

####for android begin#####
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.app.View
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.net.http.SslError
-keep class android.webkit.**{*;}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class **.R$* {
    *;
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
####for android end######

#抑制google guava的javax.annotation.XX 引用警告。
-dontwarn javax.annotation.**
-dontwarn com.google.common.**

#百度统计 2016/4/22
-keep class com.baidu.bottom.** { *; }
-keep class com.baidu.kirin.** { *; }
-keep class com.baidu.mobstat.** { *; }

#百度地图、定位 2016/4/26
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**

#极光推送 2016/5/5
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-dontwarn com.google.gson.jpush.**

#butterknife 2016/5/31
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#okhttp 2016/5/31
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-dontwarn okio.**

#fresco
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

#shareSDK 2016/5/31
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class com.mob.**{*;}

#for 自己的model gson可能需要
-keep class com.mumu.realmadrid.model.** { *; }

#systembartint
-keep class com.readystatesoftware.systembartint.** { *; }

#eventbus
-keep class de.greenrobot.event.eventbus.** { *; }
-keepclassmembers class ** {
    public void onEvent*(**);
}

#rx部分rxjava,rxandroid,retrofit2
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

-keepattributes *Annotation*
-keepattributes *JavascriptInterface*

#删除Log代码
#    public static *** e(...);
-assumenosideeffects class android.util.Log {
    public static *** w(...);
    public static *** wtf(...);
    public static *** d(...);
    public static *** v(...);
}

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-dontwarn com.android.volley.toolbox.**

#不懂什么意思的部分
-optimizationpasses 5
#-dontusemixedcaseclassnames
#-dontskipnonpubliclibraryclasses
#-dontpreverify
-verbose
-dontnote
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#-dontoptimize
-dontwarn
-keepattributes InnerClasses,Signature,Exceptions
#-dontskipnonpubliclibraryclassmembers
#新增
-allowaccessmodification

-keep class com.rengwuxian.materialedittext.** { *; }
-keep class com.rey.material.** { *; }
-keep class com.gc.materialdesign { *; }
-keep class cn.smssdk.** { *; }