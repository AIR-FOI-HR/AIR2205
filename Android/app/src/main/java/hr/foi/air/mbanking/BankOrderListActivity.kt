package hr.foi.air.mbanking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        binding.bankOrderView.
    }

    fun initializeLayout(id: Int){
        var currentUserTransactionList = mutableListOf<Transaction>()
        //currentUserTransactionList.add(Transaction(122.00, "Kurac", "Kurac 2", "Kurac 3", "229123", 1, "1", 1))
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

    fun onRecyclerViewButtonPressed(){
        binding.bankOrderView.
    }
}