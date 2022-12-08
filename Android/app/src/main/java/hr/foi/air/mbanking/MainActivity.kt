package hr.foi.air.mbanking



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import hr.foi.air.mbanking.databinding.LayoutUserAccountBinding
import hr.foi.air.mbanking.entities.Account
import hr.foi.air.mbanking.entities.Transaction
import hr.foi.air.mbanking.transactionRecyclerView.TransactionAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var account : Account
    private lateinit var binding: LayoutUserAccountBinding
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mockUpDataForAccountAndTransactions()

        binding = LayoutUserAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initializeLayout()
        onMenuPressed()
    }

    private fun mockUpDataForAccountAndTransactions(){
        val ime = "Pero"
        val prezime = "Peric"
        val username = "Pero123"
        val iban = "HR6224840083119462973"
        val id = 1
        val promet = 1500.00
        val vrstaRacuna = "Žiro"
        val stanje = 3400.00

        val listaTransakcija = mutableListOf<Transaction>(
            Transaction("uplata", 100.00, "HRK"),
            Transaction("isplata", 20.00, "HRK"),
            Transaction("isplata", 70.00, "HRK"),
            Transaction("uplata", 45.00, "HRK"),
            Transaction("isplata", 121.00, "HRK"),
        )

        account = Account(id, username, ime, prezime, vrstaRacuna, iban, promet, stanje, listaTransakcija)
    }


    fun initializeLayout(){
        binding.username.text = "Dobrodošli ".plus(account.username)
        binding.accountDetails.text = account.vrstaRacuna
            .plus(" račun")
            .plus("\n")
            .plus(account.iban)
            .plus("\n")
            .plus(account.promet.toString())
            .plus("\n")
            .plus("Raspoloživo: ")
            .plus(account.stanje)

        transactionAdapter = TransactionAdapter(account.transakcije)
        binding.transactionsView.adapter = transactionAdapter
        binding.transactionsView.layoutManager = LinearLayoutManager(this)
    }

    fun onMenuPressed(){
        binding.menuButton.setOnClickListener{
            val intent1 = Intent(this, MenuActivity::class.java)
            startActivity(intent1)
        }
    }
}