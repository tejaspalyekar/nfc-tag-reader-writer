package my.nfc.tag

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NfcF
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import  com.my.nfc.demo.R

class WriteActivity: AppCompatActivity() {
     private lateinit var writeBtn: Button
     private lateinit var textField: EditText
     private var intentFiltersArray: Array<IntentFilter>? = null
     private val techListsArray = arrayOf(arrayOf(NfcF::class.java.name))
     private val nfcAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(this)
     }
    private var pendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.write_activity)

        writeBtn = findViewById(R.id.set_data_write_button)
        textField = findViewById(R.id.write_txt_field)

        checkNfcAvailability()

        writeBtn.setOnClickListener({
            val message = textField.text.toString()
            if (message.isNotEmpty()) {
                Toast.makeText(this, "Tap NFC Tag to write data", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter text to write", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun checkNfcAvailability(){
        if (nfcAdapter == null) {

            Toast.makeText(this, "NFC not supported", Toast.LENGTH_SHORT).show()
        } else if (!nfcAdapter!!.isEnabled) {

            Toast.makeText(this, "Please turn on NFC", Toast.LENGTH_SHORT).show()
        }else{
            preparePendingIntent()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun preparePendingIntent(){
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

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray)
    }

    override fun onPause() {
        if(this.isFinishing){
            nfcAdapter?.disableForegroundDispatch(this)
        }
        super.onPause()
    }

    override fun onNewIntent(intent: Intent) {
        // handles new intent delivered to the activity.(NFC Intent)
        super.onNewIntent(intent)
        try {
            val message=textField.text.toString()
            if(message != "") {
                if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action
                    || NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action ||
                    NfcAdapter.ACTION_TAG_DISCOVERED == intent.action
                ) {
                    val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return
                    val ndef = Ndef.get(tag) ?: return

                    if (ndef.isWritable) {
                        val nfcMessage = NdefMessage(
                            arrayOf(
                                NdefRecord.createTextRecord("en", message)

                            )
                        )

                        ndef.connect()
                        ndef.writeNdefMessage(nfcMessage)
                        ndef.close()

                        Toast.makeText(applicationContext, "Successfully Written!", Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(applicationContext, "NFC Tag not detected!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Write on text box!", Toast.LENGTH_SHORT).show()
            }
        }
        catch (e:Exception) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }

    }

}