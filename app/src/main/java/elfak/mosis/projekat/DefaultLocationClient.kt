package elfak.mosis.projekat

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose

class DefaultLocationClient (
    private val context: Context,
    private val client: FusedLocationProviderClient
):LocationClient{
    override fun getLocationUpdates(interval: Long): Flow<Location> {
        return callbackFlow {
            if(!context.hasLocationPermission()){
                throw LocationClient.LocationException("nemate dozvolu za lokaciju")

            }
            val locationManager=context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if(!isGpsEnabled && isNetworkEnabled){
                throw LocationClient.LocationException("GPS je onemogucen")
            }
            val request=com.google.android.gms.location.LocationRequest.create()
                .setInterval(interval)
                .setFastestInterval(interval)
            val locationCallback=object: LocationCallback(){
                override fun onLocationResult(result:LocationResult){
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let{ location->
                        launch {send(location)}
                    }
                }
            }
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return@callbackFlow
            }
            client.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )
            awaitClose{
                client.removeLocationUpdates(locationCallback)
            }
        }
    }
}