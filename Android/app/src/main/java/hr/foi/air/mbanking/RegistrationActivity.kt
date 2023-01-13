package hr.foi.air.mbanking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hr.foi.air.mbanking.api.UserRequest
import hr.foi.air.mbanking.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val userRequest = UserRequest()
            val user = userRequest.getUser(1)

            Toast.makeText(this, user, Toast.LENGTH_SHORT).show()
        }
    }
}