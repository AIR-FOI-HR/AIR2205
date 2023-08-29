package hr.foi.air.mbanking.features.ui.userAccountDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.foi.air.mbanking.entities.Transaction
import hr.foi.air.mbanking.features.domain.interactors.GetUserAccountDetails
import hr.foi.air.mbanking.features.domain.interactors.GetUserTransactions
import hr.foi.air.mbanking.features.domain.models.UserAccount
import hr.foi.air.mbanking.features.domain.interactors.GetUserTransactionById
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class UserAccountDetailsViewModel @Inject constructor(
    private val getUserAccountDetails: GetUserAccountDetails,
    private val getUserTransactions: GetUserTransactions,
    private val getUserTransactionById: GetUserTransactionById
) : ViewModel() {

    private val userAccountDetailsLiveData = MutableLiveData<UserAccount?>()
    val fetchUserAccountDetailsLiveData: LiveData<UserAccount?> = userAccountDetailsLiveData

    private val userTransactionsLiveData = MutableLiveData<List<Transaction>?>()
    val fetchTransactionsLiveData: LiveData<List<Transaction>?> = userTransactionsLiveData

    private val userTransactionLiveData = MutableLiveData<Transaction?>()
    val fetchTransactionLiveData: LiveData<Transaction?> = userTransactionLiveData

    fun fetchUserAccounts(userAccountType: Int) {
        val response = getUserAccountDetails(accountType = userAccountType)
        if (response != null) {
            fetchUserTransactions()
        }
        userAccountDetailsLiveData.postValue(response)
    }

    private fun fetchUserTransactions() {
        viewModelScope.launch {
            withContext(viewModelScope.coroutineContext) {
                val response = getUserTransactions()
                userTransactionsLiveData.postValue(response)
            }
        }
    }

    fun fetchUserTransaction(transactionId: Int) {
        val response = getUserTransactionById(transactionId)
        userTransactionLiveData.postValue(response)
    }
}