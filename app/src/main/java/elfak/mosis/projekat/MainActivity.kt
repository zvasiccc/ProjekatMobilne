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
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import elfak.mosis.projekat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var loginButton: Button
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController:NavController
    private lateinit var restaurantsViewModel: RestaurantsViewModel
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        restaurantsViewModel = ViewModelProvider(this).get(RestaurantsViewModel::class.java)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
         appBarConfiguration = AppBarConfiguration(navController.graph)
         setupActionBarWithNavController(navController, appBarConfiguration)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                restaurantsViewModel.selectedRestaurant = null
                navController.popBackStack()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment_content_main)
        when (item.itemId) {
            R.id.action_show_map-> {
                if(navController.currentDestination?.id == R.id.profilFragment)
                   {
                    navController.navigate(R.id.action_profilFragment_to_mapFragment)
                }
                if(  navController.currentDestination?.id == R.id.EditFragment )
                {
                    navController.navigate(R.id.action_EditFragment_to_mapFragment)
                }
                if(navController.currentDestination?.id==R.id.listaMestaFragment)
                {
                    navController.navigate(R.id.action_listaMestaFragment_to_mapFragment)
                }
                if(navController.currentDestination?.id==R.id.viewFragment)
                {
                    navController.navigate(R.id.action_viewFragment_to_mapFragment)
                }
                if(navController.currentDestination?.id==R.id.rangiraniKorisniciFragment)
                {
                    navController.navigate(R.id.action_rangiraniKorisniciFragment_to_mapFragment)
                }
                if(navController.currentDestination?.id==R.id.filtriranjeFragment)
                {
                    navController.navigate(R.id.action_filtriranjeFragment_to_mapFragment)
                }
                if(navController.currentDestination?.id==R.id.listaFiltriranihRestoranaFragment)
                {
                    navController.navigate(R.id.action_listaFiltriranihRestoranaFragment_to_mapFragment)
                }
            }
            R.id.action_new_place->
            {

                if(navController.currentDestination?.id==R.id.profilFragment) {
                    navController.navigate((R.id.action_profilFragment_to_EditFragment))
                }
                if(navController.currentDestination?.id==R.id.listaMestaFragment)
                {
                    navController.navigate(R.id.action_listaMestaFragment_to_EditFragment)
                }
                if(navController.currentDestination?.id==R.id.viewFragment)
                {
                    restaurantsViewModel.selectedRestaurant=null
                    navController.navigate(R.id.action_viewFragment_to_EditFragment)
                }
                if(navController.currentDestination?.id==R.id.rangiraniKorisniciFragment)
                {
                    navController.navigate(R.id.action_rangiraniKorisniciFragment_to_EditFragment)
                }
                if (navController.currentDestination?.id==R.id.mapFragment)
                {
                    navController.navigate(R.id.action_mapFragment_to_EditFragment)
                }
                if(navController.currentDestination?.id==R.id.filtriranjeFragment)
                {
                    navController.navigate(R.id.action_filtriranjeFragment_to_EditFragment)
                }
                if(navController.currentDestination?.id==R.id.listaFiltriranihRestoranaFragment)
                {
                    navController.navigate(R.id.action_listaFiltriranihRestoranaFragment_to_EditFragment)
                }
            }
            R.id.action_my_places_list-> {
                if(navController.currentDestination?.id == R.id.profilFragment)
                {
                    navController.navigate(R.id.action_profilFragment_to_listaMestaFragment)
                }
                if(navController.currentDestination?.id == R.id.EditFragment) {
                    navController.navigate(R.id.action_EditFragment_to_listaMestaFragment)
                }
                if(navController.currentDestination?.id==R.id.viewFragment)
                {
                    navController.navigate(R.id.action_viewFragment_to_listaMestaFragment)
                }
                if(navController.currentDestination?.id==R.id.rangiraniKorisniciFragment)
                {
                    navController.navigate(R.id.action_rangiraniKorisniciFragment_to_listaMestaFragment)
                }
                if(navController.currentDestination?.id==R.id.mapFragment)
                {
                    navController.navigate(R.id.action_mapFragment_to_listaMestaFragment)
                }
                if(navController.currentDestination?.id==R.id.filtriranjeFragment)
                {
                    navController.navigate(R.id.action_filtriranjeFragment_to_listaMestaFragment)
                }
                if(navController.currentDestination?.id==R.id.listaFiltriranihRestoranaFragment)
                {
                    navController.navigate(R.id.action_listaFiltriranihRestoranaFragment_to_listaMestaFragment)
                }
            }
//
            R.id.action_rangirani_korisnici->{
                if(navController.currentDestination?.id==R.id.profilFragment){
                   navController.navigate(R.id.action_profilFragment_to_rangiraniKorisniciFragment)
                }
                if(navController.currentDestination?.id==R.id.EditFragment)
                {
                    navController.navigate(R.id.action_EditFragment_to_rangiraniKorisniciFragment)
                }
                if(navController.currentDestination?.id==R.id.listaMestaFragment)
                {
                    navController.navigate(R.id.action_listaMestaFragment_to_rangiraniKorisniciFragment)
                }
                if(navController.currentDestination?.id==R.id.viewFragment)
                {
                    navController.navigate(R.id.action_viewFragment_to_rangiraniKorisniciFragment)
                }
                if(navController.currentDestination?.id==R.id.mapFragment)
                {
                    navController.navigate(R.id.action_mapFragment_to_rangiraniKorisniciFragment)
                }
                if(navController.currentDestination?.id==R.id.filtriranjeFragment)
                {
                    navController.navigate(R.id.action_filtriranjeFragment_to_rangiraniKorisniciFragment)
                }
                if(navController.currentDestination?.id==R.id.listaFiltriranihRestoranaFragment)
                {
                    navController.navigate(R.id.action_listaFiltriranihRestoranaFragment_to_rangiraniKorisniciFragment)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

