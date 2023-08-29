package hr.foi.air.mbanking.features.domain.repositories

import hr.foi.air.mbanking.entities.Transaction
import hr.foi.air.mbanking.entities.User
import hr.foi.air.mbanking.features.domain.models.UserAccount

interface UserAccountRepository {

    suspend fun getUserAccounts(userIban: String): List<UserAccount>

    fun getUserAccount(accountType: Int): UserAccount?

    fun getCurrentUserAccounts(): List<UserAccount>

    suspend fun getAllTransaction(): List<Transaction>

    fun getTransactionById(transactionId: Int): Transaction?

    suspend fun createTransaction(transaction: Transaction): Boolean

    suspend fun checkRecipientAccountByIban(userIban: String): Boolean
}