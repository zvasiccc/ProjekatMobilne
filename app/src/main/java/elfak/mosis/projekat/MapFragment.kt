package elfak.mosis.projekat

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class MapFragment : Fragment() {
    lateinit var map:MapView
    private lateinit var fabButton:FloatingActionButton
    private lateinit var pretragaButton:Button
    private lateinit var radijusEditText:EditText
    private val sviMarkeri:ArrayList<Marker> = ArrayList<Marker>()
    private val restaurantsViewModel: RestaurantsViewModel by activityViewModels()
    private val koordinateViewModel: KoordinateViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map,container,false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var ctx: Context?=getActivity()?.getApplicationContext()
        Configuration.getInstance().load(ctx,PreferenceManager.getDefaultSharedPreferences((ctx!!)))
        map=requireView().findViewById<MapView>(R.id.map)
        map.setMultiTouchControls(true)
        if(ActivityCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED &&
              ActivityCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            requestPermissionLauncher.launch(
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else{
            setMyLocationOverlay()
        }
        map.controller.setZoom(15.0)
        val startPoint=GeoPoint(43.3209,21.8958)
        map.controller.setCenter(startPoint)

        for(res in restaurantsViewModel.sviRestorani) {
            var marker = Marker(map)
            marker?.position = GeoPoint(res.latituda.toDouble(), res.longituda.toDouble())
            marker.icon=resources.getDrawable(org.osmdroid.library.R.drawable.marker_default_focused_base)
            map.overlays.add(marker)
            sviMarkeri.add(marker)
        }
        fabButton=requireView().findViewById<FloatingActionButton>(R.id.fab)
        fabButton.setOnClickListener{
            val myLocationOverlay=map.overlays.firstOrNull{ it is MyLocationNewOverlay} as MyLocationNewOverlay?
            myLocationOverlay?.run{
                val trenutnaLokacija=myLocation
                val latituda=trenutnaLokacija.latitude
                val longituda=trenutnaLokacija.longitude
                Toast.makeText(requireContext(),"latituda=$latituda a longituda=$longituda",Toast.LENGTH_LONG).show()
                koordinateViewModel.latituda=latituda
                koordinateViewModel.longituda=longituda
                findNavController().navigate(R.id.action_mapFragment_to_EditFragment)

            }
        }

        pretragaButton=requireView().findViewById<Button>(R.id.buttonPretraga)
        radijusEditText=requireView().findViewById<EditText>(R.id.editTextRadius)
        pretragaButton.setOnClickListener {
            if(radijusEditText.text.isNotEmpty()) {
                val myLocationOverlay =
                    map.overlays.firstOrNull { it is MyLocationNewOverlay } as MyLocationNewOverlay?
                myLocationOverlay?.run {
                    val trenutnaLokacija = myLocation
                    val latituda = trenutnaLokacija.latitude
                    val longituda = trenutnaLokacija.longitude
                    val radijus = radijusEditText.text.toString().toDouble()
                    for (marker in sviMarkeri) {
                        map.overlays.remove(marker)
                    }
                    sviMarkeri.clear()
                    for (res in restaurantsViewModel.sviRestorani) {
                        val udaljenost = izracunajUdaljenost(
                            latituda,
                            longituda,
                            res.latituda.toDouble(),
                            res.longituda.toDouble()
                        )
                        if (udaljenost <= radijus) {
                            Toast.makeText(requireContext(),"Postoji resotran u radijusu",Toast.LENGTH_LONG).show()
                            val marker = Marker(map)
                            marker.position = GeoPoint(res.latituda.toDouble(), res.longituda.toDouble())
                            marker.icon = resources.getDrawable(org.osmdroid.library.R.drawable.marker_default_focused_base)
                            map.overlays.add(marker)//dodajemo ga na mapu jer je unutar radijusa
                            sviMarkeri.add(marker)
                        }
                    }
                }
                map.invalidate()
            }

        }
        map.invalidate()

    }

    private fun izracunajUdaljenost(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371 // Zemljopisni radijus u kilometrima

        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)

        val a = sin(latDistance / 2) * sin(latDistance / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(lonDistance / 2) * sin(lonDistance / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return R * c
    }
    private fun setMyLocationOverlay(){
        var myLocationOverlay=MyLocationNewOverlay(GpsMyLocationProvider(activity),map)
        myLocationOverlay.enableMyLocation()
        myLocationOverlay.enableFollowLocation()
        myLocationOverlay.isDrawAccuracyEnabled=true //crta krug oko markeram
        val redMarkerDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.red_marker)
        val redMarkerBitmap = redMarkerDrawable?.toBitmap()
        myLocationOverlay.setPersonIcon(redMarkerBitmap)

        map.overlays.add(myLocationOverlay)
    }
    private val requestPermissionLauncher=
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){
            isGranted:Boolean->
            if(isGranted){
                setMyLocationOverlay()
            }
        }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_new_place->{
                this.findNavController().navigate(R.id.action_mapFragment_to_EditFragment)
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        var item=menu.findItem(R.id.action_my_places_list)
        item.isVisible=false
        item=menu.findItem(R.id.action_show_map)
        item.isVisible=false;
    }
    override fun onResume(){
        super.onResume()
        map.onResume()
    }
    override fun onPause(){
        super.onPause()
        map.onPause()
    }

}


