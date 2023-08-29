package hr.foi.air.mbanking.features.domain.interactors

import hr.foi.air.mbanking.entities.Transaction
import hr.foi.air.mbanking.features.domain.repositories.UserAccountRepository
import javax.inject.Inject

class GetUserTransactionById @Inject constructor(private val userAccountRepository: UserAccountRepository) {
    operator fun invoke(transactionId: Int): Transaction? =
        userAccountRepository.getTransactionById(transactionId)
}