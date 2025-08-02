package my.nfc.tag

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NfcF
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.my.nfc.demo.R

class ReadActivity : AppCompatActivity() {
    private lateinit var dataTxt : TextView
    private val nfcAdapter: NfcAdapter? by lazy{
        NfcAdapter.getDefaultAdapter(this)
    }
    private var pendingIntent: PendingIntent? = null
    private var intentFiltersArray: Array<IntentFilter>? = null
    private val techListsArray = arrayOf(arrayOf(NfcF::class.java.name))


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.read_activity)
        dataTxt = findViewById(R.id.read_data_text)

        checkNfcAvailability()
    }

    override fun onResume() {
        super.onResume()
        // Enable NFC foreground dispatch to listen for tags
        nfcAdapter?.enableForegroundDispatch(
            this,
            pendingIntent,
            intentFiltersArray,
            techListsArray
        )
    }

    override fun onPause() {
        if(this.isFinishing){
            nfcAdapter?.disableForegroundDispatch(this)
        }
        super.onPause()
    }

    @SuppressLint("SetTextI18n")
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action || NfcAdapter.ACTION_TECH_DISCOVERED == intent.action ||  NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) {
            dataTxt.text = "NFC Tag Detected..Loading data...";
            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return
            val ndef = Ndef.get(tag) ?: return

            try {
                ndef.connect()
                val ndefMessage = ndef.cachedNdefMessage
                val records = ndefMessage.records

                if (records.isNotEmpty()) {

                    val messageRecord = records[0]
                    val message = String(messageRecord.payload).drop(3)
                    dataTxt.text = message
                }

                ndef.close()
            } catch (e: Exception) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
            }
        }else{
            dataTxt.text = "No NFC tag detected or data is empty"
            Toast.makeText(this, "No NFC tag detected", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun preparePendingIntent(){
        dataTxt.text = "Waiting for NFC tag..."
        // Prepare pending intent for NFC detection
        val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        try {
            ndef.addDataType("text/plain")
        } catch (e: IntentFilter.MalformedMimeTypeException) {
            throw RuntimeException("fail", e)
        }
        intentFiltersArray = arrayOf(ndef)
    }

    @SuppressLint("SetTextI18n")
    private fun checkNfcAvailability(){
        if (nfcAdapter == null) {
            dataTxt.text = "NFC not supported"
            Toast.makeText(this, "NFC not supported", Toast.LENGTH_SHORT).show()
        } else if (!nfcAdapter!!.isEnabled) {
            dataTxt.text = "Please turn on NFC"
            Toast.makeText(this, "Please turn on NFC", Toast.LENGTH_SHORT).show()
        }else{
            preparePendingIntent()
        }
    }


}