package it.polito.mad.lab2

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.File


class ShowProfileActivity : AppCompatActivity() {

    private lateinit var nickname: TextView
    private lateinit var fullname: TextView
    private lateinit var email: TextView
    private lateinit var birth: TextView
    private lateinit var sex: TextView
    private lateinit var city: TextView
    private lateinit var lists: TextView
    private lateinit var sharedPref: SharedPreferences
    private lateinit var photo: ImageView
    private var imageUri: Uri? = null


    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            nickname.text = result.data?.getStringExtra("nickname")
            fullname.text = result.data?.getStringExtra("fullname")
            email.text = result.data?.getStringExtra("email")
            birth.text = result.data?.getStringExtra("birth")
            sex.text = result.data?.getStringExtra("sex")
            city.text = result.data?.getStringExtra("city")
            lists.text = result.data?.getStringExtra("lists")
            if (result.data?.getStringExtra("profilepic") != null) {
                imageUri = Uri.parse(result.data?.getStringExtra("profilepic"))
                var map: Bitmap? = null
                try {
                    map = if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images.Media.getBitmap(
                            contentResolver, imageUri
                        )
                    } else {
                        val source: ImageDecoder.Source =
                            ImageDecoder.createSource(contentResolver, imageUri!!)
                        ImageDecoder.decodeBitmap(source)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                photo.setImageBitmap(map)
            }

            //saving preferences
            val editor = sharedPref.edit()
            editor.apply {
                putString("nickname", nickname.text.toString())
                putString("fullname", fullname.text.toString())
                putString("email", email.text.toString())
                putString("birth", birth.text.toString())
                putString("sex", sex.text.toString())
                putString("city", city.text.toString())
                putString("lists", lists.text.toString())
                apply()
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_profile)

        nickname = findViewById(R.id.NickName)
        fullname = findViewById(R.id.FullName)
        email = findViewById(R.id.Mail)
        birth = findViewById(R.id.Birth)
        sex = findViewById(R.id.Sex)
        city = findViewById(R.id.City)
        lists = findViewById(R.id.Sport)
        photo = findViewById(R.id.ProfileImage)
        sharedPref = getSharedPreferences("preferences_file", MODE_PRIVATE)

        nickname.text = sharedPref.getString("nickname", "")
        fullname.text = sharedPref.getString("fullname", "")
        email.text = sharedPref.getString("email", "")
        birth.text = sharedPref.getString("birth", "")
        sex.text = sharedPref.getString("sex", "")
        city.text = sharedPref.getString("city", "")
        lists.text = sharedPref.getString("lists", "")

        if ( checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
        ) {
            val permission = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            requestPermissions(permission, 112)
        }

        val file = File(filesDir, "profile.jpg")
        if (file.exists()) {
            val fis = openFileInput("profile.jpg")
            val imageData = fis.readBytes()
            fis.close()
            val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
            photo.setImageBitmap(bitmap)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.show_profile_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.pencil -> {
                Toast.makeText(this, "Edit Mode!", Toast.LENGTH_SHORT).show()
                val nextpage=Intent(this,EditProfileActivity::class.java)

                if(nickname.text.toString()!= "")
                    nextpage.putExtra("nickname",nickname.text.toString())
                if(fullname.text.toString()!= "")
                    nextpage.putExtra("fullname",fullname.text.toString())
                if(email.text.toString()!= "")
                    nextpage.putExtra("email",email.text.toString())
                if(birth.text.toString()!= "")
                    nextpage.putExtra("birth",birth.text.toString())
                if(sex.text.toString()!= "")
                    nextpage.putExtra("sex",sex.text.toString())
                if(city.text.toString()!= "")
                    nextpage.putExtra("city",city.text.toString())
                if(lists.text.toString()!= "")
                    nextpage.putExtra("lists",lists.text.toString())

                startForResult.launch(nextpage)
                return  true
            }
            else -> return  super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("nickname",nickname.text.toString())
        outState.putString("fullname",fullname.text.toString())
        outState.putString("email",email.text.toString())
        outState.putString("birth",birth.text.toString())
        outState.putString("sex",sex.text.toString())
        outState.putString("city",city.text.toString())
        outState.putString("lists",lists.text.toString())

        if(imageUri != null)
            outState.putString("profilepic",imageUri.toString())

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        nickname.text=savedInstanceState.getString("nickname")
        fullname.text=savedInstanceState.getString("fullname")
        email.text=savedInstanceState.getString("email")
        birth.text=savedInstanceState.getString("birth")
        sex.text=savedInstanceState.getString("sex")
        city.text=savedInstanceState.getString("city")
        lists.text=savedInstanceState.getString("lists")
        if( savedInstanceState.getString("profilepic") != null) {
            imageUri = Uri.parse(savedInstanceState.getString("profilepic"))
           var mappa: Bitmap? = null
           try {
               mappa = if (Build.VERSION.SDK_INT < 28) {
                   MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
               } else {
                   val source: ImageDecoder.Source =
                       ImageDecoder.createSource(contentResolver,imageUri!!)
                   ImageDecoder.decodeBitmap(source)
               }
           } catch (e: Exception) {
               e.printStackTrace()
           }
            photo.setImageBitmap(mappa)
        }
    }

}