package hr.foi.air.mbanking.features.domain.models


data class UserAccount(
    val iban: String,
    val stanje: Double,
    val aktivnost: String,
    val korisnik_id: Int,
    var vrsta_racuna_id: Int,
    var vrsta_racuna: AccountType = AccountType.TEKUCI_RACUN,
    val qr_kod: String
){
    init {
        vrsta_racuna = if (vrsta_racuna_id == 4){
            AccountType.ZIRO_RACUN
        }else{
            AccountType.TEKUCI_RACUN
        }
    }
}

enum class AccountType(val typeName: String) {
    ZIRO_RACUN("Žiro račun"),
    TEKUCI_RACUN("Tekući račun");

}

