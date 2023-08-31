package hr.foi.air.mbanking.features.domain.interactors

import hr.foi.air.mbanking.entities.Transaction
import hr.foi.air.mbanking.features.data.repositories.RemoteUserAccountRepository
import hr.foi.air.mbanking.features.domain.repositories.UserAccountRepository
import javax.inject.Inject

class CreateUserTransaction @Inject constructor(private val userAccountRepository: UserAccountRepository) {
    suspend operator fun invoke(transaction: Transaction): Boolean =
        userAccountRepository.createTransaction(transaction)
}