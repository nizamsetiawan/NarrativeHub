package com.intermediate.storyapp.view.story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.intermediate.storyapp.R
import com.intermediate.storyapp.data.pref.UserPreferences
import com.intermediate.storyapp.databinding.ActivityStoryAddBinding
import com.intermediate.storyapp.utils.getImageUri
import com.intermediate.storyapp.utils.reduceFileImage
import com.intermediate.storyapp.utils.uriToFile
import com.intermediate.storyapp.view.dashboard.DashboardActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.FileNotFoundException

class StoryAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryAddBinding
    private var currentImageUri: Uri? = null
    private val storyViewModel: StoryViewModel by viewModel()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Kamera diberikan akses", Toast.LENGTH_LONG).show()
                openCamera()
            } else {
                Toast.makeText(this, "Kamera tidak diberikan akses", Toast.LENGTH_LONG).show()
            }
        }

    // Callback untuk menangani hasil dari aksi pengambilan gambar
    private val takePictureCallback =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                displayImage()
            } else {
                Log.d("Camera", "Gagal mengambil gambar")
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener { openGallery() }
        binding.cameraButton.setOnClickListener { checkCameraPermissionAndOpen() }
        binding.uploadButton.setOnClickListener { uploadImage() }

        if (!allPermissionsGranted()) {
            requestCameraPermission()
        }
        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.addStoryToolbar)
        binding.addStoryToolbar.apply {
            setNavigationIcon(R.drawable.baseline_arrow_back_24)
            setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun requestCameraPermission() {
        requestPermissionLauncher.launch(REQUIRED_PERMISSION)
    }

    private fun checkCameraPermissionAndOpen() {
        if (allPermissionsGranted()) {
            openCamera()
        } else {
            requestCameraPermission()
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            displayImage()
        } else {
            Log.d("Photo Picker", " Tidak ada gambar yang dipilih")
        }
    }

    private fun openCamera() {
        currentImageUri = getImageUri(this)
        currentImageUri?.let {
            takePictureCallback.launch(it)
        } ?: run {
            Toast.makeText(this, "Gagal mengambil gambar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayImage() {
        currentImageUri?.let {
            Log.d("Image URI", "displayImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun uploadImage() {
        if (currentImageUri == null) {
            Toast.makeText(this, "Pilih atau ambil gambar terlebih dahulu", Toast.LENGTH_SHORT)
                .show()
            return
        }

        try {
            val imageFile = uriToFile(currentImageUri!!, this).reduceFileImage()

            val description = binding.descEditText.text.toString()

            if (description.isBlank()) {
                Toast.makeText(this, "Deskripsi tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }

            val token = UserPreferences.userToken
            storyViewModel.addNewStory(token, imageFile, description,
                onSuccess = { _ ->
                    Toast.makeText(this, "Sukses upload story", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, DashboardActivity::class.java))
                },
                onError = {
                    Toast.makeText(this, "Gagal upload story", Toast.LENGTH_SHORT).show()
                }
            )
        } catch (e: FileNotFoundException) {
            Toast.makeText(this, "Pilih atau ambil gambar terlebih dahulu", Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        } catch (e: Exception) {
            Toast.makeText(this, "Terjadi kesalahan saat mengunggah gambar", Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }


    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
