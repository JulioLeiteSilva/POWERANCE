package br.com.powerance.denterprofessional

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity

import br.com.powerance.denterprofessional.databinding.ActivityUserDetailBinding
import br.com.powerance.denterprofessional.ui.theme.DenterProfessionalTheme
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import br.com.powerance.denterprofessional.MenuActivity
import br.com.powerance.denterprofessional.databinding.FragmentUserDetailProfileBinding

class UserDetailProfileActivity : AppCompatActivity() {

    private var _binding: ActivityUserDetailBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var functions: FirebaseFunctions
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()
    private val binding get() = _binding!!
    lateinit var dataProfile: Task<CustomResponse>





    private fun updateUserProfile(data: HashMap<String,String>?): Task<CustomResponse> {
        functions = Firebase.functions("southamerica-east1")

        return functions
            .getHttpsCallable("updateUserProfile")
            .call(data)
            .continueWith { task ->
                val result = gson.fromJson((task.result?.data as String), CustomResponse::class.java)
                result
            }
    }

    private fun verificacao(name: String,phone: String,resume: String, cep: String,address1: String,address2: String,address3: String, dataProfile: Task<CustomResponse>){
        var profile = gson.fromJson((dataProfile.result.payload as String), Payload::class.java)
        auth = Firebase.auth
        val user = auth.currentUser
        val uid = user!!.uid

        var data = hashMapOf(
            "uid" to uid
        )

        if(profile.name == name && profile.phone == phone && profile.miniResume == resume && profile.cep == cep && profile.address1 == address1 && profile.address2 ==address2 && profile.address3 == address3 ){
            Snackbar.make(binding.root,"sexu",
                Snackbar.LENGTH_LONG).show()
        }else{
            data = hashMapOf(
                "uid" to uid,
                "name" to name,
                "phone" to phone,
                "miniResume" to resume,
                "cep" to cep,
                "address1" to address1,
                "address2" to address2,
                "address3" to address3
            )
            updateUserProfile(data)
                .addOnCompleteListener(this) { res ->
                    // conta criada com sucesso.
                    if(res.result.status == "SUCCESS"){
                        val intent = Intent(this, MenuActivity::class.java)
                        startActivity(intent)
                        this?.finish()
                    }
                }
        }
    }

    private fun getUserProfile(): Task<CustomResponse>  {
        functions = Firebase.functions("southamerica-east1")

        auth = Firebase.auth
        val user = auth.currentUser
        val uid = user!!.uid

        val data = hashMapOf(
            "uid" to uid
        )

        return functions
            .getHttpsCallable("getUserProfileByUid")
            .call(data)
            .continueWith { task ->
                val result = gson.fromJson((task.result?.data as String), CustomResponse::class.java)
                result
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        _binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        dataProfile = getUserProfile()
            .addOnCompleteListener(this) { res ->
                if(res.result.status == "SUCCESS"){
                    var profile = gson.fromJson((res.result?.payload as String), Payload::class.java)
                    binding.tiName.setText(profile.name)
                    binding.tiFone.setText(profile.phone)
                    binding.tiResume.setText(profile.miniResume)
                    binding.tiCep.setText(profile.cep)
                    binding.tiAddress1.setText(profile.address1)
                    binding.tiAddress2.setText(profile.address2.toString())
                    binding.tiAddress3.setText(profile.address3.toString())
                }else{

                }
            }

        binding.btnNext.setOnClickListener{
            verificacao(binding.tiName.text.toString(),binding.tiFone.text.toString(),binding.tiResume.text.toString(),binding.tiCep.text.toString(),binding.tiAddress1.text.toString(),binding.tiAddress2.text.toString(),binding.tiAddress3.text.toString(), dataProfile)
        }

    }
}