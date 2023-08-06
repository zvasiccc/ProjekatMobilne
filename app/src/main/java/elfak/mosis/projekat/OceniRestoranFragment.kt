package elfak.mosis.projekat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import elfak.mosis.projekat.databinding.FragmentOceniRestoranBinding
import elfak.mosis.projekat.databinding.FragmentProfilBinding
import kotlinx.coroutines.selects.select


class OceniRestoranFragment : Fragment() {
    private val restaurantsViewModel: RestaurantsViewModel by activityViewModels()
    private lateinit var binding:FragmentOceniRestoranBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOceniRestoranBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        restaurantsViewModel.selectedRestaurant?.let { selectedRestaurant ->
            binding.textViewNazivRestorana.text = selectedRestaurant.ime
            binding.buttonOceni.setOnClickListener {
                var staraProsecnaOcena=selectedRestaurant.prosecnaOcena
                val dobijenaOcena=binding.editTextOcena.text.toString().toInt()
                val novaProsecnaOcena=((staraProsecnaOcena* selectedRestaurant.brojOcena)+dobijenaOcena)/(selectedRestaurant.brojOcena+1)
                selectedRestaurant.prosecnaOcena=novaProsecnaOcena
                selectedRestaurant.brojOcena++
                updateRestaurantInDatabase(selectedRestaurant)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun updateRestaurantInDatabase(restaurant: Restaurant) {
        val restaurantRef: DatabaseReference = restaurantsViewModel.database.getReference("Restaurants").child(restaurant.key)

        // Koristite setValue metodu da biste ažurirali objekat u bazi podataka
        restaurantRef.setValue(restaurant)
            .addOnSuccessListener {
                // Ako je ažuriranje uspešno, obavestite korisnika
                Toast.makeText(requireContext(), "Ocena je uspešno sačuvana.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                // Ako je došlo do greške prilikom ažuriranja, obavestite korisnika
                Toast.makeText(requireContext(), "Došlo je do greške pri čuvanju ocene.", Toast.LENGTH_SHORT).show()
            }
    }
}