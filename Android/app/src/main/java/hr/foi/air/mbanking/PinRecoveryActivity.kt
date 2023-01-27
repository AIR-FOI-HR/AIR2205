package hr.foi.air.mbanking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import hr.foi.air.mbanking.LogInActivity.Companion.currentUser
import hr.foi.air.mbanking.databinding.ActivityPinRecoveryBinding
import java.util.*

class PinRecoveryActivity : AppCompatActivity()
{

    private lateinit var binding : ActivityPinRecoveryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_recovery)

        binding = ActivityPinRecoveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        confirmRecoveryCode(binding.etRecoveryCode.text.toString())
        getNewPIN(binding.etNewPin.text.toString(), binding.etNewPinConfirmed.text.toString())
    }

    fun confirmRecoveryCode(recoveryCode: String){
        if(currentUser?.kod_za_oporavak == recoveryCode) {
            Toast.makeText(this, "Unesite novi PIN", Toast.LENGTH_SHORT).show()
            binding.tvOporavakPina.isVisible = false
            binding.etRecoveryCode.isVisible = false
            binding.btnSubmit.isVisible = false
        }
        else{
            Toast.makeText(this, "Kod za oporavak nije točan", Toast.LENGTH_SHORT).show()
        }
    }

    fun getNewPIN(newPin: String, newPinConfirmed: String) {
        if(newPin == newPinConfirmed) {
            Toast.makeText(this, "Uspješno ste promijenili PIN!", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this, "PINovi se ne podudaraju!", Toast.LENGTH_SHORT).show()
        }
    }

    fun isNumeric(str: String): Boolean {
        return str.matches("\\d+".toRegex())
    }



}