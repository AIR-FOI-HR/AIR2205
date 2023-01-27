package hr.foi.air.mbanking

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
import kotlinx.coroutines.withContext

class LogInActivity : AppCompatActivity()
{
    val userRequest = UserRequest()

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

    }

    fun onLogInPressed() {
        binding.btnLogIn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                var user = userRequest.logInUser(binding.etEmail.text.toString(), binding.etPIN.text.toString())
                withContext(Dispatchers.Main){
                    if(user != null) {
                        currentUser = user
                        val MainView = Intent(this@LogInActivity, MainActivity::class.java)
                        startActivity(MainView)
                    }
                    else {
                        Toast.makeText(this@LogInActivity, "Netočno ime ili lozinka", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun onPinRecoveryPressed()
    {
        binding.tvPinRecovery.setOnClickListener {
            val pinRecovery = Intent(this, PinRecoveryActivity::class.java)
            startActivity(pinRecovery)
        }
    }


}



