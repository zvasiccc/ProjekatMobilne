package elfak.mosis.projekat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import elfak.mosis.projekat.databinding.FragmentProfilBinding
import elfak.mosis.projekat.databinding.FragmentViewBinding


class ProfilFragment : Fragment() {
    private lateinit var binding:FragmentProfilBinding
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val restaurantsViewModel:RestaurantsViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfilBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val trenutnoPrijavljeniKorisnik= FirebaseAuth.getInstance().currentUser
        trenutnoPrijavljeniKorisnik?.let { user ->
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val userRef: DatabaseReference = database.getReference("Users").child(user.uid)
            userRef.child("bodovi").addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val brojBodova=dataSnapshot.value as? Long ?:0
                    binding.textViewBodovi.text=brojBodova.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        requireContext(),
                        "Doslo je do greske pri dohvatanju bodova",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
        restaurantsViewModel.selectedRestaurant = null

        binding.buttonFiltrirajMesta.setOnClickListener{
            findNavController().navigate(R.id.action_profilFragment_to_filtriranjeFragment)
        }



        super.onViewCreated(view, savedInstanceState)
    }
}