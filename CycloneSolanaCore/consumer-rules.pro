-keep class com.cyclone.solana.core.** { *; }

##---------------Begin: proguard configuration for sqlcipher  ----------

-keep class net.sqlcipher.** {
    *;
}

-dontwarn net.sqlcipher.**

##---------------End: proguard configuration for sqlcipher  ----------