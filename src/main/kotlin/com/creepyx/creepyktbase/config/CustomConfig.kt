package com.creepyx.creepyktbase.config

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.InputStream

abstract class CustomConfig : YamlConfiguration() {
    abstract fun createOrLoadDefault(file: File, resource: InputStream?): CustomConfig
    abstract fun createOrLoadDefault(file: String, resource: InputStream?): CustomConfig

    abstract fun getOrCreate(file: File): CustomConfig
    abstract fun getOrCreate(directory: File, file: File): CustomConfig
    abstract fun getOrCreate(file: String): CustomConfig
    abstract fun getOrCreate(folder: String, file: String): CustomConfig

    /**
     * Save a Custom Config in a yaml Format
     */
    abstract fun save()
}
