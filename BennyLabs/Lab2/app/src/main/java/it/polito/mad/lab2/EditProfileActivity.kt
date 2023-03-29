package it.polito.mad.lab2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class EditProfileActivity : AppCompatActivity() {
    lateinit var nickname : TextView;
    lateinit var fullname : TextView;
    lateinit var email    : TextView;
    lateinit var birth : TextView;
    lateinit var sex : TextView;
    lateinit var city : TextView;
    lateinit var lists : TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        nickname=findViewById<EditText>(R.id.EditNickname)
        fullname=findViewById(R.id.EditFullName);
        email=findViewById(R.id.EditMail);
        birth=findViewById(R.id.EditBirth);
        sex=findViewById(R.id.EditSex);
        city=findViewById(R.id.EditCity);
        lists=findViewById(R.id.EditSportslist);

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.homeditmenu,menu);
        return true;

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.check -> {
                Toast.makeText(this, "Profile Mode!", Toast.LENGTH_SHORT).show();
                finish();
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