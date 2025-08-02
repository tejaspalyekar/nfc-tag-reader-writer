package my.nfc.tag
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import com.my.nfc.demo.R

class MainActivity : AppCompatActivity() {
    //private var nfcAdapter: NfcAdapter? = null

    private lateinit var readBtn : Button
    private lateinit var writeBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        readBtn = findViewById(R.id.read_btn)
        writeBtn = findViewById(R.id.write_btn)
        buttonActions()

    }

   private fun buttonActions(){
       readBtn.setOnClickListener({

         val intent = Intent(this,ReadActivity::class.java)
           startActivity(intent)
       });

       writeBtn.setOnClickListener({
val intent = Intent(this,WriteActivity::class.java)
           startActivity(intent)
       })

   }
}