package hr.foi.air.mbanking.features.domain.models

enum class TransactionValidationMessages(val message: String){
    SENDER_NOT_EXIST("Posiljatelj ne postoji!"),
    RECIPIENT_NOT_EXIST("Primatelj ne postoji!"),
    LOW_FUNDS("Nedovoljan iznos na računu"),
    PAYMENT_DESCRIPTION("Upisite opis placanja"),
    EMPTY_MODEL("Upisite model"),
    EMPTY_FUNDS("Upisite iznos"),
    EMPTY_CALLING_NUMBER("Upisite poziv na broj"),
    EMPTY_FIELDS("Ispunite sva polja"),
}