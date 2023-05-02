package it.polito.mad.lab2

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import it.polito.mad.lab2.model.ProfileViewModel
import java.io.File

class ShowProfile: Fragment(R.layout.fragment_show_profile) {
    private val sharedViewModel: ProfileViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if ( checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
            checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
        ) {
            val permission = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            requestPermissions(permission, 112)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.show_profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.pencil -> {/*(
                    (parentFragmentManager
                        .findFragmentById(R.id.fragmentContainerView)) as NavHostFragment
                    ).navController
                    .navigate(R.id.editProfileNavGraph)*/
                val navHost = (requireParentFragment().parentFragmentManager.findFragmentById(R.id.mainFragmentContainerView)) as NavHostFragment
                navHost.navController.navigate(R.id.editProfileNavGraph)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nickname = view.findViewById<TextView>(R.id.NickName)
        sharedViewModel.nickname.observe(viewLifecycleOwner){
            nickname.text = it
        }

        val fullName = view.findViewById<TextView>(R.id.FullName)
        sharedViewModel.fullName.observe(viewLifecycleOwner){
            fullName.text = it
        }

        val email = view.findViewById<TextView>(R.id.Mail)
        sharedViewModel.email.observe(viewLifecycleOwner){
            email.text = it
        }

        val birth = view.findViewById<TextView>(R.id.Birth)
        sharedViewModel.birth.observe(viewLifecycleOwner){
            birth.text = it
        }

        val gender = view.findViewById<TextView>(R.id.Gender)
        sharedViewModel.gender.observe(viewLifecycleOwner){
            gender.text = it
        }

        val city = view.findViewById<TextView>(R.id.City)
        sharedViewModel.city.observe(viewLifecycleOwner){
            city.text = it
        }

        val sports = view.findViewById<TextView>(R.id.Sports)
        sharedViewModel.sports.observe(viewLifecycleOwner){
            sports.text = it
        }

        val photo = view.findViewById<ImageView>(R.id.ProfileImage)
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
                photo.setImageBitmap(mappa)
            }
            else{
                photo.setImageResource(R.drawable.person)
            }
        }
    }
}

/*


        val file = File(requireActivity().filesDir, "profile.jpg")
        if (file.exists()) {
            val fis = requireActivity().openFileInput("profile.jpg")
            val imageData = fis.readBytes()
            fis.close()
            val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
            photo.setImageBitmap(bitmap)
        }
 */