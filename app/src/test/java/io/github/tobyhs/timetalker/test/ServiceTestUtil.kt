package io.github.tobyhs.timetalker.test

import android.app.Service

import org.robolectric.Robolectric

/**
 * Starts a service, runs the given function on it, then destroys it.
 *
 * @param action function that takes a service so callers can make assertions on it
 * @return instance of the service that ran
 */
inline fun <reified SC : Service> runService(action: (SC) -> Unit = { _ -> }): SC {
    val controller = Robolectric.buildService(SC::class.java)
    controller.create()
    val service = controller.get()
    try {
        action(service)
    } finally {
        controller.destroy()
    }
    return service
}
