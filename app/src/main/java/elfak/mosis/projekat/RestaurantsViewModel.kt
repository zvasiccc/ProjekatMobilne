package elfak.mosis.projekat

import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModel

class RestaurantsViewModel:ViewModel() {
    var selectedRestaurant: Restaurant? = null
    var sviRestorani: ArrayList<Restaurant> =ArrayList<Restaurant>()
    var adapter: ArrayAdapter<Restaurant>? = null

}