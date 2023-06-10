package com.example.storyappsubmission.ui.addstory

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.storyappsubmission.R
import com.example.storyappsubmission.databinding.ActivityAddStoryBinding
import com.example.storyappsubmission.helper.*
import com.example.storyappsubmission.ui.liststory.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddStoryBinding
    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    private val viewModel by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(application)
    }
    @VisibleForTesting
    private var getFile: File? = null
    private var lat: Double? = null
    private var lon: Double? = null

    override fun onRequestPermissionsResult(
        requestCode : Int ,
        permissions : Array<out String> ,
        grantResults : IntArray
    ) {
        super.onRequestPermissionsResult(requestCode , permissions , grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this ,
                    R.string.message_permission ,
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        binding.tvAdd.setOnClickListener {
            chooseAction()
        }

        binding.btUpload.setOnClickListener {
            uploadImage(lat,lon)
        }

        binding.switchButton.setOnCheckedChangeListener { _ , isChecked ->
            if (isChecked) {
                getUserLocation()
            } else {
                binding.switchButton.isChecked = false
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getUserLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getUserLocation()
                }
                else -> {
                    binding.switchButton.isChecked = false
                    redirectToLocationSettings()
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getUserLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {location: Location? ->
                if (location != null) {
                    lat = location.latitude
                    lon = location.longitude
                } else {
                    Toast.makeText(
                        this@AddStoryActivity ,
                        getString(R.string.toast_error_userlocation) ,
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.switchButton.isChecked = false
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun redirectToLocationSettings() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.required_permission))
        builder.setMessage(getString(R.string.required_permission_message))
        builder.setIcon(R.mipmap.ic_launcher_round)
        builder.setPositiveButton(getString(R.string.alert_dialog_yes)) { _ , _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        builder.setNegativeButton(getString(R.string.alert_dialog_cancel)) { dialog , _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun chooseAction() {
        val options = arrayOf<CharSequence>(getString(R.string.alert_dialog_optionkamera) , getString(
                    R.string.alert_dialog_opengaleri))
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.alert_dialog_selectoption))
        builder.setItems(options) { _ , which ->
            when(which){
                0 -> {
                    takePhoto()
                }
                1 -> {
                    choosePhoto()
                }
            }
        }
        builder.show()
    }

    private fun choosePhoto() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddStoryActivity)
                getFile = myFile
                Glide.with(this@AddStoryActivity)
                    .load(uri)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                    .into(binding.ivAddStory)
            }
        }
    }

    private fun takePhoto() {
        val intent = Intent(this@AddStoryActivity, CameraActivity::class.java)
        launcherCameraX.launch(intent)
    }

    private val launcherCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result->
        if (result.resultCode == CAMERA_X_RESULT) {
            val myFile = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                result.data?.getSerializableExtra("picture")
            } as? File

            val isBackCamera = result.data?.getBooleanExtra("isBackCamera", true) as Boolean

            myFile?.let {file ->
                rotateFile(file, isBackCamera)
                getFile = file
                Glide.with(this@AddStoryActivity)
                    .load(BitmapFactory.decodeFile(file.path))
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                    .into(binding.ivAddStory)
            }
        }
    }

    private fun uploadImage(lat : Double? = null , lon : Double? = null) {
        if (getFile != null && binding.etDescription.text.isNotEmpty()) {
            val file = reduceFileImage(getFile as File)
            val descriptionText = binding.etDescription.text

            val description = descriptionText.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            viewModel.postStory(imageMultipart, description, lat , lon).observe(this) {result->
                if (result != null){
                    when(result) {
                        is com.example.storyappsubmission.data.Result.Loading -> {
                            showLoading(true)
                        }
                        is com.example.storyappsubmission.data.Result.Success -> {
                            showLoading(false)
                            Toast.makeText(this@AddStoryActivity , result.data.message , Toast.LENGTH_SHORT).show()
                            val i = Intent(this@AddStoryActivity, MainActivity::class.java)
                            startActivity(i)
                            finish()
                        }
                        is com.example.storyappsubmission.data.Result.Error -> {
                            showLoading(false)
                            Toast.makeText(this@AddStoryActivity , "Failed to Upload Story"  , Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        } else if (getFile == null) {
            Toast.makeText(this@AddStoryActivity , getString(R.string.toast_picture_empty) , Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@AddStoryActivity , R.string.message_form_alert , Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(loading : Boolean) {
        binding.progressBar3.isVisible = loading
        if (loading){
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE , WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    companion object{
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}