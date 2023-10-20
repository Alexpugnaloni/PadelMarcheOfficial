package com.example.padelmarcheofficial

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.padelmarcheofficial.databinding.ActivityManagingPhotoBinding
import com.example.padelmarcheofficial.dataclass.Funzionalita
 //import com.theartofdev.edmodo.cropper.CropImage
//import com.theartofdev.edmodo.cropper.CropImageView
import java.io.IOException

/**
 * * Activity per inserire foto all'interno dell'applicazione.
 *
 * @author Di Biase Alessandro, Donnini Valerio, Sopranzetti Lorenzo
 */
class ManagingPHOTO : AppCompatActivity() { /*
    /**
     * binding con la grafica
     */
    private lateinit var binding: ActivityManagingPhotoBinding

    /**
     * Immagine da inserire
     */
    private lateinit var image: Bitmap

    /**
     * Codice per la richiesta di scelta immagine dalla galleria
     */
    private val pickImage = 666

    /**
     * Codice per la richiesta di acquisizione immagine da fotocamera
     */
    private val pickCamera = 865

    /**
     * Codice per la richiesta del permesso di prelievo immagini dalla galleria
     */
    private val pickPermissionFiles = 798

    /**
     * Codice per la richiesta del permesso di acquisizione immagini dalla fotocamera
     */
    private val pickPermissionCamera = 444

    /**
     * Indirizzo Uri relativo alla locazione dell'immagine
     */
    private lateinit var imageUri: Uri

