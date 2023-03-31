package it.polito.mad.lab2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
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
    lateinit var profilepicture : ImageButton ;

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
        profilepicture=findViewById(R.id.EditImage);

        nickname.text = this.intent?.getStringExtra("nickname");
        fullname.text = this.intent?.getStringExtra("fullname");
        email.text = this.intent?.getStringExtra("email");
        birth.text = this.intent?.getStringExtra("birth");
        sex.text = this.intent?.getStringExtra("sex");
        city.text = this.intent?.getStringExtra("city");
        lists.text = this.intent?.getStringExtra("lists");

        registerForContextMenu(profilepicture);

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.homeditmenu,menu);
        return true;

    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {

        menuInflater.inflate(R.menu.photocontextmenu,menu);
        print("menu inflato");
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId)
        {
            R.id.Gallery-> {
                Toast.makeText(this, "Select From gallery!", Toast.LENGTH_SHORT).show();
                return  true;
            }
            R.id.Picture ->{
                Toast.makeText(this, "Take a photo!", Toast.LENGTH_SHORT).show();
                return true;

            }
        }

        return super.onContextItemSelected(item)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.check -> {
                val i = Intent(this,ShowProfileActivity::class.java);
                Toast.makeText(this, "Profile Mode!", Toast.LENGTH_SHORT).show();
                if(nickname.text.toString()!= "")
                {
                    i.putExtra("nickname",nickname.text.toString());
                }
                if(fullname.text.toString()!= "")
                {
                    i.putExtra("fullname",fullname.text.toString());
                }
                if(email.text.toString()!= "")
                {
                    i.putExtra("email",email.text.toString());
                }
                if(birth.text.toString()!= "")
                {
                    i.putExtra("birth",birth.text.toString());
                }
                if(sex.text.toString()!= "")
                {
                    i.putExtra("sex",sex.text.toString());
                }
                if(city.text.toString()!= "")
                {
                    i.putExtra("city",city.text.toString());
                }
                if(lists.text.toString()!= "")
                {
                    i.putExtra("lists",lists.text.toString());
                }
                setResult(RESULT_OK,i);
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