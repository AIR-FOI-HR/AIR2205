package hr.foi.air.mbanking.features.data.repositories

import hr.foi.air.mbanking.features.ApiManager
import com.google.gson.reflect.TypeToken
import hr.foi.air.mbanking.entities.Transaction
import hr.foi.air.mbanking.features.domain.models.UserAccount
import hr.foi.air.mbanking.features.domain.repositories.UserAccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteUserAccountRepository @Inject constructor() : UserAccountRepository {

    private var userAccounts: List<UserAccount> = listOf()
    private var transactions: List<Transaction> = listOf()

    override suspend fun getUserAccounts(userIban: String): List<UserAccount> {
        return getUserAcc(userIban)
    }

    override fun getUserAccount(accountType: Int): UserAccount? {
        return userAccounts.firstOrNull {
            it.vrsta_racuna_id == accountType
        }
    }

    override fun getCurrentUserAccounts(): List<UserAccount> = userAccounts


    override suspend fun getAllTransaction(): List<Transaction> =
        withContext(Dispatchers.IO) {
            val userAccountsType = object : TypeToken<List<Transaction>>() {}

            val result = ApiManager.makeApiCall(
                pathSegments = arrayOf("transaction", "get_all.php"),
                returnType = userAccountsType
            )

            if (result.isSuccess) {
                val data = result.getOrNull()

                if (data != null) {
                    val transactionList = data.map {
                        Transaction(
                            transakcija_id = it.transakcija_id,
                            iznos = it.iznos,
                            opis_placanja = it.opis_placanja,
                            model = it.model,
                            poziv_na_broj = it.poziv_na_broj,
                            datum_izvrsenja = it.datum_izvrsenja,
                            primatelj_iban = it.primatelj_iban,
                            platitelj_iban = it.platitelj_iban,
                            valuta_id = it.valuta_id
                        )
                    }

                    transactions = transactionList

                    return@withContext transactionList
                } else {
                    return@withContext emptyList()
                }
            } else {
                return@withContext emptyList()
            }
        }

    override fun getTransactionById(transactionId: Int): Transaction? =
        transactions.firstOrNull { it.transakcija_id == transactionId }

    override suspend fun createTransaction(transaction: Transaction): Boolean =
        withContext(Dispatchers.IO) {
            val postParams = mapOf(
                "primatelj_iban" to transaction.primatelj_iban,
                "platitelj_iban" to transaction.platitelj_iban,
                "iznos" to transaction.iznos,
                "opis_placanja" to transaction.opis_placanja,
                "model" to transaction.model,
                "poziv_na_broj" to transaction.poziv_na_broj,
                "datum_izvrsenja" to transaction.datum_izvrsenja,
                "valuta_id" to 1,
            )

            val userAccountsType = object : TypeToken<Any>() {}

            val result = ApiManager.makeApiCall(
                pathSegments = arrayOf("transaction", "create.php"),
                postBody = postParams,
                returnType = userAccountsType
            )

            if (result.isSuccess) {
                val data = result.getOrNull()
                return@withContext data != null
            } else {
                return@withContext false
            }
        }

    override suspend fun checkRecipientAccountByIban(userIban: String): Boolean =
        withContext(Dispatchers.IO) {
            val queryParams = mapOf("iban" to userIban)
            val userAccountsType = object : TypeToken<List<UserAccount>>() {}
            val result = ApiManager.makeApiCall(
                pathSegments = arrayOf("account", "get.php"),
                queryParams = queryParams,
                returnType = userAccountsType
            )
            if (result.isSuccess) {
                val data = result.getOrNull()
                return@withContext data != null
            } else {
                return@withContext false
            }
        }


    private suspend fun getUserAcc(userIban: String): List<UserAccount> =
        withContext(Dispatchers.IO) {
            val queryParams = mapOf("iban" to userIban)
            val userAccountsType = object : TypeToken<List<UserAccount>>() {}
            val result = ApiManager.makeApiCall(
                pathSegments = arrayOf("account", "get.php"),
                queryParams = queryParams,
                returnType = userAccountsType
            )

            if (result.isSuccess) {
                val data = result.getOrNull()
                if (data != null) {
                    val accounts = data.map {
                        UserAccount(
                            iban = it.iban,
                            stanje = it.stanje,
                            aktivnost = it.aktivnost,
                            korisnik_id = it.korisnik_id,
                            vrsta_racuna_id = it.vrsta_racuna_id,
                            qr_kod = it.qr_kod
                        )
                    }
                    userAccounts = accounts
                    return@withContext accounts
                } else {
                    return@withContext emptyList()
                }
            } else {
                return@withContext emptyList()
            }
        }


}
