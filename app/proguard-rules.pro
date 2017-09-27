# 指定代码的压缩级别
 -optimizationpasses 5
# 包明不混合大小写
 -dontusemixedcaseclassnames
# 不去忽略非公共的库类
 -dontskipnonpubliclibraryclasses
# 不优化输入的类文件
 -dontoptimize
# 预校验
 -dontpreverify
# 混淆时是否记录日志
 -verbose

# LeakCanary
-keep class org.eclipse.mat.** { *; }
-keep class com.squareup.leakcanary.** { *; }
-libraryjars libs/pgyer_sdk_x.x.jar
-dontwarn com.pgyersdk.**
-keep class com.pgyersdk.** { *; }

# UMENG
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**

-keepattributes *Annotation*

-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class org.apache.thrift.** {*;}

-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}
-keep class com.umeng.socialize**{*;}

-keep public class **.R$*{
   public static final int *;
}

# 避免Log打印输出
-assumenosideeffects class android.util.Log {
   public static *** v(...);
   public static *** d(...);
   public static *** i(...);
   public static *** w(...);
 }

 -dontusemixedcaseclassnames
     -dontshrink
     -dontoptimize
     -dontwarn com.google.android.maps.**
     -dontwarn android.webkit.WebView
     -dontwarn com.umeng.**
     -dontwarn com.tencent.weibo.sdk.**
     -dontwarn com.facebook.**
     -keep public class javax.**
     -keep public class android.webkit.**
     -dontwarn android.support.v4.**
     -keep enum com.facebook.**
     -keepattributes Exceptions,InnerClasses,Signature
     -keepattributes *Annotation*
     -keepattributes SourceFile,LineNumberTable

     -keep public interface com.facebook.**
     -keep public interface com.tencent.**
     -keep public interface com.umeng.socialize.**
     -keep public interface com.umeng.socialize.sensor.**
     -keep public interface com.umeng.scrshot.**
     -keep class com.android.dingtalk.share.ddsharemodule.** { *; }
     -keep public class com.umeng.socialize.* {*;}


     -keep class com.facebook.**
     -keep class com.facebook.** { *; }
     -keep class com.umeng.scrshot.**
     -keep public class com.tencent.** {*;}
     -keep class com.umeng.socialize.sensor.**
     -keep class com.umeng.socialize.handler.**
     -keep class com.umeng.socialize.handler.*
     -keep class com.umeng.weixin.handler.**
     -keep class com.umeng.weixin.handler.*
     -keep class com.umeng.qq.handler.**
     -keep class com.umeng.qq.handler.*
     -keep class UMMoreHandler{*;}
     -keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
     -keep class com.tencent.mm.sdk.modelmsg.** implements   com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
     -keep class im.yixin.sdk.api.YXMessage {*;}
     -keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
     -keep class com.tencent.mm.sdk.** {
      *;
     }
     -keep class com.tencent.mm.opensdk.** {
    *;
     }
     -dontwarn twitter4j.**
     -keep class twitter4j.** { *; }

     -keep class com.tencent.** {*;}
     -dontwarn com.tencent.**
     -keep public class com.umeng.com.umeng.soexample.R$*{
     public static final int *;
     }
     -keep public class com.linkedin.android.mobilesdk.R$*{
     public static final int *;
         }
     -keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
     }

     -keep class com.tencent.open.TDialog$*
     -keep class com.tencent.open.TDialog$* {*;}
     -keep class com.tencent.open.PKDialog
     -keep class com.tencent.open.PKDialog {*;}
     -keep class com.tencent.open.PKDialog$*
     -keep class com.tencent.open.PKDialog$* {*;}

     -keep class com.sina.** {*;}
     -dontwarn com.sina.**
     -keep class  com.alipay.share.sdk.** {
        *;
     }
     -keepnames class * implements android.os.Parcelable {
     public static final ** CREATOR;
     }

     -keep class com.linkedin.** { *; }
     -keepattributes Signature

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

# Bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}