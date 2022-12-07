package hr.foi.air.mbanking


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.foi.air.mbanking.databinding.LayoutMenuBinding

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: LayoutMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutMenuBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        onBackArrowPressed()
    }


    fun onBackArrowPressed(){
        binding.buttonBack.setOnClickListener(){
            finish()
        }
    }
}