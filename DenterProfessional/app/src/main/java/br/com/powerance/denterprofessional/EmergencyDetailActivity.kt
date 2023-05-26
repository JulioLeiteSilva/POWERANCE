package br.com.powerance.denterprofessional

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.com.powerance.denterprofessional.databinding.ActivityEmergencyDetailBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.gson.GsonBuilder
import java.io.File
import java.io.IOException


class EmergencyDetailActivity : AppCompatActivity() {


    private lateinit var binding: ActivityEmergencyDetailBinding

    private var emergency: Emergency? = null

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmergencyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val user = auth.currentUser
        val uid = user!!.uid


        emergency = intent.getParcelableExtra("emergencia")

        binding.tvEmergencyName.text = getString(R.string.Nome_EmergencyDetail, emergency?.nome)
        binding.tvEmergencyPhone.text =
            getString(R.string.Telefone_EmergencyDetail, emergency?.telefone)

        var imageID = emergency?.fotos
        val storageRef = FirebaseStorage.getInstance().reference.child("emergencies/$imageID")
//        Toast.makeText(this, "$storageRef", Toast.LENGTH_LONG).show()

        val localfile = File.createTempFile("tempImage", "png")
        storageRef.getFile(localfile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.ivEmergency2.setImageBitmap(bitmap)
            binding.ivEmergency1.setImageBitmap(bitmap)
            binding.ivEmergency3.setImageBitmap(bitmap)

        }.addOnFailureListener {

            Toast.makeText(this, "Não Deu pra pegar a imagem", Toast.LENGTH_SHORT).show()
        }

        binding.bAccept.setOnClickListener{
            insertCollection(user.uid,"aceita",emergency!!.docID)
            startActivity(Intent(this,EmergencyActivity::class.java))
        }

        binding.bReject.setOnClickListener{
            insertCollection(user.uid,"rejeitada",emergency!!.docID)
        }

    }

    fun insertCollection(uid:String, status: String,emergency: String): View.OnClickListener? {
        val db = FirebaseFirestore.getInstance()
        val update = hashMapOf<String,Any?>(
            "status" to status
        )
        val accept: MutableMap<String?, Any> = HashMap()
        accept["dentist"] = uid
        accept["emergency"] = emergency
        accept["status"] = status
        db.collection("accept")
            .add(accept)
            .addOnSuccessListener {
            }
            .addOnFailureListener{exception->

            }
        db.collection("emergencyTeste").document(emergency).update(update)
            .addOnSuccessListener {
                Toast.makeText(this,"Sucesso ao inserir no banco",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this,"Erro ao atualizar o status: ${exception.message}",Toast.LENGTH_SHORT).show()
            }

        return null
    }


}
