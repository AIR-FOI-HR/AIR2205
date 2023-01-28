package foi.projekt.skeniraj_i_plati

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import foi.projekt.skeniraj_i_plati.databinding.LayoutQrscanBinding


class QRScanActivity: AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var binding: LayoutQrscanBinding
    private lateinit var glavniRacun: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutQrscanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        glavniRacun = intent.getStringExtra("GlavniRacun").toString()

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 123)
        }
        else{
            scanCode()
        }
    }

    private fun scanCode() {
        val scannerView: CodeScannerView = findViewById(R.id.scanner_view)
        codeScanner = CodeScanner(this, scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.isFlashEnabled = false
        codeScanner.isAutoFocusEnabled = true
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.decodeCallback = DecodeCallback {

            val intent1 = Intent(this, QRCodeActivity::class.java)
            intent1.putExtra("QRCodeData", it.text)
            intent1.putExtra("GlavniRacun", glavniRacun)
            startActivity(intent1)

            finish()

        }

        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread{
                Toast.makeText(this, "Greška pri skeniranju: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        scannerView.setOnClickListener{
            codeScanner.startPreview()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 123){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Dobiveno dopuštenje za korištenje kamere", Toast.LENGTH_SHORT).show()
                scanCode()
            }else{
                Toast.makeText(this, "Nije dobiveno dopuštenje za korištenje kamere", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}