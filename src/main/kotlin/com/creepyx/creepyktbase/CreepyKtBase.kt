package com.creepyx.creepyktbase

import com.creepyx.creepyktbase.config.Config
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

val ktInstance: CreepyKtBase get() = PluginInstance

@PublishedApi
internal lateinit var PluginInstance: CreepyKtBase
    private set

abstract class CreepyKtBase : JavaPlugin(), Listener {


    open var prefix: String = this.name

    open var logPrefix: String = this.name


    /**
     * Called when the plugin was loaded
     */
    open fun load() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this))
    }

    /**
     * Called when the plugin was enabled
     */
    open fun start() {
        CommandAPI.onEnable()
    }

    /**
     * Called when the plugin gets disabled
     */
    open fun stop() {}

    final override fun onLoad() {

        if (::PluginInstance.isInitialized) {
            server.logger.warning("The main instance of KPaper has been modified, even though it has already been set by another plugin!")
        }
        PluginInstance = this
        load()
    }

    final override fun onEnable() {
        super.onEnable()
        server.pluginManager.registerEvents(this, this)
        start()
        Config("config.yml").getString("prefix", this.prefix)
    }

    final override fun onDisable() {
        super.onDisable()
        stop()
    }

}