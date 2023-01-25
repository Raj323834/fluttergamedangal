package com.example.dangal_games_demo

import io.flutter.app.FlutterApplication
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.PluginRegistry.PluginRegistrantCallback
import com.clevertap.android.sdk.ActivityLifecycleCallback


class Application : FlutterApplication(), PluginRegistrantCallback {
    override fun onCreate() {
        ActivityLifecycleCallback.register(this)
        super.onCreate()
    }

    override fun registerWith(registry: PluginRegistry?) {
        FirebaseCloudMessagingPluginRegistrant.registerWith(registry)
        if (registry != null) {
        }
    }
}