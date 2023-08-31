package hr.foi.air.mbanking.features.domain.interactors

import hr.foi.air.mbanking.features.domain.repositories.UserAccountRepository
import javax.inject.Inject

class GetRecipientAccountByIban @Inject constructor(private val userAccountRepository: UserAccountRepository) {
    suspend operator fun invoke(recipientIban: String): Boolean =
        userAccountRepository.checkRecipientAccountByIban(recipientIban)
}