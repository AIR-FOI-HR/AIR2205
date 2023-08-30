package hr.foi.air.mbanking.features.domain.repositories

import hr.foi.air.mbanking.features.domain.models.Notification

interface NotificationRepository {

    suspend fun getAllNotification(): List<Notification>

    fun getNotificationById(notification_id: Int): Notification?
}