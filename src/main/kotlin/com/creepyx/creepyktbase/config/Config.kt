package com.creepyx.creepyktbase.config

import com.creepyx.creepyktbase.ktInstance
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

class Config : YamlConfiguration {
    private var file: File? = null


    constructor(folder: File? = null, file: File)  {
        this.file = file

        if (folder != null) {
            require(folder.isDirectory) { "Folder must be a directory!" }
            this.file = File(folder, file.name)
            if (!folder.exists()) { folder.mkdirs() }
        }

        try {
            if (!this.file!!.exists()) {
                this.file!!.parentFile.mkdirs()
                if (folder != null) {
                    val resourcePath = folder.toPath().resolve(file.name).toFile().path

                    if (ktInstance.getResource(resourcePath) != null) {
                        ktInstance.saveResource(resourcePath, false)
                    } else {
                        this.file!!.createNewFile()
                    }
                } else {
                    if (ktInstance.getResource(file.name) != null) {
                        ktInstance.saveResource(file.name, false)
                    } else {
                        file.createNewFile()
                    }
                }
            }

            load(this.file!!)
            save()
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: InvalidConfigurationException) {
            throw RuntimeException(e)
        }
    }

    constructor(file: String) {
        Config(file = File(ktInstance.dataFolder, file))
    }

    constructor(folder: String, file: String) {
        val directory = File(ktInstance.dataFolder, folder)
        val internalFile = File(directory, file)
        Config(directory, internalFile)
    }

    override fun getString(path: String): String? {
        return super.getString(path)
    }

    fun getString(path: String, def: String): String {
        return super.getString(path, def)!!
    }

    override fun set(path: String, value: Any?) {
        if (value is Location) {
            this["$path.World"] = value.world.name
            this["$path.X"] = value.x
            this["$path.Y"] = value.y
            this["$path.Z"] = value.z
            this["$path.Yaw"] = value.yaw
            this["$path.Pitch"] = value.pitch
            return
        }

        super.set(path, value)
    }

    override fun getLocation(path: String): Location {
        val world = this.getString("$path.World")
        val x = this.getInt("$path.X")
        val y = this.getInt("$path.Y")
        val z = this.getInt("$path.Z")
        val yaw = getDouble("$path.Yaw").toFloat()
        val pitch = getDouble("$path.Pitch").toFloat()

        return Location(Bukkit.getWorld(world!!), x.toDouble(), y.toDouble(), z.toDouble(), yaw, pitch)
    }

    fun save() {
        try {
            super.save(file!!)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}
