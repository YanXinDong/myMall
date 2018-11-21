# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\JAVA\SDK/tools/proguard/proguard-android.txt
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
    #指定代码的压缩级别
    -optimizationpasses 5
    #包明不混合大小写
    -dontusemixedcaseclassnames
    #不去忽略非公共的库类
    -dontskipnonpubliclibraryclasses
     #预校验
    -dontpreverify
     #混淆时是否记录日志
    -verbose
     # 混淆时所采用的算法
    -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
    #保护注解
    -keepattributes *Annotation*
    # 保持哪些类不被混淆
    -keep public class * extends android.app.Activity
    -keep public class * extends android.app.Application
    -keep public class * extends android.app.Service
    -keep public class * extends android.content.BroadcastReceiver
    -keep public class * extends android.content.ContentProvider
    -keep public class * extends android.app.backup.BackupAgentHelper
    -keep public class * extends android.preference.Preference
    -keep public class * extends android.os.IInterface
    -keep public class * extends android.os.ResultReceiver
    -keep public class * extends android.appwidget.AppWidgetProvider
    -keep public class com.BFMe.BFMBuyer.javaBean.** {*;}
    -keep public class com.BFMe.BFMBuyer.ugc.bean.** {*;}
    -keep public class com.BFMe.BFMBuyer.main.bean.** {*;}

    -keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
    #忽略警告
    -ignorewarning
    #保持 native 方法不被混淆
    -keepclasseswithmembernames class * {
        native <methods>;
    }
    #保持自定义控件类不被混淆
    -keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet);
    }
    #保持 Parcelable 不被混淆
    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }
    #保持 Serializable 不被混淆
    -keepnames class * implements java.io.Serializable

    #gson
    -keepattributes Signature
    -keep class sun.misc.Unsafe { *; }
    -keep class com.google.gson.examples.android.model.** { *; }

    #IM
    -dontwarn com.netease.**
    -keep class com.netease.** {*;}
    #如果你使用全文检索插件，需要加入
    -dontwarn org.apache.lucene.**
    -keep class org.apache.lucene.** {*;}

     ##    sharesdk
    -keep class cn.sharesdk.**{*;}
    -keep class com.sina.**{*;}
    -keep class **.R$* {*;}
    -keep class **.R{*;}
    -keep class com.mob.**{*;}
    -dontwarn com.mob.**
    -dontwarn cn.sharesdk.**
    -dontwarn **.R$*

     #Glide
     -keep public class * implements com.bumptech.glide.module.GlideModule
     -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
           **[] $VALUES;
           public *;
           }

    #极光推送
    -dontoptimize
    -dontpreverify

    -dontwarn cn.jpush.**
    -keep class cn.jpush.** { *; }

    -dontwarn cn.jiguang.**
    -keep class cn.jiguang.** { *; }
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}