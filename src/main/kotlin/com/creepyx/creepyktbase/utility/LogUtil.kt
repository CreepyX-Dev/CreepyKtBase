package com.creepyx.creepyktbase.utility

import com.creepyx.creepyktbase.extension.c
import com.creepyx.creepyktbase.ktInstance
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit

fun log(logType: LogType, message: String) {

    val consoleSender = Bukkit.getConsoleSender()

    if (logType == LogType.DEBUG) {
        if (!ktInstance.config.getBoolean("debug")) return
    }

    consoleSender.sendMessage(ktInstance.logPrefix.plus(logType.prefix).plus(message).c().color(logType.color))
}

enum class LogType(val prefix: String, val color: NamedTextColor) {
    INFO("&r[INFO] ", NamedTextColor.GREEN),
    WARNING("&6[WARNING] ", NamedTextColor.GOLD),
    ERROR("&c[ERROR] ", NamedTextColor.RED),
    DEBUG("&e[DEBUG] ", NamedTextColor.YELLOW),
}