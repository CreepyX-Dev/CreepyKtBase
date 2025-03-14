package com.creepyx.creepyktbase

import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener


/**
 * Shortcut for unregistering all events in this listener.
 */
fun Listener.unregister() = HandlerList.unregisterAll(this)

/**
 * Registers the event with a custom event [executor].
 *
 * @param T the type of event
 * @param priority the priority when multiple listeners handle this event
 * @param ignoreCancelled if manual cancellation should be ignored
 * @param executor handles incoming events
 */
inline fun <reified T : Event> Listener.register(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    noinline executor: (Listener, Event) -> Unit,
) {
    Bukkit.getPluginManager().registerEvent(T::class.java, this, priority, executor, PluginInstance, ignoreCancelled)
}

/**
 * This class represents a [Listener] with
 * only one event to listen to.
 */
abstract class SingleListener<T : Event>(
    val priority: EventPriority,
    val ignoreCancelled: Boolean,
) : Listener {
    abstract fun onEvent(event: T)
}

/**
 * Registers the [SingleListener] with its
 * event listener.
 *
 * @param priority the priority when multiple listeners handle this event
 * @param ignoreCancelled if manual cancellation should be ignored
 */
inline fun <reified T : Event> SingleListener<T>.register() {
    Bukkit.getPluginManager().registerEvent(
        T::class.java,
        this,
        priority,
        { _, event -> (event as? T)?.let { this.onEvent(it) } },
        PluginInstance,
        ignoreCancelled
    )
}

/**
 * @param T the type of event to listen to
 * @param priority the priority when multiple listeners handle this event
 * @param ignoreCancelled if manual cancellation should be ignored
 * @param register if the event should be registered immediately
 * @param onEvent the event callback
 */
inline fun <reified T : Event> listen(
//    priority: EventPriority = KPaperConfiguration.Events.eventPriority,
//    ignoreCancelled: Boolean = KPaperConfiguration.Events.ignoreCancelled,
//    register: Boolean = KPaperConfiguration.Events.autoRegistration,
    register: Boolean = true,
    crossinline onEvent: (event: T) -> Unit,
): SingleListener<T> {
    val listener = object : SingleListener<T>(EventPriority.NORMAL, false) {
        override fun onEvent(event: T) = onEvent.invoke(event)
    }
    if (register) listener.register()
    return listener
}