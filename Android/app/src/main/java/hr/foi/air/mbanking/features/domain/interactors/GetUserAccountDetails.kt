package hr.foi.air.mbanking.features.domain.interactors

import hr.foi.air.mbanking.features.domain.models.UserAccount
import hr.foi.air.mbanking.features.domain.repositories.UserAccountRepository
import javax.inject.Inject

class GetUserAccountDetails @Inject constructor(private val userAccountRepository: UserAccountRepository) {
    operator fun invoke(accountType: Int): UserAccount? =
        userAccountRepository.getUserAccount(accountType)
}