package br.com.powerance.denterprofessional

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.powerance.denterprofessional.datastore.Emergency

class EmergencyActivity : AppCompatActivity() {

    public var emergency: Emergency? = null
    lateinit var userAddress: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency)

        emergency = intent.getParcelableExtra("emergencia")
        userAddress = intent.getStringExtra("userAddress").toString()

    }
}