package hr.foi.air.mbanking.features.domain.interactors

import hr.foi.air.mbanking.features.domain.models.UserAccount
import hr.foi.air.mbanking.features.domain.repositories.UserAccountRepository
import javax.inject.Inject

class GetUserAccounts @Inject constructor(private val userAccountRepository: UserAccountRepository) {
    suspend operator fun invoke(userIban: String): List<UserAccount> =
        userAccountRepository.getUserAccounts(userIban)
}