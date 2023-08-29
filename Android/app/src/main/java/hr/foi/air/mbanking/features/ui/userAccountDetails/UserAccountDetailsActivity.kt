package hr.foi.air.mbanking.features.ui.userAccountDetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import hr.foi.air.mbanking.R
import hr.foi.air.mbanking.databinding.ActivityUserAccountDetailsBinding
import hr.foi.air.mbanking.entities.Transaction

@AndroidEntryPoint
class UserAccountDetailsActivity : AppCompatActivity(), OnTransactionClickListener {
    private lateinit var binding: ActivityUserAccountDetailsBinding
    private val viewModel: UserAccountDetailsViewModel by viewModels()
    private lateinit var transactionRecyclerViewAdapter: TransactionRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserAccountDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userAccountType = intent.getIntExtra(ACCOUNT_TYPE, 0)

        initRecyclerViews()

        viewModel.fetchUserAccounts(userAccountType)

        observeUserAccount()

        observeUserTransactions()

        observeUserTransaction()

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initRecyclerViews() {
        binding.transactionRecyclerViewAdapter.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            transactionRecyclerViewAdapter =
                TransactionRecyclerViewAdapter(
                    context,
                    this@UserAccountDetailsActivity
                )
            adapter = transactionRecyclerViewAdapter
        }
    }

    private fun observeUserAccount() {
        viewModel.fetchUserAccountDetailsLiveData.observe(this) {
            if (it != null) {
                binding.layoutUserAccount.txtIban.text = this.getString(R.string.iban, it.iban)
                binding.layoutUserAccount.txtStanje.text =
                    this.getString(R.string.stanje_1_f, it.stanje)
                binding.layoutUserAccount.txtAktivnost.text =
                    this.getString(R.string.aktivnost_1_s, it.aktivnost)
                binding.layoutUserAccount.txtVrstaRacuna.text =
                    this.getString(R.string.vrsta_ra_una_1_s, it.vrsta_racuna.typeName)
                observeUserPayments()
            }
        }
    }


    private fun observeUserPayments() {
        // Sakrivanje prikaza IBAN-a i vrste računa
        binding.layoutUserPayments.txtIban.visibility = View.GONE
        binding.layoutUserPayments.txtVrstaRacuna.visibility = View.GONE
        // Ažuriranje teksta za prikaz uplata i isplata
        binding.layoutUserPayments.txtStanje.text = "Uplate: 150 eura"
        binding.layoutUserPayments.txtAktivnost.text = "Isplate: 450 eura"
    }

    private fun observeUserTransactions() {
        viewModel.fetchTransactionsLiveData.observe(this) {
            if (it != null) {
                // Ako postoje transakcije, zamijeni trenutni sadržaj RecyclerView-a s novim listom
                transactionRecyclerViewAdapter.swapList(it)
            }
        }
    }

    private fun observeUserTransaction() {
        viewModel.fetchTransactionLiveData.observe(this) {
            if (it != null) {
                // Ako postoji transakcija, prikaži dijalog s transakcijom
                showDialog(it)
            }
        }
    }


    private fun showDialog(transaction: Transaction) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Transakcija ${transaction.transakcija_id}")
            .setMessage("Datum: ${transaction.datum_izvrsenja} \n\nPrimatelj: ${transaction.primatelj_iban} \n\nIznos ${transaction.iznos} eura  \n\nOpis plačanja: ${transaction.opis_placanja} \n\nModel: ${transaction.model} \n\nPoziv na broj: ${transaction.poziv_na_broj}")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, UserAccountDetailsActivity::class.java)
        const val ACCOUNT_TYPE = "Account_type_extra"
    }


    override fun onTransactionClicked(transactionId: Int) {
        // Dohvaćanje pojedinačne transakcije temeljem njezinog ID-a
        viewModel.fetchUserTransaction(transactionId)
    }
}