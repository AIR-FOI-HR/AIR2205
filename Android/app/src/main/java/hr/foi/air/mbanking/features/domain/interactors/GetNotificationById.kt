package hr.foi.air.mbanking.features.domain.interactors

import hr.foi.air.mbanking.features.domain.models.Notification
import hr.foi.air.mbanking.features.domain.repositories.NotificationRepository
import javax.inject.Inject

class GetNotificationById @Inject constructor(private val notificationRepository: NotificationRepository) {
    operator fun invoke(notificationId: Int): Notification? =
        notificationRepository.getNotificationById(notificationId)
}