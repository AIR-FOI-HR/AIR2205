package hr.foi.air.mbanking

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import hr.foi.air.mbanking.api.UserRequest
import hr.foi.air.mbanking.databinding.ActivityLogInBinding
import hr.foi.air.mbanking.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LogInActivity : AppCompatActivity()
{
    private val userRequest = UserRequest()

    private lateinit var binding: ActivityLogInBinding

    companion object{
        var currentUser: User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onLogInPressed()
        onPinRecoveryPressed()
        onRegisterPressed()
    }

    private fun onLogInPressed() {
        binding.btnLogIn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                val user = userRequest.logInUser(binding.etEmail.text.toString(), binding.etPIN.text.toString())
                    if(user != null) {
                        currentUser = user
                        saveActiveUser(user)
                        val mainView = Intent(this@LogInActivity, MainActivity::class.java)
                        startActivity(mainView)
                    }
                    else {
                        Toast.makeText(this@LogInActivity, "Netočno ime ili lozinka", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    private fun onPinRecoveryPressed() {
        binding.tvPinRecovery.setOnClickListener {
            val pinRecovery = Intent(this, PinRecoveryActivity::class.java)
            startActivity(pinRecovery)
        }
    }

    private fun onRegisterPressed(){
        binding.btnRegistration.setOnClickListener(){
            val registrationActivity = Intent(this, RegistrationActivity::class.java)
            startActivity(registrationActivity)
        }
    }

    private fun saveActiveUser(user: User){
        val sharedPreferences = getSharedPreferences("ACTIVE_USER", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("USER_ID", user.korisnik_id)

        editor.commit()
    }

}



