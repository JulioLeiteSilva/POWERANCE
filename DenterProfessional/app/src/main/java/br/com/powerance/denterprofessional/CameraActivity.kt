package br.com.powerance.denterprofessional

import android.content.Intent

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import br.com.powerance.denterprofessional.databinding.ActivityCameraBinding
import br.com.powerance.denterprofessional.datastore.CustomResponse
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.gson.GsonBuilder
import java.io.File

class CameraActivity : AppCompatActivity() {
    private var _binding: ActivityCameraBinding? = null
    private val binding get() = _binding!!
    private lateinit var storage: FirebaseStorage
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()
    private lateinit var functions: FirebaseFunctions
    private lateinit var auth: FirebaseAuth
    private lateinit var imgViewFoto: ImageView
    private lateinit var imagemSalvaPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storage = Firebase.storage
        binding.btnTirarFoto.setOnClickListener {
            it.isEnabled = false
            binding.btnVoltar.isEnabled = false
            cameraProviderResult.launch(android.Manifest.permission.CAMERA)
        }
        binding.btnVoltar.setOnClickListener{
            val intentVoltar = Intent(this, MenuActivity::class.java)
            startActivity(intentVoltar)
            this?.finish()
        }
        val imagemSalvaPath = intent.getStringExtra("imagemSalvaPath")
        if (imagemSalvaPath != null) {
            val imageFile = File(imagemSalvaPath)
            if (imageFile.exists()) {
                exibirImagemSalva()
            }
        }
        binding.btnConfirmar.setOnClickListener {
            uploadImageToFirebaseStorage()
            binding.imgViewFoto.visibility = View.GONE
            binding.btnConfirmar.isEnabled = false
            binding.btnTirarFoto.isEnabled = false
            binding.signUPProgressBar.visibility = View.VISIBLE
            Handler().postDelayed({
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
                this?.finish()
            },3500)

        }
    }
    private var cameraProviderResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                abrirPreview()
            } else {
                Snackbar.make(
                    binding.root,
                    "Você tem que aceitar as permissões",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

    private fun resizeBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
    private fun exibirImagemSalva() {
        binding.btnVoltar.visibility = View.GONE
        val imagemSalvaPath = intent.getStringExtra("imagemSalvaPath")
        if (imagemSalvaPath != null) {
            val imageFile = File(imagemSalvaPath)
            if(imageFile.exists()){
                val bitmap = BitmapFactory.decodeFile(imagemSalvaPath)
                val resized = resizeBitmap(bitmap, 150,150)
                binding.imgViewFoto.visibility = View.VISIBLE
                binding.imgViewFoto.setImageBitmap(resized)
                binding.btnConfirmar.visibility = View.VISIBLE
            }
        }
    }
    private fun abrirPreview() {
        val intentCameraPreview = Intent(this, CameraPreviewActivity::class.java)
        startActivity(intentCameraPreview)
    }
    private fun uploadImageToFirebaseStorage() {
        val imagemSalvaPath = intent.getStringExtra("imagemSalvaPath")
        val imageFile = File(imagemSalvaPath)
        val fileRef = storage.reference.child("/dentista/${imageFile.name}")
        val uploadTask = fileRef.putFile(Uri.fromFile(imageFile))
        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                fileRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val imageUrl = downloadUri.toString()
                }
                Log.i("CameraActivity", "A imagem foi enviada para o Firebase Storage com sucesso")

                auth = Firebase.auth
                val user = auth.currentUser
                val uid = user!!.uid
                val data = hashMapOf(
                    "uid" to uid,
                    "fotoPerfil" to imageFile.name
                )
                updateUserProfile(data)
                    .addOnCompleteListener {
                    }
            }else {
                Toast.makeText(this, "Erro ao enviar a imagem", Toast.LENGTH_SHORT).show()
                Log.e(
                    "CameraActivity",
                    "Exceção ao enviar a imagem para o Firebase Storage: ${task.exception}"
                )
            }
        }
    }


    private fun updateUserProfile(data: HashMap<String,String>?): Task<CustomResponse> {
        functions = Firebase.functions("southamerica-east1")

        return functions
            .getHttpsCallable("updateUserProfile")
            .call(data)
            .continueWith {task ->
                val result = gson.fromJson((task.result?.data as String), CustomResponse::class.java)
                result
            }
    }


}














