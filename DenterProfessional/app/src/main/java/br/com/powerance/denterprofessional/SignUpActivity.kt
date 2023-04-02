package br.com.powerance.denterprofessional

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.powerance.denterprofessional.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

lateinit var binding: ActivitySignUpBinding
private lateinit var auth:FirebaseAuth
private lateinit var database:FirebaseFirestore
private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.tvLinkLogin.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        auth=FirebaseAuth.getInstance()
        database=FirebaseFirestore.getInstance()

        binding.btnNext.setOnClickListener{
            val name = binding.signUpName.text.toString()
            val email = binding.signUpEmail.text.toString()
            val cEmail = binding.signUpCEmail.text.toString()
            val phone = binding.signUpFone.text.toString()
            val password = binding.signUpPassword.text.toString()
            val cPassword = binding.signUpCPassword.text.toString()
            val adress1 = ""
            val adress2 = ""
            val adress3 = ""
            val miniResume=""

            binding.signUPProgressBar.visibility= View.VISIBLE
            binding.signUpPasswordLayout.isPasswordVisibilityToggleEnabled = true
            binding.signUpCPasswordLayout.isPasswordVisibilityToggleEnabled = true

            if(name.isEmpty()||email.isEmpty()||cEmail.isEmpty()||phone.isEmpty()||password.isEmpty()||cPassword.isEmpty()){
                if(name.isEmpty()){
                    binding.signUpName.error="Insira seu nome"
                }
                if(email.isEmpty()){
                    binding.signUpEmail.error="Insira seu endereço email"
                }
                if(cEmail.isEmpty()){
                    binding.signUpCEmail.error="Repita seu endereço de email"
                }
                if(phone.isEmpty()){
                    binding.signUpFone.error="Insira seu número de telefone"
                }
                if(password.isEmpty()){
                    binding.signUpPasswordLayout.isPasswordVisibilityToggleEnabled = false
                    binding.signUpPassword.error="Insira sua senha"
                }
                if(cPassword.isEmpty()){
                    binding.signUpCPasswordLayout.isPasswordVisibilityToggleEnabled = false
                    binding.signUpCPassword.error="Repita sua senha"
                }
                Toast.makeText(this,"Insira detalhes validos", Toast.LENGTH_SHORT).show()
                binding.signUPProgressBar.visibility= View.GONE
            }else if(!email.matches(emailPattern.toRegex())){
                binding.signUPProgressBar.visibility= View.GONE
                binding.signUpEmail.error="Insira seu endereço email valido"
                Toast.makeText(this,"Insira um endereço de email valido", Toast.LENGTH_SHORT).show()
            }else if(phone.length!=10){
                binding.signUPProgressBar.visibility= View.GONE
                binding.signUpFone.error="Insira um número de telefone valido"
                Toast.makeText(this,"Insira um número de telefone valido", Toast.LENGTH_SHORT).show()
            }else if(password.length < 6){
                binding.signUPProgressBar.visibility= View.GONE
                binding.signUpPassword.error="A senha deve ter mais de 6 digitos"
                Toast.makeText(this,"A senha deve ter mais de 6 digitos", Toast.LENGTH_SHORT).show()
            }else if(password != cPassword){
                binding.signUPProgressBar.visibility= View.GONE
                binding.signUpCPassword.error="As senhas não batem"
                Toast.makeText(this,"As senhas não batem", Toast.LENGTH_SHORT).show()
            }else if(email != cEmail){
                binding.signUPProgressBar.visibility= View.GONE
                binding.signUpCEmail.error="Os Emails não batem"
                Toast.makeText(this,"Os Emails não batem", Toast.LENGTH_SHORT).show()
            }else{
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
                    when{
                        it.isSuccessful ->{
                            Toast.makeText(this,"Usuario cadastrado", Toast.LENGTH_SHORT).show()
                            createUser(email,name,phone,adress1,adress2,adress3,miniResume)
                            binding.signUPProgressBar.visibility= View.GONE
                        }
                        else ->{
                            binding.signUPProgressBar.visibility= View.GONE
                            Toast.makeText(this,"Algo deu errado, tente novamente", Toast.LENGTH_SHORT).show()
                        }
                    }
                    clearInputs()
                }
            }

        }

    }


    private fun insertUser(user:User){
        val userRef= database.collection("users")

        userRef.document().set(user).addOnCompleteListener(){
            when{
                it.isSuccessful ->{
                    Toast.makeText(this,"Usuario postado", Toast.LENGTH_SHORT).show()
                }
                else ->{
                    Toast.makeText(this,"Algo deu errado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun createUser(email:String,name:String,phone:String,adress1:String,adress2:String,adress3:String,miniResume:String) {
        val user = User(email,name,phone,adress1,adress2,adress3,miniResume)
        insertUser(user)
    }

    private fun clearInputs(){
        binding.signUpName.text?.clear()
        binding.signUpEmail.text?.clear()
        binding.signUpCEmail.text?.clear()
        binding.signUpFone.text?.clear()
        binding.signUpPassword.text?.clear()
        binding.signUpCPassword.text?.clear()
    }
}