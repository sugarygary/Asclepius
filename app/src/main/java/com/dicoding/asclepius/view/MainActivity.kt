package com.dicoding.asclepius.view

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.google.android.material.snackbar.Snackbar
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import kotlin.math.roundToInt


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentImageUri: Uri? = null
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri == null || uri == Uri.EMPTY) {
            return@registerForActivityResult
        } else {
            uCrop(uri)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.progressIndicator.isGone = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("currentImageUri")) {
                currentImageUri = Uri.parse(savedInstanceState.getString("currentImageUri"))
                showImage()
            }
        }
        initListeners()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        currentImageUri?.let { uri ->
            outState.putString("currentImageUri", uri.toString())
        }
        super.onSaveInstanceState(outState)
    }

    private fun initListeners() {
        with(binding) {
            galleryButton.setOnClickListener {
                startGallery()
            }
            analyzeButton.setOnClickListener {
                analyzeImage()
            }
            btnClear.setOnClickListener {
                currentImageUri = null
                showImage()
            }
            toolbarMain.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.news -> {
                        val newsIntent = Intent(this@MainActivity, NewsActivity::class.java)
                        startActivity(newsIntent)
                        true
                    }

                    R.id.history -> {
                        val historyIntent = Intent(this@MainActivity, HistoryActivity::class.java)
                        startActivity(historyIntent)
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        with(binding) {
            val tempCurrentImageUri = currentImageUri
            if (tempCurrentImageUri == null || tempCurrentImageUri == Uri.EMPTY) {
                tvEmpty.isVisible = true
                previewImageView.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this@MainActivity,
                        R.drawable.skin_cancer_placeholder
                    )
                )
                btnClear.isGone = true
            } else {
                btnClear.isVisible = true
                tvEmpty.isGone = true
                previewImageView.setImageDrawable(null) // image view perlu di force redraw ketika file di URI di rewrite
                previewImageView.setImageURI(tempCurrentImageUri)
            }
        }
    }

    private fun analyzeImage() {
        val tempImageUri = currentImageUri
        if (tempImageUri == null || tempImageUri == Uri.EMPTY) {
            showGallerySnackbar()
            return
        }
        binding.progressIndicator.isVisible = true
        ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        showToast("Terjadi error saat klasifikasi")
                    }
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    runOnUiThread {
                        results?.let { it ->
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                val sortedCategories =
                                    it[0].categories.sortedByDescending { it?.score }
                                val score = (sortedCategories[0].score * 100).roundToInt()
                                val label = sortedCategories[0].label
                                moveToResult(
                                    score,
                                    label
                                )
                            }
                        }
                    }
                }
            }
        ).classifyStaticImage(tempImageUri)

    }

    private fun uCrop(sourceUri: Uri) {
        val fileName = "preview_cropped.jpg"
        val destinationFile = File(this@MainActivity.cacheDir, fileName)
        val destinationUri = Uri.fromFile(destinationFile)

        val uCropOptions = UCrop.Options().apply {
            setCompressionFormat(Bitmap.CompressFormat.JPEG)
            setActiveControlsWidgetColor(getColor(R.color.asclepius_blue))
            setStatusBarColor(getColor(R.color.asclepius_blue))
            setFreeStyleCropEnabled(true)
            setToolbarColor(getColor(R.color.asclepius_bg_light))
        }

        UCrop.of(sourceUri, destinationUri)
            .withOptions(uCropOptions)
            .start(this)
    }

    private fun moveToResult(score: Int, label: String) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_SCORE, score)
            putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri.toString())
            putExtra(ResultActivity.EXTRA_LABEL, label)
            putExtra(ResultActivity.EXTRA_NEW_RESULT, true)
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showGallerySnackbar() {
        Snackbar.make(
            binding.root,
            "Gambar belum dipilih",
            Snackbar.LENGTH_LONG
        ).setAction("BUKA GALERI") {
            startGallery()
        }.show()

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            currentImageUri = resultUri
            showImage()
        } else if (resultCode == UCrop.RESULT_ERROR) {
            showToast("Terjadi kesalahan saat mengedit foto")
        }
    }
}