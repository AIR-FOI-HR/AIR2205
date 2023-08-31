package hr.foi.air.mbanking.features.domain.interactors

import hr.foi.air.mbanking.entities.Transaction
import hr.foi.air.mbanking.features.domain.models.TransactionValidationMessages
import hr.foi.air.mbanking.features.domain.models.UserAccount
import hr.foi.air.mbanking.features.domain.repositories.UserAccountRepository
import javax.inject.Inject

class ValidateTransactionsInputField @Inject constructor(private val userAccountRepository: UserAccountRepository) {
    suspend operator fun invoke(
        transaction: Transaction?,
        user: UserAccount
    ): List<TransactionValidationMessages> {
        val validation = mutableListOf<TransactionValidationMessages>()

        if (transaction == null) {
            validation.add(TransactionValidationMessages.EMPTY_FIELDS)
            return validation
        }
        if (!userAccountRepository.checkRecipientAccountByIban(transaction.primatelj_iban)) {
            validation.add(TransactionValidationMessages.RECIPIENT_NOT_EXIST)
        } else if (transaction.iznos == 0.0) {
            validation.add(TransactionValidationMessages.EMPTY_FUNDS)
            return validation
        } else if (user.stanje < transaction.iznos) {
            validation.add(TransactionValidationMessages.LOW_FUNDS)
        } else if (transaction.opis_placanja.isEmpty()) {
            validation.add(TransactionValidationMessages.PAYMENT_DESCRIPTION)
            return validation
        } else if (transaction.model.isEmpty()) {
            validation.add(TransactionValidationMessages.EMPTY_MODEL)
            return validation
        } else if (transaction.poziv_na_broj.isEmpty()) {
            validation.add(TransactionValidationMessages.EMPTY_CALLING_NUMBER)
            return validation
        } else if (transaction.primatelj_iban.isEmpty()) {
            validation.add(TransactionValidationMessages.RECIPIENT_NOT_EXIST)
            return validation
        }

        return validation
    }
}