package hr.foi.air.mbanking


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import hr.foi.air.mbanking.databinding.LayoutMenuBinding

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: LayoutMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutMenuBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        onBackArrowPressed()
        onLogout()
    }

    fun onBackArrowPressed(){
        binding.buttonBack.setOnClickListener(){
            finish()
        }
    }

    fun onLogout() {
        binding.buttonOdjava.setOnClickListener {
            val registerView = Intent(this, RegistrationActivity::class.java)

            startActivity(registerView)
        }
    }
}