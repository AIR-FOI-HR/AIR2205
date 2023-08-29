package hr.foi.air.mbanking.features.domain.interactors

import hr.foi.air.mbanking.entities.Transaction
import hr.foi.air.mbanking.features.domain.repositories.UserAccountRepository
import javax.inject.Inject

class GetUserTransactions @Inject constructor(private val userAccountRepository: UserAccountRepository) {
    suspend operator fun invoke(): List<Transaction> =
        userAccountRepository.getAllTransaction()
}