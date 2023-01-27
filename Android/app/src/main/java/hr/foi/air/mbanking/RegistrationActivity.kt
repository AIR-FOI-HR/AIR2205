package hr.foi.air.mbanking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import hr.foi.air.mbanking.api.UserRequest
import hr.foi.air.mbanking.databinding.ActivityRegistrationBinding
import hr.foi.air.mbanking.entities.User
import kotlinx.coroutines.*

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val userRequest = UserRequest()

        if (!isNumeric(binding.etPIN.text.toString())) {
            Toast.makeText(
                this@RegistrationActivity,
                "PIN se mora sastojati od brojeva!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (!isCorrectSize(binding.etPIN.text.toString())) {
            Toast.makeText(
                this@RegistrationActivity,
                "PIN mora sadržavati 4 znamenke!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (!isEqualToConfirm(binding.etPIN.text.toString(), binding.etRepeatPIN.text.toString())) {
            Toast.makeText(
                this@RegistrationActivity,
                "PIN i potvrda PIN-a moraju se podudarati!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val recoveryCode = userRequest.createUser(
                binding.etName.text.toString(),
                binding.etLastName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPIN.text.toString()
            )

            withContext(Dispatchers.Main) {
                if (recoveryCode != null) {
                    val intent = Intent(this@RegistrationActivity, PostRegistrationActivity::class.java)
                    intent.putExtra("recoveryCode", recoveryCode)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Pogrešni podaci!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun isNumeric(pin: String): Boolean {
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return pin.matches(regex)
    }

    private fun isEqualToConfirm(pin: String, confirmPin: String): Boolean {
        return pin.equals(confirmPin)
    }

    private fun isCorrectSize(pin: String): Boolean {
        return pin.length <= 4
    }
}