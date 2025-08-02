package my.nfc.tag

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import  com.my.nfc.demo.R

class WriteActivity: AppCompatActivity() {
     private lateinit var writeBtn: Button
    private lateinit var textField: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.write_activity)

        writeBtn = findViewById(R.id.set_data_write_button)
        textField = findViewById(R.id.write_txt_field)
    }

}