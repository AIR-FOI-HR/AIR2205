package hr.foi.air.mbanking.features.ui.fundstransaction

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import hr.foi.air.mbanking.databinding.ActivityFundsTransactionBinding
import hr.foi.air.mbanking.entities.Transaction
import hr.foi.air.mbanking.features.domain.models.TransactionValidationMessages

@AndroidEntryPoint
class FundsTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFundsTransactionBinding
    private val viewModel: FundsTransactionViewModel by viewModels()
    private var transaction: Transaction = Transaction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFundsTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeInputTextFocus()
        observeErrors()

        val userIban = intent.getStringExtra(USER_IBAN_FUNDS) ?: ""
         viewModel.fetchUserAccountsList(userIban = userIban)

        observeUser()
        observeTransaction()

        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnPay.setOnClickListener {
            viewModel.createTransaction(transaction)
        }
    }

     private fun observeInputTextFocus() {
        binding.editTextPrimateljban.doAfterTextChanged {
            transaction.primatelj_iban = it.toString()
        }

        binding.editTextIznos.doAfterTextChanged {
            transaction.iznos = it.toString().toDouble()
        }
        binding.editTextOpisPlacanja.doAfterTextChanged {
            transaction.opis_placanja = it.toString()
        }
        binding.editTextModel.doAfterTextChanged {
            transaction.model = it.toString()
        }

        binding.editTextPozivNaBroj.doAfterTextChanged {
            transaction.poziv_na_broj = it.toString()
        }
    }

     private fun observeUser() {
        viewModel.fetchUserLiveData.observe(this) { userList ->
            if (userList.size == 1) {
                binding.btnPlatiteljIban.apply {
                    text = "Platitelj: ${userList[0].iban}"
                    isEnabled = false
                }
            } else {
                 binding.btnPlatiteljIban.apply {
                    isEnabled = true
                    setOnClickListener {
                        val accounts = userList.map { it.vrsta_racuna.typeName }.toTypedArray()
                        var inputSelected = -1
                        val builder = MaterialAlertDialogBuilder(this@FundsTransactionActivity)
                        builder.setTitle("Odaberi racun")

                        val adapter = ArrayAdapter(
                            this@FundsTransactionActivity,
                            androidx.appcompat.R.layout.abc_select_dialog_material,
                            accounts
                        )
                        builder.setSingleChoiceItems(adapter, inputSelected) { dialog, item ->
                            inputSelected = item
                            dialog.dismiss()
                        }

                        val levelDialog = builder.create()
                        levelDialog.show()

                        levelDialog.setOnDismissListener {
                            if (inputSelected != -1) {
                                val selectedUser = userList[inputSelected]
                                text = selectedUser.iban
                                viewModel.setSelectedUser(selectedUser)
                            }
                        }
                    }
                }
            }
        }
    }

     private fun observeTransaction() {
        viewModel.fetchTransactionLiveData.observe(this) {
            if (it) finish() else createToast("Greska")
        }
    }

    private fun observeErrors() {
        viewModel.fetchErrorsLiveData.observe(this) { messages ->
            if (messages != null) {
                messages.forEach {
                    when (it) {
                        TransactionValidationMessages.SENDER_NOT_EXIST -> binding.btnPlatiteljIban.error =
                            it.message

                        TransactionValidationMessages.RECIPIENT_NOT_EXIST -> binding.editTextPrimateljban.error =
                            it.message

                        TransactionValidationMessages.LOW_FUNDS -> binding.editTextIznos.error =
                            it.message

                        TransactionValidationMessages.PAYMENT_DESCRIPTION -> binding.editTextOpisPlacanja.error =
                            it.message

                        TransactionValidationMessages.EMPTY_MODEL -> binding.editTextModel.error =
                            it.message

                        TransactionValidationMessages.EMPTY_CALLING_NUMBER -> binding.textInputPozivNaBroj.error =
                            it.message

                        TransactionValidationMessages.EMPTY_FIELDS -> createToast(it.message)

                        TransactionValidationMessages.EMPTY_FUNDS -> binding.editTextIznos.error =
                            it.message
                    }
                }
            } else {
                binding.apply {
                    btnPlatiteljIban.error = null
                    editTextPrimateljban.error = null
                    editTextIznos.error = null
                    editTextOpisPlacanja.error = null
                    editTextModel.error = null
                    editTextPozivNaBroj.error = null
                }
            }
        }
    }

    private fun createToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
         fun createIntent(context: Context) = Intent(context, FundsTransactionActivity::class.java)
        // Ključ za dodatne informacije u Intentu
        const val USER_IBAN_FUNDS = "USER_IBAN"
    }
}