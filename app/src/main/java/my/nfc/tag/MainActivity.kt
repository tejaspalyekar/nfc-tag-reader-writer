package my.nfc.tag
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.my.nfc.demo.R

class MainActivity : AppCompatActivity() {
    //private var nfcAdapter: NfcAdapter? = null

    private lateinit var readBtn : Button
    private lateinit var writeBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        readBtn = findViewById(R.id.read_btn)
        writeBtn = findViewById(R.id.write_btn)
        buttonActions()
//        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
//        if(nfcAdapter == null){
//            Toast.makeText(this,"No NFC Found",Toast.LENGTH_LONG).show()
//            finish()
//            return
//
//        }


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