package hr.foi.air.mbanking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import hr.foi.air.mbanking.api.UserRequest
import hr.foi.air.mbanking.databinding.ActivityPinRecoveryBinding
import hr.foi.air.mbanking.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PinRecoveryActivity : AppCompatActivity()
{
    private val userRequest = UserRequest()
    private var currentUser: User? = null

    private lateinit var binding : ActivityPinRecoveryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_recovery)

        binding = ActivityPinRecoveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvUnosNovogPINa.isVisible = false
        binding.etNewPIN.isVisible = false
        binding.etNewPINConfirm.isVisible = false
        binding.btnSubmit2.isVisible = false

        onConfirmRecoveryCodePressed()
        onGetNewPinPressed()
    }

    private fun confirmRecoveryCode(): Boolean {
        var check = false
        lifecycleScope.launch(Dispatchers.IO)
        {
            check = getRecoveryUser(binding.etEmail.text.toString(), binding.etRecoveryCode.text.toString())
            withContext(Dispatchers.Main) {
                if(check) {
                    Toast.makeText(this@PinRecoveryActivity, "Unesite novi PIN", Toast.LENGTH_SHORT).show()
                    binding.tvOporavakPina.isVisible = false
                    binding.etEmail.isVisible = false
                    binding.etRecoveryCode.isVisible = false
                    binding.btnSubmit.isVisible = false
                    binding.tvUnosNovogPINa.isVisible = true
                    binding.etNewPIN.isVisible = true
                    binding.etNewPINConfirm.isVisible = true
                    binding.btnSubmit2.isVisible = true
                    check = true
                }
                else{
                    Toast.makeText(this@PinRecoveryActivity, "Netočan email ili kod za oporavak", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return check
    }

    private fun getNewPIN() {
        if(!isNumeric(binding.etNewPIN.text.toString())) {
            Toast.makeText(this@PinRecoveryActivity, "PIN se mora sastojati od brojeva", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isCorrectSize(binding.etNewPIN.text.toString())) {
            Toast.makeText(this@PinRecoveryActivity, "PIN mora sadržavati 4 znamenke!", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isEqual(binding.etNewPIN.text.toString(), binding.etNewPINConfirm.text.toString())) {
            Toast.makeText(this@PinRecoveryActivity, "Novi PIN i potvrda novog PIN-a moraju se podudarati!", Toast.LENGTH_SHORT).show()
            return
        }

        if (isEqual(binding.etNewPIN.text.toString(), currentUser!!.pin)){
            Toast.makeText(this@PinRecoveryActivity, "Novi PIN ne smije biti isti kao trenutni PIN!", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch(Dispatchers.IO){
            val responseCode = userRequest.updateUser(currentUser!!.korisnik_id, currentUser!!.ime, currentUser!!.prezime, currentUser!!.email, currentUser!!.adresa,
                currentUser!!.mobitel, binding.etNewPIN.text.toString(), currentUser!!.kod_za_oporavak)
            withContext(Dispatchers.Main) {
                if(responseCode == 200) {
                    Toast.makeText(this@PinRecoveryActivity, "PIN uspješno promijenjen", Toast.LENGTH_SHORT).show()
                    val logIn= Intent(this@PinRecoveryActivity, LogInActivity::class.java)
                    startActivity(logIn)
                }
                else {
                    Toast.makeText(this@PinRecoveryActivity, "Nešto je pošlo po zlu, pokušajte kasnije!", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun onGetNewPinPressed() {
        binding.btnSubmit2.setOnClickListener {
            getNewPIN()
        }
    }

    private fun onConfirmRecoveryCodePressed(){
        binding.btnSubmit.setOnClickListener {
            confirmRecoveryCode()
        }
    }

    private fun getRecoveryUser(email: String, recoveryCode: String): Boolean{
        var check = false
        val userList =  userRequest.getAllUsers()
        for (user in userList) {
            if (user.email == email && user.kod_za_oporavak == recoveryCode) {
                currentUser = user
                check = true
            }
        }
        return check
    }

    private fun isNumeric(pin: String): Boolean {
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return pin.matches(regex)
    }

    private fun isEqual(pin: String, pinConfirm: String): Boolean {
        return pin == pinConfirm
    }

    private fun isCorrectSize(pin: String): Boolean {
        return pin.length == 4
    }

}