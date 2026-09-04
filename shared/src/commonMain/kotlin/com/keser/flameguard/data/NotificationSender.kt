package com.keser.flameguard.data

interface NotificationSender {
    fun send(title: String, body: String)
}
