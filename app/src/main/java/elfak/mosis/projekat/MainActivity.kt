package elfak.mosis.projekat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_delete-> Toast.makeText(this," kliknuli ste brisanje",Toast.LENGTH_SHORT).show()
            R.id.nav_add->Toast.makeText(this,"kliknuli ste dodavanje",Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}