package com.creepyx.creepyktbase.extension

import org.bukkit.inventory.Inventory

fun Inventory.closeForViewers() = HashSet(viewers).forEach { it.closeInventory() }