# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\sdk/tools/proguard/proguard-android.txt
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


-keepattributes Signature

-dontwarn cn.qatime.player.view.**
-dontwarn com.bumptech.glide.**
-dontwarn libraryextra.**
-keep class cn.qatime.player.view.**{*;}
-keep class libraryextra.transformation.GlideCircleTransform {*;}
-keep class libraryextra.transformation.GlideRoundTransform {*;}
#common
-keep class libraryextra.cropview.**{*;}
-keep class libraryextra.view.**{*;}
-keep class libraryextra.utils.**{*;}
-dontwarn libraryextra.transformation.**
-keep class libraryextra.transformation.**{*;}
-keep class libraryextra.interpolator.**{*;}
-keep class libraryextra.adapter.**{*;}

# #  ############### volley混淆  ###############
-keep class com.android.volley.** {*;}

-keep class com.android.volley.toolbox.** {*;}

-keep class com.android.volley.Response$* { *; }

-keep class com.android.volley.Request$* { *; }

-keep class com.android.volley.RequestQueue$* { *; }

-keep class com.android.volley.toolbox.HurlStack$* { *; }

-keep class com.android.volley.toolbox.ImageLoader$* { *; }

#支付宝
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}

#彈幕混淆
-keep class cn.qatime.player.barrage.**{*;}

#eventbus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }


#网易云信
-dontwarn com.netease.**
-dontwarn io.netty.**
-keep class com.netease.** {*;}
#如果 netty 使用的官方版本，它中间用到了反射，因此需要 keep。如果使用的是我们提供的版本，则不需要 keep
-keep class io.netty.** {*;}

#如果你使用全文检索插件，需要加入
#-dontwarn java.nio.channels.SeekableByteChannel
#-dontwarn org.apache.lucene.**
#-keep class org.apache.lucene.** {*;}
#-keep class org.lukhnos.** {*;}
#-keep class org.tartarus.** {*;}


#wechat
-keep class com.tencent.**{*;}

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}


-dontwarn android.support.**
-keep class android.support.**{*;}
-dontwarn com.handmark.**
-keep class com.handmark.**{*;}

-dontwarn com.google.gson.**
-keep class com.google.gson.**{*;}
-keep class org.json.** {*;}

-dontobfuscate
-dontoptimize

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

-keep class com.google.gson.examples.android.model.** { *; }
-keep class libraryextra.bean.**{*;}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep public class * implements java.io.Serializable {*;}

-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService


-dontwarn org.apache.**
-keep class org.apache.**{*;}
