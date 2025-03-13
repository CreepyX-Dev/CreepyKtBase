package com.creepyx.creepyktbase.utility

import com.creepyx.creepyktbase.config.Config
import com.creepyx.creepyktbase.extension.c
import com.creepyx.creepyktbase.extension.toComponents
import com.destroystokyo.paper.profile.ProfileProperty
import com.google.common.base.Preconditions
import lombok.Setter
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import java.util.*

class ItemBuilder {
    private val item: ItemStack
    private val meta: ItemMeta

    constructor(material: Material) {
        this.item = ItemStack(material)
        this.meta = item.itemMeta
    }

    constructor(itemKey: String) {
        config = Config("items.yml")
        if (config == null) {
            throw NullPointerException("[ItemBuilder] Config cannot be null")
        }
        val name: String = config!!.getString("$itemKey.name", itemKey)
        val lore: List<String> = config!!.getStringList("$itemKey.lore")
        val materialString: String = config!!.getString("$itemKey.material") ?: "DIRT"
        val material = Material.matchMaterial(materialString)

        Preconditions.checkNotNull(material, "Invalid material $materialString")

        this.item = ItemStack(material!!)
        this.meta = item.itemMeta
        this.name(name)
        this.lore(lore)
    }

    constructor(itemKey: String, placeholders: Map<String, String>) {
        val name: String = config!!.getString("$itemKey.name", itemKey)
        val lore: List<String> = config!!.getStringList("$itemKey.lore")
        val materialString: String = config!!.getString("$itemKey.material", itemKey)
        val material = Material.matchMaterial(materialString)

        requireNotNull(material) { "Invalid material $materialString" }

        this.item = ItemStack(material)
        this.meta = item.itemMeta
        this.name(name.c(placeholders))
        this.compLore(lore.toComponents(placeholders))
    }

    constructor(material: Material, amount: Int) {
        this.item = ItemStack(material, amount)
        this.meta = item.itemMeta
    }

    constructor(item: ItemStack) {
        this.item = item
        this.meta = item.itemMeta
    }

    fun name(name: String): ItemBuilder {
        meta.displayName(name.c())
        return this
    }

    fun name(name: Component?): ItemBuilder {
        meta.displayName(name)
        return this
    }

    fun lore(vararg lore: String): ItemBuilder {
        return getItemBuilder(lore.toList().toTypedArray())
    }

    private fun getItemBuilder(lore: Array<String>): ItemBuilder {
        val loreList: MutableList<Component> = ArrayList()
        for (string in lore) {
            loreList.add(string.c())
        }
        meta.lore(loreList)
        return this
    }

    fun lore(lore: List<String>): ItemBuilder {
        return getItemBuilder(lore.toTypedArray<String>())
    }

    fun lore(vararg lore: Component): ItemBuilder {
        meta.lore(Arrays.stream(lore).toList())
        return this
    }

    fun compLore(loreComponents: List<Component?>?): ItemBuilder {
        meta.lore(loreComponents)
        return this
    }

    fun modelData(number: Int): ItemBuilder {
        meta.setCustomModelData(number)
        return this
    }

    fun addPersistent(key: NamespacedKey, type: PersistentDataType<Any, Any>, value: Any): ItemBuilder {
        meta.persistentDataContainer.set(key, type, value)
        return this
    }

    fun addPersistentString(key: NamespacedKey, value: String): ItemBuilder {
        meta.persistentDataContainer.set(key, PersistentDataType.STRING, value)
        return this
    }

    fun unbreakable(): ItemBuilder {
        meta.isUnbreakable = !meta.isUnbreakable
        return this
    }

    fun addFlag(vararg flags: ItemFlag): ItemBuilder {
        meta.addItemFlags(*flags)
        return this
    }

    fun removeFlag(vararg flags: ItemFlag): ItemBuilder {
        meta.removeItemFlags(*flags)
        return this
    }

    fun clearLore(): ItemBuilder {
        meta.lore(ArrayList())
        return this
    }

    fun hideTags(): ItemBuilder {
        for (value in ItemFlag.entries) {
            meta.addItemFlags(value)
        }
        meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE)
        return this
    }

    fun hideAll(): ItemBuilder {
        for (value in ItemFlag.entries) {
            meta.addItemFlags(value)
        }
        return this
    }

    fun showTags(): ItemBuilder {
        for (value in ItemFlag.entries) {
            meta.removeItemFlags(value)
        }
        return this
    }

    fun glow(glow: Boolean): ItemBuilder {
        meta.addEnchant(Enchantment.RIPTIDE, 1, true)
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        return this
    }

    fun enchant(enchantment: Enchantment, level: Int): ItemBuilder {
        meta.addEnchant(enchantment, level, true)
        return this
    }

    fun removeEnchant(enchantment: Enchantment, level: Int): ItemBuilder {
        meta.removeEnchant(enchantment)
        return this
    }

    fun removeEnchants(): ItemBuilder {
        for (enchantment in meta.enchants.keys) {
            meta.removeEnchant(enchantment)
        }
        return this
    }

    fun attribute(attribute: Attribute, modifier: AttributeModifier): ItemBuilder {
        meta.addAttributeModifier(attribute, modifier)
        return this
    }

    fun removeAttribute(attribute: Attribute, modifier: AttributeModifier): ItemBuilder {
        meta.removeAttributeModifier(attribute, modifier)
        return this
    }

    fun removeAttribute(attribute: Attribute): ItemBuilder {
        meta.removeAttributeModifier(attribute)
        return this
    }

    fun headOwner(uuid: String): ItemBuilder {
        if (meta is SkullMeta) meta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(uuid)))
        return this
    }

    fun headOwnerName(name: String): ItemBuilder {
        if (meta is SkullMeta) meta.setOwningPlayer(Bukkit.getOfflinePlayer(name))
        return this
    }

    fun headValue(base64: String): ItemBuilder {
        if (meta is SkullMeta) {
            val profile = Bukkit.createProfile(UUID.randomUUID())
            profile.setProperty(ProfileProperty("textures", base64))
            meta.playerProfile = profile
        }
        return this
    }

    fun headURL(url: String): ItemBuilder {
        val base64 = convertURLToBase64(url)
        return headValue(base64)
    }

    private fun convertURLToBase64(url: String): String {
        val json = "{\"textures\":{\"SKIN\":{\"url\":\"$url\"}}}"
        return Base64.getEncoder().encodeToString(json.toByteArray())
    }

    fun build(): ItemStack {
        item.setItemMeta(this.meta)
        return this.item
    }

    companion object {
        @Setter
        private var config: Config? = null
    }
}