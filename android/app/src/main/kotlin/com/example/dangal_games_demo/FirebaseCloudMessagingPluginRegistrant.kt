package com.example.dangal_games_demo

import io.flutter.plugin.common.PluginRegistry

object FirebaseCloudMessagingPluginRegistrant {
    fun registerWith(registry: PluginRegistry?) {
        if (alreadyRegisteredWith(registry)) {
            return
        }
    }

    private fun alreadyRegisteredWith(registry: PluginRegistry?): Boolean {
        return false
    }
}