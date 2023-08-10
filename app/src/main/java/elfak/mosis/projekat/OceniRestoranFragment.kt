package elfak.mosis.projekat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import elfak.mosis.projekat.databinding.FragmentOceniRestoranBinding
import elfak.mosis.projekat.databinding.FragmentProfilBinding
import kotlinx.coroutines.selects.select


class OceniRestoranFragment : Fragment() {
    private val restaurantsViewModel: RestaurantsViewModel by activityViewModels()
    private lateinit var binding:FragmentOceniRestoranBinding
    val trenutnoPrijavljeniKorisnik= FirebaseAuth.getInstance().currentUser

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
                trenutnoPrijavljeniKorisnik?.let {user->
                    val database:FirebaseDatabase=restaurantsViewModel.database
                    val userRef=database.getReference("Users").child(user.uid)
                    userRef.child("bodovi").get().addOnCompleteListener{task->
                        if(task.isSuccessful)
                        {
                            val trenutniBodovi=task.result?.value as? Long?:0
                            val noviBodovi=trenutniBodovi+5
                            userRef.child("bodovi").setValue(noviBodovi)
                            Toast.makeText(requireContext(), "Uspesno ste azurirali bodove na $noviBodovi", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Doslo je do greske u dodavanju bodova", Toast.LENGTH_SHORT).show()
                        }

                        }

                    }

                }
            }
        super.onViewCreated(view, savedInstanceState)
        }



    private fun updateRestaurantInDatabase(restaurant: Restaurant) {
        val restaurantRef: DatabaseReference = restaurantsViewModel.database.getReference("Restaurants").child(restaurant.key)
        restaurantRef.setValue(restaurant)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Ocena je uspešno sačuvana.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Došlo je do greške pri čuvanju ocene.", Toast.LENGTH_SHORT).show()
            }
    }
}