    /**
     * Variabile per stabilire se l'immagine dovrà essere rotonda o rettangolare
     */
    private var rounded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityManagingPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Se è arrivata la richiesta da parte di un intent esterno ho già la foto, l'utente non deve
        // sceglierla dalla galleria o scattarla
        if (intent.getStringExtra("UriShared") != null) {
            imageUri = intent.getStringExtra("UriShared")!!.toUri()
            image = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            //check sulla dimensione
            //per corretta visualizzazione su canvas
            if (image.byteCount > 100000000) {
                val displayMetrics = DisplayMetrics()
                windowManager?.defaultDisplay?.getMetrics(displayMetrics)
                val rapp = image.height.toFloat() / image.width.toFloat()
                val w = displayMetrics.widthPixels
                val h = (w.toFloat() * rapp).toInt()
                image = Bitmap.createScaledBitmap(image, w, h, false)
            }
            image=Funzionalita().adattaImage(image)
            binding.imageView3.setImageBitmap(image)
        } else {
            //preparo la richiesta per l'immagine
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
            imageUri =
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
            //Se la schermata conteneva già un immagine, cioè il dispotivio viene ruotato dopo aver caricato l'immagine
            // la ricarico altrimenti richiedo i permessi
            // o il caricamento di un'immagine
            if (savedInstanceState != null)
                rounded = savedInstanceState.getBoolean("rounded")
            if (savedInstanceState == null || savedInstanceState.getString("foto") == "nulla") {
                if (savedInstanceState == null)
                    rounded = intent.extras?.get("Rounded") as Boolean
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("INSERIMENTO FOTO")
                alertDialog.setMessage("Vuoi scattare una foto o caricarne una?")
                alertDialog.setPositiveButton("Scatta") { _, _ ->
                    if (this.let { it1 ->
                            //controllo sui permessi
                            ContextCompat.checkSelfPermission(
                                it1,
                                Manifest.permission.CAMERA
                            )
                        } != PackageManager.PERMISSION_GRANTED) {
                        //se non ci sono li chiedo
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.CAMERA),
                            pickPermissionCamera
                        )
                    }

                    if (this.let { it1 ->
                            ContextCompat.checkSelfPermission(
                                it1,
                                Manifest.permission.CAMERA
                            )
                        } == PackageManager.PERMISSION_GRANTED) {
                        //avvio l'intent per scattare la foto
                        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                        camera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                        camera.putExtra("crop", true)

                        startActivityForResult(camera, pickCamera)
                    }
                }
                alertDialog.setNegativeButton("Scegli") { _, _ ->
                    if (this.let { it1 ->
                            ContextCompat.checkSelfPermission(
                                it1,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                        } != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            pickPermissionFiles
                        )
                    }
                    if (this.let { it1 ->
                            ContextCompat.checkSelfPermission(
                                it1,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                        } == PackageManager.PERMISSION_GRANTED) {
                        //avvio intent per prendere immagine da galleria
                        val gallery =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                        startActivityForResult(gallery, pickImage)
                    }
                }
                val alert = alertDialog.create()
                alert.setCancelable(false)
                alert.show()
            } else {
                //recupero l'immagine già inserita
                image = BitmapFactory.decodeStream(openFileInput("myImage"))
                binding.imageView3.setImageBitmap(image)
            }
        }
        if (rounded) {
            //l'immagine deve essere rotonda
            binding.cardview.radius = 1000F
            val displayMetrics = DisplayMetrics()
            windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            val min =
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    (displayMetrics.heightPixels - resources.getDimensionPixelSize(
                        resources.getIdentifier(
                            "navigation_bar_height",
                            "dimen",
                            "android"
                        )
                    ) * 2) / 5 * 4
                } else {
                    displayMetrics.widthPixels / 5 * 4
                }
            binding.cardview.layoutParams.width = min
            binding.cardview.layoutParams.height = min
            binding.imageView3.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            //l'immagine deve essere rettangolare
            binding.cardview.radius = 0F
            binding.cardview.layoutParams.width = 0
            binding.cardview.layoutParams.height = 0
            binding.imageView3.scaleType = ImageView.ScaleType.CENTER_INSIDE
        }
        //Configuro il menù inferiore
        //per la gestione dell'immagine
        binding.navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.rotating_clock -> {
                    rotate(sensoorario = true)
                    binding.imageView3.setImageBitmap(image)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.rotating_not_clock -> {
                    rotate(sensoorario = false)
                    binding.imageView3.setImageBitmap(image)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.confirm -> {
                    //salvo l'immagine per poterla poii recuperare
                    saveLocalImage()
                    setResult(Activity.RESULT_OK)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.delete -> {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.crop -> {
                    saveLocalImage()
                    imageUri = Uri.fromFile(getFileStreamPath("myImage"))
                    Log.d("imageuri", imageUri.toString())
                    if (rounded)
                        CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON)
                            .setMultiTouchEnabled(true).setCropShape(CropImageView.CropShape.OVAL)
                            .setAspectRatio(1, 1).start(this)
                    else
                        CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON)
                            .setMultiTouchEnabled(true).start(this)
                    return@setOnNavigationItemSelectedListener true
                }
                else ->
                    return@setOnNavigationItemSelectedListener false
            }
        }
    }

    /**
     * Ruota l'immagine di 90° in senso orario o antiorario.
     * @param sensoorario se true configura una rotazione in senso orario, altrimenti antiorario
     * @return true se la funzione ò stata eseguita senza problemi
     */
    private fun rotate(sensoorario: Boolean): Boolean {
        val matrix = Matrix()
        if (sensoorario) {
            matrix.postRotate(-90F)
        } else {
            matrix.postRotate(90F)
        }
        image = Bitmap.createBitmap(image, 0, 0, image.width, image.height, matrix, true)
        image=Funzionalita().adattaImage(image)
        return true
    }

    /**
     * Salva un'immagine compressa per poterla passarla mediante intent senza appesantirli
     * @return true se l'operazione viene eseguita senza errori
     */
    private fun saveLocalImage(): Boolean {
        try {
            val fo = this.openFileOutput("myImage", MODE_PRIVATE)
            image.compress(Bitmap.CompressFormat.JPEG, 100, fo)
            fo.close()
        } catch (e: Exception) {
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //SCELTA IMMAGINE DALLA GALLERIA
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            try {
                //recupero l'immagine, la inserisco nell'imageView, la adatto e salvo l'uri
                image = MediaStore.Images.Media.getBitmap(contentResolver, data?.data)
                Log.d("DIM", image.byteCount.toString())
                if (image.byteCount > 100000000) {
                    val displayMetrics = DisplayMetrics()
                    windowManager?.defaultDisplay?.getMetrics(displayMetrics)
                    val rapp = image.height.toFloat() / image.width.toFloat()
                    val w = displayMetrics.widthPixels
                    val h = (w.toFloat() * rapp).toInt()
                    image = Bitmap.createScaledBitmap(image, w, h, false)
                }
                image=Funzionalita().adattaImage(image)
                binding.imageView3.setImageBitmap(image)
                imageUri = data?.data!!
            } catch (e: IOException) {
                Toast.makeText(this, "Errore nel caricamento dell'immagine", Toast.LENGTH_SHORT).show()
                setResult(RESULT_CANCELED)
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        //SCELTA IMMAGINE DALLA FOTOCAMERA
        if (resultCode == RESULT_OK && requestCode == pickCamera) {
            try {
                //recupero immagine, la adatto e la inserisco nell'imageview
                image = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                if (image.byteCount > 100000000) {
                    val displayMetrics = DisplayMetrics()
                    windowManager?.defaultDisplay?.getMetrics(displayMetrics)
                    val rapp = image.height.toFloat() / image.width.toFloat()
                    val w = displayMetrics.widthPixels
                    val h = (w.toFloat() * rapp).toInt()
                    image = Bitmap.createScaledBitmap(image, w, h, false)
                }
                binding.imageView3.setImageBitmap(image)
            } catch (e: IOException) {
                Toast.makeText(this, "Errore nel caricamento dell'immagine", Toast.LENGTH_SHORT).show()
                setResult(RESULT_CANCELED)
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        //Libreria esterna per ritagliare l'immagine
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
            imageUri = result.uri
            image = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            image=Funzionalita().adattaImage(image)
            binding.imageView3.setImageBitmap(image)
        }
        //Se l'utente non seleziona o scatta l'immagine esco dall'activity
        if (resultCode == RESULT_CANCELED && (requestCode == pickCamera || requestCode == pickImage)) {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>, grantResults: IntArray) {
        //Se sono stati concessi i permessi lancio gli intent direttamente da qui
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == pickPermissionFiles && grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Accesso alla memoria consentito", Toast.LENGTH_SHORT).show()
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }else if (requestCode == pickPermissionCamera && grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Accesso alla fotocamera consentito", Toast.LENGTH_SHORT).show()
            val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(camera, pickCamera)
        }else{
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (saveLocalImage())
            outState.putString("foto", "caricata")
        else
            outState.putString("foto", "nulla")
        outState.putBoolean("rounded", rounded)
    } */
}