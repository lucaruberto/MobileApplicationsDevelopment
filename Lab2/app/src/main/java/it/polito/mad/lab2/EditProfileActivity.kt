package it.polito.mad.lab2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.get

class EditProfileActivity : AppCompatActivity() {
    lateinit var b: Button
    lateinit var userInfo: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        b = findViewById(R.id.done)
        userInfo = findViewById(R.id.user_data)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString( "fullname", userInfo[0].toString())
        outState.putString( "nickname", userInfo[1].toString())
        outState.putString( "spinner", userInfo[2].toString())
        outState.putString( "birthDate", userInfo[3].toString())
        outState.putString( "emailAddress", userInfo[4].toString())
        outState.putString( "userPassword", userInfo[5].toString())
        outState.putString( "phoneNumber", userInfo[6].toString())
        outState.putString( "sportsList", userInfo[7].toString())
        outState.putString( "reputation", userInfo[8].toString())


    }
}