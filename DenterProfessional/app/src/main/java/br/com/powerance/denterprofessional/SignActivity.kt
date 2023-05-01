package br.com.powerance.denterprofessional

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import br.com.powerance.denterprofessional.databinding.ActivitySignBinding
import br.com.powerance.denterprofessional.datastore.UserPreferencesRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging


class SignActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignBinding
    private lateinit var userPreferencesRepository: UserPreferencesRepository
    fun storeUserId(uid: String){
        userPreferencesRepository.uid = uid
    }

    fun getUserUid(): String{
        return userPreferencesRepository.uid;
    }

    private fun storeFcmToken(){
        Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // guardar esse token.
            userPreferencesRepository.fcmToken = task.result
        })
    }

    fun getFcmToken(): String{
        return userPreferencesRepository.fcmToken
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userPreferencesRepository = UserPreferencesRepository.getInstance(this)

        binding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // guardar o token FCM pois iremos precisar.
        storeFcmToken();
    }
}