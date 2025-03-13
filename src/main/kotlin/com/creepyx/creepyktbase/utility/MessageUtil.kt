package com.creepyx.creepyktbase.utility

import com.creepyx.creepyktbase.config.Config
import com.creepyx.creepyktbase.extension.c
import lombok.Setter
import net.kyori.adventure.text.Component

@Setter
private val config: Config? = null


fun getMessage(key: String, placeholders: Map<String, String> = mapOf(), withPrefix: Boolean = false, fallback: String?): Component? {
    if (config == null) {
        log(LogType.ERROR, "[MessageConfig] Config is null or empty!")
        return null
    }
    if (fallback != null) {
        return config.getString(key, fallback).c(placeholders, withPrefix)
    }
    return config.getString(key, key).c(placeholders, withPrefix)
}