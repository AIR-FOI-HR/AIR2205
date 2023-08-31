package hr.foi.air.mbanking.features.ui.fundstransaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.foi.air.mbanking.entities.Transaction
import hr.foi.air.mbanking.features.domain.interactors.CreateUserTransaction
import hr.foi.air.mbanking.features.domain.interactors.GetUserAccounts
import hr.foi.air.mbanking.features.domain.interactors.ValidateTransactionsInputField
import hr.foi.air.mbanking.features.domain.models.TransactionValidationMessages
import hr.foi.air.mbanking.features.domain.models.UserAccount
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class FundsTransactionViewModel @Inject constructor(
    private val validateTransactionsInputField: ValidateTransactionsInputField,
    private val createUserTransaction: CreateUserTransaction,
    private val getUserAccounts: GetUserAccounts
) : ViewModel() {

    private val errorsLiveData = MutableLiveData<List<TransactionValidationMessages>?>()
    val fetchErrorsLiveData: LiveData<List<TransactionValidationMessages>?> = errorsLiveData

    private val transactionLiveData = MutableLiveData<Boolean>()
    val fetchTransactionLiveData: LiveData<Boolean> = transactionLiveData

    private val userLiveData = MutableLiveData<List<UserAccount>>()
    val fetchUserLiveData: LiveData<List<UserAccount>> = userLiveData

    private var selectedUserAccount: UserAccount? = null

    fun fetchUserAccountsList(userIban: String) {
        viewModelScope.launch {
            withContext(viewModelScope.coroutineContext) {
                val userList = getUserAccounts(userIban)
                selectedUserAccount = userList[0]

                userLiveData.postValue(userList)
            }
        }
    }

    fun setSelectedUser(user: UserAccount) {
        selectedUserAccount = user
    }

    fun createTransaction(transaction: Transaction?) {
        viewModelScope.launch {
            withContext(viewModelScope.coroutineContext) {
                if (selectedUserAccount == null) {
                    return@withContext
                }
                val isTransactionValid =
                    validateTransactionsInputField(transaction, selectedUserAccount!!)
                if (isTransactionValid.isEmpty() && transaction != null) {
                    errorsLiveData.postValue(null)
                    val newTransaction = Transaction(
                        platitelj_iban = selectedUserAccount!!.iban,
                        primatelj_iban = transaction.primatelj_iban,
                        iznos = transaction.iznos,
                        opis_placanja = transaction.opis_placanja,
                        model = transaction.model,
                        poziv_na_broj = transaction.poziv_na_broj,
                        datum_izvrsenja = SimpleDateFormat("yyyy-MM-dd").format(Date())
                    )
                    val response = createUserTransaction(newTransaction)
                    if (response) {
                        transactionLiveData.postValue(true)
                    } else {
                        transactionLiveData.postValue(false)
                    }
                } else {
                    errorsLiveData.postValue(isTransactionValid)
                }
            }
        }
    }
}