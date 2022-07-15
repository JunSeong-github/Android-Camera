package com.example.myapplication

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MediaUtil.Companion.getMediaUri
import com.example.myapplication.MediaUtil.Companion.scanMediaToBitmap
import com.example.myapplication.MediaUtil.Companion.saveToGallery
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val choosePhoto = registerForActivityResult(ActivityResultContracts.GetContent()) {
        binding.imageViewImageFromGallery.setImageURI(it)
    }

    private var photoUri: Uri? = null
    private val flexibleTakePicture = FlexibleTakePicture()
    private val takePhoto = registerForActivityResult(flexibleTakePicture) { isSuccess ->
        if (isSuccess) {
            photoUri?.let { uri ->
                scanMediaToBitmap(uri) {
                    runOnUiThread {
                        binding.imageViewImageFromGallery.setImageBitmap(it)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.checkPermission()

        with(binding) {
            buttonSaveBitmap.setOnClickListener {
                val bitmapDrawable = imageViewSample.drawable as? BitmapDrawable
                val bitmap = bitmapDrawable?.bitmap
                bitmap?.saveToGallery(this@MainActivity)
            }
            buttonTakePhotoSaveToGallery.setOnClickListener {
                val intent = flexibleTakePicture.newIntent()
                photoUri = this@MainActivity.getMediaUri(intent)

                takePhoto.launch(photoUri)
            }
            buttonGetImageFromGallery.setOnClickListener {
                choosePhoto.launch("image/Pictures/*")
            }
        }
    }

}