package hr.foi.air.mbanking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.foi.air.mbanking.databinding.ActivityPostRegistrationBinding

class PostRegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_registration)

        binding = ActivityPostRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recoveryCode = intent.getStringExtra("recoveryCode")
        binding.tvRecoveryCodeMsg.setText(recoveryCode.toString())

        onLoginPressed()
    }

    private fun onLoginPressed(){
        binding.btnLogin.setOnClickListener(){
            val logInActivity = Intent(this, LogInActivity::class.java)
            startActivity(logInActivity)
        }
    }
}