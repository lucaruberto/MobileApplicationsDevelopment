package it.polito.mad.lab2

import android.content.ClipData.Item
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast

class ShowProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_profile)


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
                startActivity(nextpage);
                return  true;
            }
            else -> return  super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}