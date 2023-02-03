package hr.foi.air.mbanking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import hr.foi.air.mbanking.LogInActivity.Companion.currentUser
import hr.foi.air.mbanking.api.AccountRequest
import hr.foi.air.mbanking.api.TransactionRequest
import hr.foi.air.mbanking.bankorderRecyclerView.BankOrderAdapter
import hr.foi.air.mbanking.databinding.ActivityBankOrderListBinding
import hr.foi.air.mbanking.entities.Account
import hr.foi.air.mbanking.entities.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BankOrderListActivity : AppCompatActivity() {


    val transactionRequest = TransactionRequest()
    val accountRequest = AccountRequest()

    private lateinit var bankOrderAdapter: BankOrderAdapter
    private lateinit var binding: ActivityBankOrderListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_order_list)

        binding = ActivityBankOrderListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeLayout(0)
        onBackArrowPressed()
        onTekuciRacunButtonPressed()
        onZiroRacunButtonPressed()
    }

    fun initializeLayout(id: Int){
        var currentUserTransactionList = mutableListOf<Transaction>()
        lifecycleScope.launch(Dispatchers.IO){
            val userAccountList = getUserAccounts(currentUser!!.korisnik_id)
            val currentUserAccount = userAccountList[id]
            val allBankOrderList = transactionRequest.getAllTransactions()
            for(transaction in allBankOrderList){

                if(transaction.iban == currentUserAccount.iban){
                    currentUserTransactionList.add(transaction)
                }
            }
            withContext(Dispatchers.Main)
            {
                if(id == 0)
                    binding.btnTekuciRacun.isEnabled = false
                else
                    binding.btnZiroRacun.isEnabled = false

                binding.tvAccountIban.setText(currentUserAccount.iban)
                bankOrderAdapter = BankOrderAdapter(currentUserTransactionList)
                binding.bankOrderView.adapter = bankOrderAdapter
                binding.bankOrderView.layoutManager = LinearLayoutManager(this@BankOrderListActivity)
                bankOrderAdapter.setOnButtonCLickListener(object : BankOrderAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        val intent = Intent(this@BankOrderListActivity, BankOrderActivity::class.java)
                        intent.putExtra("transakcijaId", currentUserTransactionList[position].transakcija_id)
                        intent.putExtra("iznos", currentUserTransactionList[position].iznos.toString())
                        intent.putExtra("opisPlacanja", currentUserTransactionList[position].opis_placanja)
                        intent.putExtra("model", currentUserTransactionList[position].model)
                        intent.putExtra("pozivNaBroj", currentUserTransactionList[position].poziv_na_broj)
                        intent.putExtra("datumIzvrsenja", currentUserTransactionList[position].datum_izvrsenja)
                        intent.putExtra("vrstaTransakcije", currentUserTransactionList[position].vrsta_transakcije_id)
                        intent.putExtra("racunIban", currentUserTransactionList[position].iban)
                        intent.putExtra("valuta", currentUserTransactionList[position].valuta_id)
                        startActivity(intent)
                    }
                })
            }
        }
    }

    fun getUserAccounts(userId: Int): List<Account> {
        val userAccountList = mutableListOf<Account>()
        val accountList = accountRequest.getAllAccounts()
        for(account in accountList){
            if(account.korisnik_id == userId) {
                userAccountList.add(account)
            }
        }
        return  userAccountList
    }

    fun onBackArrowPressed(){
        binding.buttonBack.setOnClickListener(){
            finish()
        }
    }

    fun onTekuciRacunButtonPressed(){
        binding.btnTekuciRacun.setOnClickListener(){
            initializeLayout(0)
        }
    }
    fun onZiroRacunButtonPressed(){
        binding.btnZiroRacun.setOnClickListener(){
            initializeLayout(1)
        }
    }
}