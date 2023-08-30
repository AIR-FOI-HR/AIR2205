package hr.foi.air.mbanking.features.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.foi.air.mbanking.features.domain.interactors.GetAllNotification
import hr.foi.air.mbanking.features.domain.interactors.GetNotificationById
import hr.foi.air.mbanking.features.domain.models.Notification
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getAllNotification: GetAllNotification,
    private val getNotificationById: GetNotificationById
) : ViewModel() {

    private val notificationsLiveData = MutableLiveData<List<Notification>?>()
    val fetchNotificationsLiveData: LiveData<List<Notification>?> = notificationsLiveData

    private val notificationLiveData = MutableLiveData<Notification?>()
    val fetchNotificationLiveData: LiveData<Notification?> = notificationLiveData

    init {
        fetchNotifications()
    }

    private fun fetchNotifications() {
        viewModelScope.launch {
            withContext(viewModelScope.coroutineContext) {
                val response = getAllNotification()
                notificationsLiveData.postValue(response)
            }
        }
    }

    fun fetchNotification(notificationId: Int) {
        val response = getNotificationById(notificationId)
        notificationLiveData.postValue(response)

    }
}