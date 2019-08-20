# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android sdk\sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keepclasscom.pnsol.sdk.newland.listener.**{*;}
-keepclasscom.pnsol.sdk.auth.**{*;}
-keepclasscom.pnsol.sdk.payment.PaymentInitialization{*;}
-keepclasscom.pnsol.sdk.payment.PaymentProcessThread{*;}
-keepclasscom.pnsol.sdk.payment.PaymentModeThread{*;}
-keepclasscom.pnsol.sdk.payment.**{*;}
-keepclasscom.pnsol.sdk.qpos.listener.QPOSListener{*;}
-keepclasscom.pnsol.sdk.payment.MiuraUpdates{*;}
-keepclasscom.pnsol.sdk.interfaces.**{*;}
-keepclasscom.pnsol.sdk.enums.**{*;}
-keepclasscom.pnsol.sdk.miura.emv.tlv.ISOUtil{*;}
-keepclasscom.pnsol.sdk.miura.response.ResponseManager{*;}
-keepclasscom.pnsol.sdk.miura.messages.Message{*;}
-keepclasscom.pnsol.sdk.usb.USBClass{*;}
-keepclasscom.dspread.xpos.BLE.**{*;}
-keepclasscom.pnsol.sdk.util.**{*;}
-keepclasscom.newland.mpos.payswiff.**{*;}
-keepclassorg.codehaus.jackson.**{*;}
-keepclasscom.pnsol.sdk.n910.**{*;}
-keepclasscom.pnsol.sdk.n3.**{*;}
-keepclassandroid.a.a.**{*;}
-keepclassandroid.misc.**{*;}
-keepclassandroid.newland.**{*;}
-keepclassandroid.psam.**{*;}
-keepclasscom.a.a.**{*;}
-keepclasscom.newland.**{*;}
-keepclasscom.nlscan.android.scan.**{*;}

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

 -keep class javax.xml.crypto.dsig.** { *; }
 -dontwarn javax.xml.crypto.dsig.**
 -keep class javax.xml.crypto.** { *; }
 -dontwarn javax.xml.crypto.**

 -keep class org.spongycastle.** { *; }
 -dontwarn org.spongycastle.**


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