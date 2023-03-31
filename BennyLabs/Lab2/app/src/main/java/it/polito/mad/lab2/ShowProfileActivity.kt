package it.polito.mad.lab2

import android.app.Activity
import android.content.ClipData.Item
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts

class ShowProfileActivity : AppCompatActivity() {

    lateinit var nickname : TextView;
    lateinit var fullname : TextView;
    lateinit var email    : TextView;
    lateinit var birth : TextView;
    lateinit var sex : TextView;
    lateinit var city : TextView;
    lateinit var lists : TextView;

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            nickname.text = result.data?.getStringExtra("nickname");
            fullname.text = result.data?.getStringExtra("fullname");
            email.text = result.data?.getStringExtra("email");
            birth.text = result.data?.getStringExtra("birth");
            sex.text = result.data?.getStringExtra("sex");
            city.text = result.data?.getStringExtra("city");
            lists.text = result.data?.getStringExtra("lists");
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_profile)
        nickname=findViewById(R.id.NickName)
        fullname=findViewById(R.id.FullName);
        email=findViewById(R.id.Mail);
        birth=findViewById(R.id.Birth);
        sex=findViewById(R.id.Sex);
        city=findViewById(R.id.City);
        lists=findViewById(R.id.Sport);

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.homemenu,menu);
        return true;

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.pencil -> {
                Toast.makeText(this, "Edit Mode!", Toast.LENGTH_SHORT).show();
                val nextpage=Intent(this,EditProfileActivity::class.java);
                if(nickname.text.toString()!= "")
                {
                    nextpage.putExtra("nickname",nickname.text.toString());
                }
                if(fullname.text.toString()!= "")
                {
                    nextpage.putExtra("fullname",fullname.text.toString());
                }
                if(email.text.toString()!= "")
                {
                    nextpage.putExtra("email",email.text.toString());
                }
                if(birth.text.toString()!= "")
                {
                    nextpage.putExtra("birth",birth.text.toString());
                }
                if(sex.text.toString()!= "")
                {
                    nextpage.putExtra("sex",sex.text.toString());
                }
                if(city.text.toString()!= "")
                {
                    nextpage.putExtra("city",city.text.toString());
                }
                if(lists.text.toString()!= "")
                {
                    nextpage.putExtra("lists",lists.text.toString());
                }


                startForResult.launch(nextpage);
                return  true;
            }
            else -> return  super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("nickname",nickname.text.toString());
        outState.putString("fullname",fullname.text.toString());
        outState.putString("email",email.text.toString());
        outState.putString("birth",birth.text.toString());
        outState.putString("sex",sex.text.toString());
        outState.putString("city",city.text.toString());
        outState.putString("lists",lists.text.toString());
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        nickname.text=savedInstanceState.getString("nickname");
        fullname.text=savedInstanceState.getString("fullname");
        email.text=savedInstanceState.getString("email");
        birth.text=savedInstanceState.getString("birth");
        sex.text=savedInstanceState.getString("sex");
        city.text=savedInstanceState.getString("city");
        lists.text=savedInstanceState.getString("lists");
    }

}