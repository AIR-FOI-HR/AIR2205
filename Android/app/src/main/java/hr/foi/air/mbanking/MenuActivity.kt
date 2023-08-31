package hr.foi.air.mbanking


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import foi.projekt.skeniraj_i_plati.QRScanActivity
import hr.foi.air.mbanking.databinding.LayoutMenuBinding
import hr.foi.air.mbanking.features.ui.notification.NotificationActivity
import hr.foi.air.mbanking.features.ui.useraccounts.UserAccountsActivity

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: LayoutMenuBinding
    private lateinit var glavniRacun: String
    private lateinit var iban: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutMenuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        glavniRacun = intent.getStringExtra("GlavniRacun").toString()
        iban = intent.getStringExtra("IBAN").toString()

        binding.buttonPrikazRacuna.setOnClickListener {
            val intent = UserAccountsActivity.createIntent(this)
            intent.putExtra(UserAccountsActivity.USER_IBAN, iban)
            startActivity(intent)
        }

        binding.buttonObavijesti.setOnClickListener {
            startActivity(NotificationActivity.createIntent(this))
        }

        onBackArrowPressed()
        onLogout()
        onSkenirajIPlatiPressed()
        onGeneriranjeQrKodaPressed()
        onBankOrderPressed()
    }

    fun onSkenirajIPlatiPressed() {
        binding.buttonSkenirajIPlati.setOnClickListener {
            val intent1 = Intent(this, QRScanActivity::class.java)
            intent1.putExtra("GlavniRacun", glavniRacun)
            startActivity(intent1)
        }
    }

    fun onGeneriranjeQrKodaPressed() {
        binding.buttonGenerirajQrKod.setOnClickListener {
            val intent1 = Intent(this, GenerateCodeActivity::class.java)
            intent1.putExtra("GlavniRacun", glavniRacun)
            startActivity(intent1)
        }
    }

    fun onBackArrowPressed() {
        binding.buttonBack.setOnClickListener() {
            finish()
        }
    }

    fun onLogout() {
        binding.buttonOdjava.setOnClickListener {
            val sharedPreferences = getSharedPreferences("ACTIVE_USER", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("USER_ID", 0)
            editor.apply()

            val logInView = Intent(this, LogInActivity::class.java)
            startActivity(logInView)
        }
    }

    fun onBankOrderPressed() {
        binding.buttonIzvrseniNalozi.setOnClickListener {
            val bankOrderView = Intent(this, BankOrderListActivity::class.java)
            startActivity(bankOrderView)
        }
    }
}