package br.com.powerance.denterprofessional

import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import br.com.powerance.denterprofessional.databinding.ActivityUserDetailBinding
import br.com.powerance.denterprofessional.datastore.CustomResponse
import br.com.powerance.denterprofessional.datastore.Payload
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder



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
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            this?.finish()
        }else if(name.isEmpty()){
            binding.tiName.error="Este campo é obrigatório! Preencha o nome!"
        }else if(phone.isEmpty()){
            binding.tiFone.error="Este campo é obrigatório! Preencha o telefone!"
        }else if(phone.length!=13){
            binding.tiFone.error="Insira um número de telefone válido"
        }else if(resume.isEmpty()){
            binding.tiAddress1.error="Este campo é obrigatório! Preencha o currículo!"
        }else if(cep.isEmpty()){
            binding.tiCep.error="Este campo é obrigatório! Preencha o CEP!"
        }else if(cep.length!=9){
            binding.tiCep.error="Insira um número de CEP válido"
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
        binding.tiFone.addTextChangedListener(PhoneNumberFormattingTextWatcher("BR"))
        binding.tiCep.addTextChangedListener(textWatcher)
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
        binding.btnVoltar.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            this?.finish()
        }

    }
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun afterTextChanged(s: Editable) {
            if (!s.toString().contains("-") && s.length > 5) {
                s.insert(5, "-");
            }
        }
    }
}