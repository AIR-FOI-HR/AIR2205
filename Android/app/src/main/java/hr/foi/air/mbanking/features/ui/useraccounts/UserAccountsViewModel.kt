package hr.foi.air.mbanking.features.ui.useraccounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.foi.air.mbanking.features.domain.interactors.GetUserAccounts
import hr.foi.air.mbanking.features.domain.models.UserAccount
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserAccountsViewModel @Inject constructor(
    private val getUserAccounts: GetUserAccounts
) : ViewModel() {

    private val userAccountsLiveData = MutableLiveData<List<UserAccount>?>()
    val fetchUserAccountsLiveData: LiveData<List<UserAccount>?> = userAccountsLiveData

    fun fetchUserAccounts(userIban: String) {
        viewModelScope.launch {
            withContext(viewModelScope.coroutineContext) {
                val response = getUserAccounts(userIban = userIban)
                userAccountsLiveData.postValue(response)
            }
        }
    }
}