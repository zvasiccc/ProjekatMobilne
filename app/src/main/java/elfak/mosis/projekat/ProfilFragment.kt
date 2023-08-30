package elfak.mosis.projekat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import elfak.mosis.projekat.databinding.FragmentProfilBinding
import elfak.mosis.projekat.databinding.FragmentViewBinding


class ProfilFragment : Fragment() {
    private lateinit var binding:FragmentProfilBinding
    //private val profileViewModel: ProfileViewModel by activityViewModels()
    private val restaurantsViewModel:RestaurantsViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilBinding.inflate(inflater,container,false)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = null
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val trenutnoPrijavljeniKorisnik= FirebaseAuth.getInstance().currentUser
        trenutnoPrijavljeniKorisnik?.let { user ->
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val userRef: DatabaseReference = database.getReference("Users").child(user.uid)
            val storage= FirebaseStorage.getInstance()
            val storageReference = storage.reference

            userRef.child("urlSlike").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val imageUrl = dataSnapshot.value as? String
                    if (!imageUrl.isNullOrEmpty()) {
                        Glide.with(requireContext())
                            .load(imageUrl)
                            .into(binding.imageViewSlika)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        requireContext(),
                        "Doslo je do greske pri dohvatanju URL-a slike",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

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
            userRef.child("korisnickoIme").addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userName=dataSnapshot.value as? String
                    binding.textViewKorisnickoIme.text=userName
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        requireContext(),
                        "Doslo je do greske pri dohvatanju korisnickog imena",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
        restaurantsViewModel.selectedRestaurant = null

        binding.buttonFiltrirajMesta.setOnClickListener{
            findNavController().navigate(R.id.action_profilFragment_to_filtriranjeFragment)
        }
        binding.buttonOdjaviSe.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_profilFragment_to_loginFragment2)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            //ne radi nista
        }
        super.onViewCreated(view, savedInstanceState)
    }
}