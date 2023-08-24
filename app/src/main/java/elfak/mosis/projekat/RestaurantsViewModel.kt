package elfak.mosis.projekat

import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*

class RestaurantsViewModel:ViewModel() {
    var selectedRestaurant: Restaurant? = null
    var sviRestorani: ArrayList<Restaurant> =ArrayList<Restaurant>()
    var filtiraniRestorani:ArrayList<Restaurant> =ArrayList<Restaurant>()
    var adapter: ArrayAdapter<Restaurant>? = null
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val restaurantRef: DatabaseReference = database.getReference("Restaurants")
    var filterAdapter: ArrayAdapter<Restaurant>? = null
    init {
        loadRestaurantsFromFirebase()
    }

    private fun loadRestaurantsFromFirebase() {
        restaurantRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                sviRestorani.clear()
                for (restaurantSnapshot in snapshot.children) {
                    val restaurant = restaurantSnapshot.getValue(Restaurant::class.java)
                    restaurant?.let {
                        sviRestorani.add(it)
                    }
                }

                // Notify the adapter that data has changed
                // (placesListView.adapter as? ArrayAdapter<Restaurant>)?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error (optional)
                // Toast.makeText(requireContext(), "Failed to load restaurants", Toast.LENGTH_SHORT).show()
                Log.v("Greska", "Failed to load restaurants")
            }
        })
    }
}