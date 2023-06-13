package br.com.powerance.denterprofessional

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.com.powerance.denterprofessional.databinding.ActivitySignBinding
import br.com.powerance.denterprofessional.datastore.User
import br.com.powerance.denterprofessional.datastore.UserPreferencesRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging


class SignActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignBinding
    public lateinit var userPreferencesRepository: UserPreferencesRepository
    public var user = User("email","name","phone","","","","","","",false,"","","")
    fun storeUserId(uid: String){
        userPreferencesRepository.uid = uid
        user.uid = uid
    }

    fun getUserUid(): String{
        return userPreferencesRepository.uid;
    }

    private fun storeFcmToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
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

        storeFcmToken()
        user.fcmToken = userPreferencesRepository.fcmToken
    }
}