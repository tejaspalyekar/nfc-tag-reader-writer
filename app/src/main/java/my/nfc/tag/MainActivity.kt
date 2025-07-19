package my.nfc.tag

import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var nfcAdapter: NfcAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
    }

    private fun createNfcIntentFilter(): Array<IntentFilter>{
val intentFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        try {
            intentFilter.addDataType("*/*")
        }catch (e: IntentFilter.MalformedMimeTypeException){
            throw RuntimeException("Failed to add MIME type.", e)
        }
        return arrayOf(intentFilter) ;
    }


}