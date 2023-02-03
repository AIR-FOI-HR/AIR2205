package hr.foi.air.mbanking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.lifecycle.lifecycleScope
import hr.foi.air.mbanking.LogInActivity.Companion.currentUser
import hr.foi.air.mbanking.api.CurrencyRequest

import hr.foi.air.mbanking.api.TransactionTypeRequest

import hr.foi.air.mbanking.databinding.ActivityBankOrderBinding
import hr.foi.air.mbanking.entities.Currency
import hr.foi.air.mbanking.entities.TransactionType
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class BankOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBankOrderBinding
    val transactionTypeRequest = TransactionTypeRequest()
    val currencyRequest = CurrencyRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_order)

        binding = ActivityBankOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeLayout()
        onButtonBackPressed()
        onButtonCopyTransactionPressed()
        onButtonShareTransactionPressed()
    }

    fun initializeLayout(){
        val transactionType = getTransactionType(intent.getIntExtra("vrstaTrasakcije", 0))
        val currency = getCurrency(intent.getIntExtra("valuta", 0))
        lifecycleScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                if(transactionType!!.naziv != "Vlastiti prijenos") {
                    binding.primateljPlatiteljNameText.setText("Pošiljatelj")
                    binding.primateljPlatiteljRacunText.setText("Račun pošiljatelja")
                }
                binding.izvrseniNalozi.setText("IZVRŠENI NALOZI - ".plus(intent.getStringExtra("datumIzvrsenja")))
                binding.iznosValute.setText(intent.getStringExtra("iznos").plus(" ").plus(currency!!.oznaka))
                binding.vrstaTransakcije.setText(transactionType.naziv)
                binding.primateljPlatiteljName.setText(currentUser!!.ime.plus(" ").plus(
                    currentUser!!.prezime))
                binding.primateljPlatiteljRacun.setText(intent.getStringExtra("racunIban"))
                binding.opis.setText(intent.getStringExtra("opisPlacanja"))
                binding.modelIPozivNaBroj.setText(intent.getStringExtra("model").plus("-").plus(intent.getStringExtra("pozivNaBroj")))
            }
        }
    }
    fun onButtonBackPressed(){
        binding.buttonBack.setOnClickListener(){
            finish()
        }
    }

    fun onButtonShareTransactionPressed(){
        binding.buttonShareTransaction.setOnClickListener(){

        }
    }

    fun onButtonCopyTransactionPressed(){
        binding.buttonCopyTransaction.setOnClickListener(){

        }
    }

    fun getTransactionType(id: Int): TransactionType? {
        return transactionTypeRequest.getTransactionType(id)
    }

    fun getCurrency(id: Int): Currency?{
        return currencyRequest.getCurrency(id)
    }


}