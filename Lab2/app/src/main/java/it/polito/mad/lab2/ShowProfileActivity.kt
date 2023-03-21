package it.polito.mad.lab2

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity

class ShowProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_profile)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
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