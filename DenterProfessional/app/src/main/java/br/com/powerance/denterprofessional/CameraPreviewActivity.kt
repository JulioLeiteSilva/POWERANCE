package br.com.powerance.denterprofessional

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.media.Image
import android.os.Build
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.graphics.Color
import android.graphics.Matrix
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider

import androidx.core.content.ContextCompat
import br.com.powerance.denterprofessional.databinding.ActivityCameraBinding
import br.com.powerance.denterprofessional.databinding.ActivityCameraPreviewBinding

import com.google.common.util.concurrent.ListenableFuture
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
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
        val imageCapture = imageCapture ?: return
        val fileName = "${System.currentTimeMillis()}"
        val outputDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        Log.i("CameraPreview", "O diretório de armazenamento é: ${outputDirectory?.absolutePath}")
        val photoFile = File(outputDirectory, fileName)

        imageCapture.takePicture(
                imgCaptureExecutor,
                object: ImageCapture.OnImageCapturedCallback(){
                    override fun onCaptureSuccess(image:ImageProxy) {

                        val buffer = image.planes[0].buffer
                        val imageBytes = ByteArray(buffer.remaining())
                        buffer.get(imageBytes)
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        val outputStream = FileOutputStream(photoFile)
                        val rotationDegrees = image.imageInfo.rotationDegrees
                        val matrix = Matrix()
                        matrix.postRotate(rotationDegrees.toFloat())
                        val rotatedBitmap =Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        outputStream.close()
                        Log.i("CameraPreview","photo")
                        val intent = Intent(this@CameraPreviewActivity, CameraActivity::class.java)
                        intent.putExtra("imagemSalvaPath",photoFile.absolutePath )
                        startActivity(intent)
                        finish()
                        Log.i("CameraPreview", " A imagem foi salva no diretorio ${photoFile.toURI()}")
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Snackbar.make(binding.root,"Erro ao salvar a imagem", Toast.LENGTH_LONG).show()
                        Log.e("CameraPreview", "Exceção ao gravar arquivo da foto: $exception")
                    }
                })

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