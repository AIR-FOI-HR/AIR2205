package hr.foi.air.mbanking.features.data.repositories

import hr.foi.air.mbanking.features.ApiManager
import com.google.gson.reflect.TypeToken
import hr.foi.air.mbanking.features.domain.models.Notification
import hr.foi.air.mbanking.features.domain.repositories.NotificationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteNotificationRepository @Inject constructor() : NotificationRepository {

    private var notifications: List<Notification> = listOf()

    override suspend fun getAllNotification(): List<Notification> = withContext(Dispatchers.IO) {
        val userAccountsType = object : TypeToken<List<Notification>>() {}
        val result = ApiManager.makeApiCall(
            pathSegments = arrayOf("notification", "get_all.php"),
            returnType = userAccountsType
        )

        if (result.isSuccess) {
            val data = result.getOrNull()
            if (data != null) {
                val notificationList = data.map {
                    Notification(
                        obavijest_id = it.obavijest_id,
                        datum = it.datum,
                        korisnik_id = it.korisnik_id,
                        sadrzaj = it.sadrzaj
                    )
                }
                notifications = notificationList
                return@withContext notificationList
            } else {
                return@withContext emptyList()
            }
        } else {
            return@withContext emptyList()
        }
    }

    override fun getNotificationById(notification_id: Int): Notification? = notifications.firstOrNull { it.obavijest_id == notification_id }
}

