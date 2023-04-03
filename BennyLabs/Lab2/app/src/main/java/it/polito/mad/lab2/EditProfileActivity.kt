package it.polito.mad.lab2

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream
import java.io.FileDescriptor
import java.io.IOException


class EditProfileActivity : AppCompatActivity() {
    lateinit var nickname: TextView
    lateinit var fullname: TextView
    lateinit var email: TextView
    lateinit var birth: TextView
    lateinit var sex: TextView
    lateinit var city: TextView
    lateinit var lists: TextView
    private lateinit var profilepicture: ImageButton
    lateinit var imageView: ImageView
    var mappa: Bitmap? = null
    var imageUri: Uri? = null


    private var galleryActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                imageUri = it.data?.data
                contentResolver.takePersistableUriPermission(imageUri!!, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                val inputImage = uriToBitmap(imageUri!!)
                val rotated = rotateBitmap(inputImage!!)
                imageView.setImageBitmap(rotated)
                mappa = rotated
                val stream = ByteArrayOutputStream()
                inputImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val imageData = stream.toByteArray()
                val fos = openFileOutput("profile.jpg", Context.MODE_PRIVATE)
                fos.write(imageData)
                fos.close()
            }
        }

    private var cameraActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                val inputImage = uriToBitmap(imageUri!!)
                val rotated = rotateBitmap(inputImage!!)
                imageView.setImageBitmap(rotated)
                mappa = rotated
            }
        }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraActivityResultLauncher.launch(cameraIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        nickname = findViewById<EditText>(R.id.EditNickname)
        fullname = findViewById(R.id.EditFullName)
        email = findViewById(R.id.EditMail)
        birth = findViewById(R.id.EditBirth)
        sex = findViewById(R.id.EditSex)
        city = findViewById(R.id.EditCity)
        lists = findViewById(R.id.EditSportslist)
        profilepicture = findViewById(R.id.EditImage) //Image Button
        imageView = findViewById(R.id.ProfileImage) //Image VIEW


        nickname.text = this.intent?.getStringExtra("nickname")
        fullname.text = this.intent?.getStringExtra("fullname")
        email.text = this.intent?.getStringExtra("email")
        birth.text = this.intent?.getStringExtra("birth")
        sex.text = this.intent?.getStringExtra("sex")
        city.text = this.intent?.getStringExtra("city")
        lists.text = this.intent?.getStringExtra("lists")

        registerForContextMenu(profilepicture)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.homeditmenu, menu)
        return true

    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.photocontextmenu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.Gallery -> {
                val galleryIntent =
                    Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                galleryActivityResultLauncher.launch(galleryIntent)
                return true
            }

            R.id.Picture -> {
                if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permission = arrayOf(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    requestPermissions(permission, 112)
                } else {
                    openCamera()
                }
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.check -> {
                val i = Intent(this, ShowProfileActivity::class.java)
                Toast.makeText(this, "Profile Mode!", Toast.LENGTH_SHORT).show()

                if (nickname.text.toString() != "")
                    i.putExtra("nickname", nickname.text.toString())
                if (fullname.text.toString() != "")
                    i.putExtra("fullname", fullname.text.toString())
                if (email.text.toString() != "")
                    i.putExtra("email", email.text.toString())
                if (birth.text.toString() != "")
                    i.putExtra("birth", birth.text.toString())
                if (sex.text.toString() != "")
                    i.putExtra("sex", sex.text.toString())
                if (city.text.toString() != "")
                    i.putExtra("city", city.text.toString())
                if (lists.text.toString() != "")
                    i.putExtra("lists", lists.text.toString())
                if (imageUri != null)
                    i.putExtra("profilepic", imageUri.toString())

                setResult(RESULT_OK, i)
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {

        outState.putString("nickname", nickname.text.toString())
        outState.putString("fullname", fullname.text.toString())
        outState.putString("email", email.text.toString())
        outState.putString("birth", birth.text.toString())
        outState.putString("sex", sex.text.toString())
        outState.putString("city", city.text.toString())
        outState.putString("lists", lists.text.toString())

        if (imageUri != null)
            outState.putString("profilepic", imageUri!!.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        nickname.text = savedInstanceState.getString("nickname")
        fullname.text = savedInstanceState.getString("fullname")
        email.text = savedInstanceState.getString("email")
        birth.text = savedInstanceState.getString("birth")
        sex.text = savedInstanceState.getString("sex")
        city.text = savedInstanceState.getString("city")
        lists.text = savedInstanceState.getString("lists")

        if (savedInstanceState.getString("profilepic") != null) {
            imageUri = Uri.parse(savedInstanceState.getString("profilepic"))
            var mappa: Bitmap? = null
            try {
                mappa = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                } else {
                    val source: ImageDecoder.Source =
                        ImageDecoder.createSource(contentResolver, imageUri!!)
                    ImageDecoder.decodeBitmap(source)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            imageView.setImageBitmap(mappa)
        }
    }


    private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {
        try {
            val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    @SuppressLint("Range", "Recycle")
    fun rotateBitmap(input: Bitmap): Bitmap? {
        val orientationColumn =
            arrayOf(MediaStore.Images.Media.ORIENTATION)
        val cur: Cursor? = contentResolver.query(imageUri!!, orientationColumn, null, null, null)
        var orientation = -1
        if (cur != null && cur.moveToFirst()) {
            orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]))
        }
        Log.d("tryOrientation", orientation.toString() + "")
        val rotationMatrix = Matrix()
        rotationMatrix.setRotate(orientation.toFloat())
        return Bitmap.createBitmap(input, 0, 0, input.width, input.height, rotationMatrix, true)
    }
}
