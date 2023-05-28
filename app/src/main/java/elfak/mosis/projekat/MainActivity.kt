package elfak.mosis.projekat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent

class MainActivity : AppCompatActivity() {
    private lateinit var loginButton: Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            0
        )
        setContentView(R.layout.activity_main)
        loginButton=findViewById(R.id.buttonLogin)
        loginButton.setOnClickListener{
            Intent(applicationContext,LocationService::class.java).apply{
                action=LocationService.ACTION_START
                startService(this)
            }
        }
        //isto se uradi za stop dugme
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_delete -> Toast.makeText(this, "Kliknuli ste brisanje", Toast.LENGTH_SHORT).show()
            R.id.nav_add -> Toast.makeText(this, "Kliknuli ste dodavanje", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}
