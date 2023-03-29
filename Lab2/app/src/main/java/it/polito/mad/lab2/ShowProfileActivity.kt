package it.polito.mad.lab2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

class ShowProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_profile)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.editIcon){
            val i = Intent(this, EditProfileActivity::class.java)
            startActivity(i)
        }

        return super.onOptionsItemSelected(item)
    }


    /* MAY BE USEFUL FOR LAUNCHING EDIT ACTIVITY!!
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        switch(item.getItemId()) {
            case R . id . settings :
            startActivity(new Intent (this, EditPreferences.class));

            return (true);
        }
        return (super.onOptionsItemSelected(item));
    }
    */
}