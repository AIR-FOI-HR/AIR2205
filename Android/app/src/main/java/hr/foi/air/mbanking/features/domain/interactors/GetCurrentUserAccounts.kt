package hr.foi.air.mbanking.features.domain.interactors

import hr.foi.air.mbanking.features.domain.models.UserAccount
import hr.foi.air.mbanking.features.domain.repositories.UserAccountRepository
import javax.inject.Inject

class GetCurrentUserAccounts @Inject constructor(private val userAccountRepository: UserAccountRepository) {
    operator fun invoke(): List<UserAccount> =
        userAccountRepository.getCurrentUserAccounts()
}