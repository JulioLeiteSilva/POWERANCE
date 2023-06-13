package br.com.powerance.denterprofessional

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.media.Image
import android.os.Build
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.graphics.Color
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider

import androidx.core.content.ContextCompat
import br.com.powerance.denterprofessional.databinding.ActivityCameraBinding
import br.com.powerance.denterprofessional.databinding.ActivityCameraPreviewBinding

import com.google.common.util.concurrent.ListenableFuture
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import java.io.File
import java.lang.Exception
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraPreviewActivity : AppCompatActivity() {
    private var _binding: ActivityCameraPreviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var functions: FirebaseFunctions
    private lateinit var auth: FirebaseAuth
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraSelector: CameraSelector
    private var imageCapture: ImageCapture? = null
    private lateinit var imgCaptureExecutor: ExecutorService



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCameraPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        imgCaptureExecutor = Executors.newSingleThreadExecutor()
        binding.btnTirarFoto.setOnClickListener{
            takePhoto()
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                blink()
//            }
//
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
            this?.finish()

        }

    }
    private fun startCamera(){
        cameraProviderFuture.addListener({
            imageCapture = ImageCapture.Builder().build()

            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also{
                it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
            }
            try{
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this,cameraSelector,preview, imageCapture)
            }catch (e:Exception){
                Log.e("CameraPreview", "Falha ao abrir a camera")
            }
        }, ContextCompat.getMainExecutor(this))
    }
    private fun takePhoto(){
        imageCapture?.let {
            val fileName = "FOTO_JPEG_${System.currentTimeMillis()}"
            val file = File(externalMediaDirs[0], fileName)

            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()
            it.takePicture(
                outputFileOptions,
                imgCaptureExecutor,
                object: ImageCapture.OnImageSavedCallback{
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val imagemSalva = File(outputFileResults.savedUri?.path)
                        val imagemSalvaPath = imagemSalva.absolutePath

                        val intent = Intent(this@CameraPreviewActivity, CameraActivity::class.java)
                        intent.putExtra("imagemSalvaPath",imagemSalvaPath )
                        startActivity(intent)
                        finish()
                        Log.i("CameraPreview", " A imagem foi salva no diretorio ${file.toURI()}")
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Snackbar.make(binding.root,"Erro ao salvar a imagem", Toast.LENGTH_LONG).show()
                        Log.e("CameraPreview", "Exceção ao gravar arquivo da fota: $exception")
                    }
                })
        }
    }
    private fun releaseCamera() {
        try {
            cameraProviderFuture.get()?.unbindAll()
        } catch (e: Exception) {
            Log.e("CameraPreview", "Falha ao liberar a câmera: $e")
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        releaseCamera()
    }
    override fun onStart() {
        super.onStart()
        startCamera()
    }
//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun blink(){
//        binding.root.postDelayed({
//            binding.root.foreground = ColorDrawable(Color.WHITE)
//            binding.root.postDelayed({
//                binding.root.foreground = null
//            },50)
//        },100)
//    }
}