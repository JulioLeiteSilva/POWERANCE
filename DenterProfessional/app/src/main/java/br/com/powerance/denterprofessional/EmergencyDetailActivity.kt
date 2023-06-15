package br.com.powerance.denterprofessional

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.com.powerance.denterprofessional.databinding.ActivityEmergencyDetailBinding
import br.com.powerance.denterprofessional.datastore.CustomResponse
import br.com.powerance.denterprofessional.datastore.Emergency
import br.com.powerance.denterprofessional.datastore.Payload
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
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
    lateinit var dataProfile: Task<CustomResponse>
    private lateinit var functions: FirebaseFunctions
    private lateinit var auth: FirebaseAuth
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmergencyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emergency = intent.getParcelableExtra("emergencia")

        auth = Firebase.auth
        val user = auth.currentUser
        val uid = user!!.uid

        dataProfile = getUserProfile()
            .addOnCompleteListener(this) { res ->
                // conta criada com sucesso.
                if (res.result.status == "SUCCESS") {
                } else {
                }
            }

        binding.tvEmergencyName.text = getString(R.string.Nome_EmergencyDetail, emergency?.name)

        val photos = emergency?.photos

        if (photos != null && photos.isNotEmpty()) {
            val bitmapList = mutableListOf<Bitmap>()

            for (photo in photos) {
                val storageRef = FirebaseStorage.getInstance().reference.child("emergencies/$photo")
                val localFile = File.createTempFile("tempImage", "jpeg")

                storageRef.getFile(localFile).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                    bitmapList.add(bitmap)

                    // Verificar se todas as imagens foram carregadas
                    if (bitmapList.size == photos.size) {
                        // Utilizar as imagens carregadas
                        binding.ivEmergency2.setImageBitmap(bitmapList[1])
                        binding.ivEmergency1.setImageBitmap(bitmapList[0])
                        binding.ivEmergency3.setImageBitmap(bitmapList[2])
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Não foi possível obter a imagem", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Nenhuma foto disponível", Toast.LENGTH_SHORT).show()
        }

        binding.bAccept.setOnClickListener{
            var profile = gson.fromJson((dataProfile.result?.payload as String), Payload::class.java)
            updateEmergencyStatus("accepted",uid,profile.name,emergency?.uid)
                .addOnSuccessListener(this){ res ->
                    if(res.status == "SUCCESS") {
                        Toast.makeText(this, "SUCESSO", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,EmergencyActivity::class.java)
                        intent.putExtra("emergencia",emergency)
                        intent.putExtra("userAddress",profile.address1)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, "FALHOU", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.bReject.setOnClickListener{
            var profile = gson.fromJson((dataProfile.result?.payload as String), Payload::class.java)
            updateEmergencyStatus("rejected",uid,profile.name,emergency?.uid)
                .addOnSuccessListener(this){ res ->
                    if(res.status == "SUCCESS") {
                        //Toast.makeText(this, "SUCESSO", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,MenuActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, "FALHOU", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

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
        db.collection("emergency").document(emergency).update(update)
            .addOnSuccessListener {
                Toast.makeText(this,"Sucesso ao inserir no banco",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this,"Erro ao atualizar o status: ${exception.message}",Toast.LENGTH_SHORT).show()
            }

        return null
    }

    private fun updateEmergencyStatus(status: String,uid: String,name: String?,uidEmergency: String?): Task<CustomResponse> {
        functions = Firebase.functions("southamerica-east1")

        val data = hashMapOf(
            "name" to name,
            "uid" to uid,
            "status" to status,
            "uidEmergency" to uidEmergency
        )
        return functions
            .getHttpsCallable("updateEmergencyStatus")
            .call(data)
            .continueWith{task ->
                val result = gson.fromJson((task.result.data as String), CustomResponse::class.java)
                result
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

}
