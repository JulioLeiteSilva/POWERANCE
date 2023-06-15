package br.com.powerance.denterprofessional

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import br.com.powerance.denterprofessional.datastore.Emergency

class EmergencyActivity : AppCompatActivity() {

    public var emergency: Emergency? = null
    lateinit var notificationMessage: String
    lateinit var userAddress: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency)

        emergency = intent.getParcelableExtra("emergencia")
        userAddress = intent.getStringExtra("userAddress").toString()
        notificationMessage= intent.getStringExtra("NotificationMessage").toString()

        if(notificationMessage=="A emergência está em andamento"){
            val navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu)
            navController.navigate(R.id.emergencyProcessFragment)
        }

    }


}