package elfak.mosis.projekat

import android.app.AlertDialog
import android.content.Context
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import androidx.navigation.findNavController
import elfak.mosis.projekat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var loginButton: Button
    //private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // appBarConfiguration = AppBarConfiguration(navController.graph)
        // setupActionBarWithNavController(navController, appBarConfiguration)
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ),
//            0
//        )
            //setContentView(R.layout.activity_main)
//        loginButton=findViewById(R.id.buttonLogin)
//        loginButton.setOnClickListener{
//            Intent(applicationContext,LocationService::class.java).apply{
//                action=LocationService.ACTION_START
//                startService(this)
//            }
 //       }
        //isto se uradi za stop dugme
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment_content_main)
        when (item.itemId) {
            // R.id.nav_delete -> Toast.makeText(this, "Kliknuli ste brisanje", Toast.LENGTH_SHORT).show()
            // R.id.nav_add -> Toast.makeText(this, "Kliknuli ste dodavanje", Toast.LENGTH_SHORT).show()
            R.id.action_show_map-> {
                Toast.makeText(this,"Prikazujem mapu",Toast.LENGTH_SHORT).show()
                if(navController.currentDestination?.id == R.id.profilFragment) {
                    navController.navigate(R.id.action_profilFragment_to_mapFragment)
                }
            }
            R.id.action_new_place->Toast.makeText(this,"Dodajem novo mesto",Toast.LENGTH_SHORT).show()
            R.id.action_my_places_list->Toast.makeText(this,"Prikazujem listu mesta",Toast.LENGTH_SHORT).show()
            R.id.action_about->{
                val i:Intent=Intent(this,About::class.java)
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
