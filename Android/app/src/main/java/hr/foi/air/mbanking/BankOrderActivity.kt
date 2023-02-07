package hr.foi.air.mbanking

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
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
import java.util.*
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import java.util.concurrent.Executors
import javax.mail.*
import javax.mail.internet.*

class BankOrderActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var binding: ActivityBankOrderBinding
    var transactionType: TransactionType? = null
    var currency: Currency? = null
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
        transactionType = getTransactionType(intent.getIntExtra("vrstaTrasakcije", 0))
        currency = getCurrency(intent.getIntExtra("valuta", 0))
        lifecycleScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                if(transactionType!!.naziv != "Vlastiti prijenos") {
                    binding.primateljPlatiteljNameText.setText("Pošiljatelj")
                    binding.primateljPlatiteljRacunText.setText("Račun pošiljatelja")
                }
                binding.izvrseniNalozi.setText("IZVRŠENI NALOZI - ".plus(intent.getStringExtra("datumIzvrsenja")))
                binding.iznosValute.setText(intent.getStringExtra("iznos").plus(" ").plus(currency!!.oznaka))
                binding.vrstaTransakcije.setText(transactionType!!.naziv)
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
            sendEmail()
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

    fun sendEmail() {
        val recieverEmail = currentUser!!.email
        val messageSubject = "Podaci o nalogu broj: ".plus(intent.getStringExtra("transakcijaId"))
        val emailMessage = generateEmailMessage()
        val senderEmail = "mbanking.podrska@gmail.com"
        val senderPassword = "ciqlncpuaiupcvde"

        progressBar = ProgressBar(this, null, android.R.attr.progressBarStyle)
        progressBar.isIndeterminate = true
        progressBar.indeterminateDrawable.setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.SRC_IN)
        val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, resources.displayMetrics).toInt()
        val height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, resources.displayMetrics).toInt()
        val layoutParams = FrameLayout.LayoutParams(width, height)
        layoutParams.gravity = Gravity.CENTER
        layoutParams.bottomMargin = 15
        addContentView(progressBar, layoutParams)

        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            val props = Properties()
            props["mail.smtp.host"] = "smtp.gmail.com"
            props["mail.smtp.socketFactory.port"] = "465"
            props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
            props["mail.smtp.auth"] = "true"
            props["mail.smtp.port"] = "465"

            val session = Session.getDefaultInstance(props,
                object : javax.mail.Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(senderEmail, senderPassword)
                    }
                })

            try {
                val mimeMessage = MimeMessage(session)
                mimeMessage.setFrom(InternetAddress(senderEmail))
                mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recieverEmail))
                mimeMessage.subject = messageSubject
                mimeMessage.setText(emailMessage)
                Transport.send(mimeMessage)

            } catch (e: MessagingException) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(this, "Error: $e", Toast.LENGTH_LONG).show()
                }
            }

            Handler(Looper.getMainLooper()).post {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Email poslan!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun generateEmailMessage(): String?{
        var message = StringBuilder()
        message.append("Poštovani,\n \nu privitku se nalaze podaci o vašem plaćenom nalogu. \n ")
        message.append("\nPrimatelj: ".plus(currentUser!!.ime).plus(" ").plus(currentUser!!.prezime))
        message.append("\nRačun primatelja: ".plus(intent.getStringExtra("racunIban")))
        message.append("\nDatum transakcije: ".plus(intent.getStringExtra("datumIzvrsenja")))
        message.append("\nIznos transakcije: ".plus(intent.getStringExtra("iznos").plus(" ").plus(currency?.naziv)))
        message.append("\nOpis plaćanja: ".plus(intent.getStringExtra("opisPlacanja")))
        message.append("\nModel i poziv na broj: ".plus(intent.getStringExtra("model").plus("-").plus(intent.getStringExtra("pozivNaBroj"))))
        return message.toString()
    }
}