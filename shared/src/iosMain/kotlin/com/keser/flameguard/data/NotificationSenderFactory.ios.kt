package com.keser.flameguard.data

import platform.Foundation.NSUUID
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNUserNotificationCenter

actual fun createNotificationSender(): NotificationSender = IosNotificationSender()

class IosNotificationSender : NotificationSender {
    override fun send(title: String, body: String) {
        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            setBody(body)
        }
        val request = UNNotificationRequest.requestWithIdentifier(
            identifier = NSUUID().UUIDString(),
            content = content,
            trigger = null,
        )
        UNUserNotificationCenter.currentNotificationCenter()
            .addNotificationRequest(request) { _ -> }
    }
}
