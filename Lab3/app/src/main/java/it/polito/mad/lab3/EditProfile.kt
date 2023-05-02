package it.polito.mad.lab3

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
import android.graphics.Picture
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import it.polito.mad.lab3.model.ProfileViewModel
import java.io.ByteArrayOutputStream
import java.io.FileDescriptor
import java.io.IOException


class EditProfile: Fragment(R.layout.fragment_edit_profile) {
    private val sharedViewModel: ProfileViewModel by activityViewModels()

    private lateinit var nickname: EditText
    private lateinit var fullName: EditText
    private lateinit var email: EditText
    private lateinit var birth: EditText
    private lateinit var gender: EditText
    private lateinit var city: EditText
    private lateinit var sports: EditText
    private lateinit var imageView: ImageView
    private lateinit var profilepicture: ImageButton
    private var map: Bitmap? = null
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.check -> {
                saveProfileData()

                val navHost = (requireParentFragment().parentFragmentManager.findFragmentById(R.id.mainFragmentContainerView)) as NavHostFragment
                navHost.navController.navigate(R.id.showProfileNavGraph)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = requireActivity().menuInflater
        inflater.inflate(R.menu.photocontextmenu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.Gallery -> {
                val galleryIntent =
                    Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                galleryActivityResultLauncher.launch(galleryIntent)
                return true
            }

            R.id.Picture -> {
                if (checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
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
            else -> super.onContextItemSelected(item)
        }
    }

    private var galleryActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                imageUri = it.data?.data
                requireActivity().contentResolver.takePersistableUriPermission(imageUri!!, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                val inputImage = getResizedBitmap(uriToBitmap(imageUri!!))
                val stream = ByteArrayOutputStream()
                val rotated = rotateBitmap(inputImage!!)
                inputImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
                imageView.setImageBitmap(rotated)
                map = rotated
                val imageData = stream.toByteArray()
                requireActivity().openFileOutput("profile.jpg", Context.MODE_PRIVATE)
                    .use { fos-> fos.write(imageData); fos.close() }
            }
        }


    private fun getResizedBitmap(bm: Bitmap?): Bitmap? {
        val scaleToUse = 20 // this will be our percentage

        val sizeY = (bm?.height?.times(scaleToUse) ?: return null) / 100
        val sizeX = bm.width * sizeY / bm.height

        return Bitmap.createScaledBitmap(bm, sizeX, sizeY, false)
    }

    private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {
        try {
            val parcelFileDescriptor = requireActivity().contentResolver.openFileDescriptor(selectedFileUri, "r")
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
        val cur: Cursor? = requireActivity().contentResolver.query(imageUri!!, orientationColumn, null, null, null)
        var orientation = -1
        if (cur != null && cur.moveToFirst()) {
            orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]))
        }
        Log.d("tryOrientation", orientation.toString() + "")
        val rotationMatrix = Matrix()
        rotationMatrix.setRotate(orientation.toFloat())
        return Bitmap.createBitmap(input, 0, 0, input.width, input.height, rotationMatrix, true)
    }


    private var cameraActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val inputImage = getResizedBitmap(uriToBitmap(imageUri!!))
                val stream = ByteArrayOutputStream()
                val rotated = rotateBitmap(inputImage!!)
                inputImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
                imageView.setImageBitmap(rotated)
                map = rotated
                val imageData = stream.toByteArray()
                requireActivity().openFileOutput("profile.jpg", Context.MODE_PRIVATE)
                    .use { fos-> fos.write(imageData); fos.close() }
            }
        }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageUri = requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraActivityResultLauncher.launch(cameraIntent)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nickname = view.findViewById<EditText>(R.id.EditNickname)
        sharedViewModel.nickname.observe(viewLifecycleOwner){
            nickname.setText(it)
        }
        /*nickname.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                sharedViewModel.setNickname(nickname.text.toString())
            }
        }*/

        fullName = view.findViewById<EditText>(R.id.EditFullName)
        sharedViewModel.fullName.observe(viewLifecycleOwner){
            fullName.setText(it)
        }
        //fullName.doAfterTextChanged { sharedViewModel.setFullName(it.toString()) }

        email = view.findViewById<EditText>(R.id.EditMail)
        sharedViewModel.email.observe(viewLifecycleOwner){
            email.setText(it)
        }
        //email.doAfterTextChanged { sharedViewModel.setEmail(it.toString()) }

        birth = view.findViewById<EditText>(R.id.EditBirth)
        sharedViewModel.birth.observe(viewLifecycleOwner){
            birth.setText(it)
        }
        //birth.doAfterTextChanged { sharedViewModel.setBirth(it.toString()) }

        gender = view.findViewById<EditText>(R.id.EditGender)
        sharedViewModel.gender.observe(viewLifecycleOwner){
            gender.setText(it)
        }
        //gender.doAfterTextChanged { sharedViewModel.setGender(it.toString()) }

        city = view.findViewById<EditText>(R.id.EditCity)
        sharedViewModel.city.observe(viewLifecycleOwner){
            city.setText(it)
        }
        //city.doAfterTextChanged { sharedViewModel.setCity(it.toString()) }

        sports = view.findViewById<EditText>(R.id.EditSports)
        sharedViewModel.sports.observe(viewLifecycleOwner){
            sports.setText(it)
        }
        //sports.doAfterTextChanged { sharedViewModel.setSports(it.toString()) }

        imageView = view.findViewById<ImageView>(R.id.ProfileImage)
        sharedViewModel.imageUri.observe(viewLifecycleOwner){
            if(it != null) {
                var mappa: Bitmap? = null
                try {
                    mappa = if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, it)
                    } else {
                        val source: ImageDecoder.Source =
                            ImageDecoder.createSource(requireActivity().contentResolver, it)
                        ImageDecoder.decodeBitmap(source)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                imageView.setImageBitmap(mappa)
            }
            else{
                imageView.setImageResource(R.drawable.person)
            }
        }


        profilepicture = view.findViewById(R.id.EditImage) //Image Button
        registerForContextMenu(profilepicture)

    }

    private fun saveProfileData() {
        sharedViewModel.setNickname(nickname.text.toString())
        sharedViewModel.setFullName(fullName.text.toString())
        sharedViewModel.setEmail(email.text.toString())
        sharedViewModel.setBirth(birth.text.toString())
        sharedViewModel.setGender(gender.text.toString())
        sharedViewModel.setCity(city.text.toString())
        sharedViewModel.setSports(sports.text.toString())
        sharedViewModel.setImageUri(imageUri)
    }
}