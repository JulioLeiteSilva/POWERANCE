package br.com.powerance.denterprofessional

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.powerance.denterprofessional.databinding.ActivityEmergencyDetailBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.IOException


class EmergencyDetailActivity : AppCompatActivity() {


    private lateinit var binding: ActivityEmergencyDetailBinding

    private var emergency: Emergency? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmergencyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emergency = intent.getParcelableExtra("emergencia")

        binding.tvEmergencyName.text = getString(R.string.Nome_EmergencyDetail, emergency?.nome)
        binding.tvEmergencyPhone.text = getString(R.string.Telefone_EmergencyDetail, emergency?.telefone)

        var imageID = emergency?.fotos
        val storageRef = FirebaseStorage.getInstance().reference.child("emergencies/$imageID")
        Toast.makeText(this,"$storageRef",Toast.LENGTH_LONG).show()

        val localfile = File.createTempFile("tempImage","png")
        storageRef.getFile(localfile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.ivEmergency2.setImageBitmap(bitmap)
            binding.ivEmergency1.setImageBitmap(bitmap)
            binding.ivEmergency3.setImageBitmap(bitmap)

        }.addOnFailureListener{

            Toast.makeText(this,"Não Deu pra pegar a imagem",Toast.LENGTH_SHORT).show()
        }




    }
}