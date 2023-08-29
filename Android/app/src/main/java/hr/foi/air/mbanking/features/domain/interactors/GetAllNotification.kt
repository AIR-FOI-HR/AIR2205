package hr.foi.air.mbanking.features.domain.interactors

import hr.foi.air.mbanking.features.domain.models.Notification
import hr.foi.air.mbanking.features.domain.repositories.NotificationRepository
import javax.inject.Inject

class GetAllNotification @Inject constructor(private val notificationRepository: NotificationRepository) {
    suspend operator fun invoke(): List<Notification> =
        notificationRepository.getAllNotification()
}