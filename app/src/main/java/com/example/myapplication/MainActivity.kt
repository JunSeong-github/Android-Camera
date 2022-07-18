package com.example.myapplication

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MediaUtil.Companion.getMediaUri
//import com.example.myapplication.MediaUtil.Companion.scanMediaToBitmap
//import com.example.myapplication.MediaUtil.Companion.saveToGallery
import com.example.myapplication.databinding.ActivityMainBinding
import com.gun0912.tedpermission.provider.TedPermissionProvider.context

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val choosePhoto = registerForActivityResult(ActivityResultContracts.GetContent()) {
        binding.videoViewSample.setVideoURI(it)
    }

    private var videoUri: Uri? = null
    private val FlexibleTakeVideo = FlexibleTakeVideo()
    private val takePhoto = registerForActivityResult(FlexibleTakeVideo) { isSuccess ->
        if (isSuccess) {
            videoUri?.let { uri ->
                runOnUiThread {
                    binding.videoViewSample.setVideoURI(uri)
                    Toast.makeText(context, "Video saved to gallery", Toast.LENGTH_SHORT).show()
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
//            buttonSaveBitmap.setOnClickListener {
//                val bitmapDrawable = imageViewSample.drawable as? BitmapDrawable
//                val bitmap = bitmapDrawable?.bitmap
//                bitmap?.saveToGallery(this@MainActivity)
//            }

            buttonStart.setOnClickListener {
               videoViewSample.start()
            }

            buttonTakeVideoSave.setOnClickListener {
                val intent = FlexibleTakeVideo.newIntent()
                videoUri = this@MainActivity.getMediaUri(intent)

                takePhoto.launch(videoUri)
            }
            buttonGetVideo.setOnClickListener {
//                choosePhoto.launch("image/Pictures/*")
                choosePhoto.launch("video/media/*")
            }

        }
    }

}