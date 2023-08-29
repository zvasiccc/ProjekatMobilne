package elfak.mosis.projekat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import elfak.mosis.projekat.databinding.FragmentEditBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.*

class EditFragment : Fragment() {

    private val restaurantsViewModel: RestaurantsViewModel by activityViewModels()
    private val koordinateViewModel: KoordinateViewModel by activityViewModels()
    private lateinit var binding: FragmentEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = null
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val trenutnoPrijavljeniKorisnik=FirebaseAuth.getInstance().currentUser
        val editName: EditText = binding.editTextImeMesta
        val editDesc: EditText = binding.editTextOpisMesta
        val editLongitude: EditText = binding.editTextLongituda
        val editLatitude: EditText = binding.editTextLatituda
        if(koordinateViewModel.latituda!=null && koordinateViewModel.longituda!=null){
            editLatitude.setText(koordinateViewModel.latituda.toString())
            editLongitude.setText(koordinateViewModel.longituda.toString())
            editLongitude.isEnabled=false
            editLatitude.isEnabled=false
        }
        binding.buttonDodajMesto.isEnabled = false

        editName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.buttonDodajMesto.isEnabled = (editName.text.length>0)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No implementation needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No implementation needed
            }
        })
        editDesc.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.buttonDodajMesto.isEnabled = (editDesc.text.length>0)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No implementation needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No implementation needed
            }
        })

        if (restaurantsViewModel.selectedRestaurant != null) {
            editName.setText(restaurantsViewModel.selectedRestaurant!!.ime)
            editDesc.setText(restaurantsViewModel.selectedRestaurant!!.opis)
            editLongitude.setText(restaurantsViewModel.selectedRestaurant!!.longituda)
            editLatitude.setText(restaurantsViewModel.selectedRestaurant!!.latituda)
        }

        binding.buttonDodajMesto.setOnClickListener {
            val name = editName.text.toString()
            val opis = editDesc.text.toString()
            val longituda = editLongitude.text.toString()
            val latituda = editLatitude.text.toString()
            val noviRestoran: Restaurant = Restaurant(name, opis, longituda, latituda)
            noviRestoran.idAutora=trenutnoPrijavljeniKorisnik!!.uid
            // Konvertujemo datum u string
            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val trenutniDatum = sdf.format(Date())
            noviRestoran.datumKreiranja = trenutniDatum
            restaurantsViewModel.sviRestorani.add(noviRestoran)
            restaurantsViewModel.adapter?.notifyDataSetChanged()
            trenutnoPrijavljeniKorisnik?.let{user->
                //val database:FirebaseDatabase=FirebaseDatabase.getInstance()
                val database:FirebaseDatabase=restaurantsViewModel.database
               val userRef:DatabaseReference=database.getReference("Users").child(user.uid)
                var restaurantRef:DatabaseReference=database.getReference("Restaurants")
                if(restaurantsViewModel.selectedRestaurant != null)
                {
                    restaurantRef = restaurantRef.child(restaurantsViewModel.selectedRestaurant!!.key)
                    noviRestoran.key = restaurantsViewModel.selectedRestaurant!!.key
                }
                else {
                    restaurantRef = restaurantRef.push()//doda se prazan objekat
                    noviRestoran.key = restaurantRef.key!!//na nas malopre napravljeni restoran dodamo taj kljuc
                }
                restaurantRef.setValue(noviRestoran)
                        .addOnSuccessListener {
                            Toast.makeText(
                                requireContext(),
                                "Uspesno ste dodali restoran u bazu",
                                Toast.LENGTH_LONG
                            ).show()
                            restaurantsViewModel.selectedRestaurant=null
                            koordinateViewModel.latituda=null
                            koordinateViewModel.longituda=null
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Niste dodali restoran u bazu",Toast.LENGTH_SHORT).show()
            }


            userRef.child("bodovi").get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val trenutniBodovi=task.result?.value as? Long?:0
                            val noviBodovi=trenutniBodovi+10
                            userRef.child("bodovi").setValue(noviBodovi)
                            Toast.makeText(requireContext(), "Uspesno ste azurirali bodove na $noviBodovi", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Doslo je do greske ", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

            editName.setText("")
            editDesc.setText("")
            editLongitude.setText("")
            editLatitude.setText("")
        }

        binding.buttonOtkazi.setOnClickListener {
            restaurantsViewModel.selectedRestaurant=null
            koordinateViewModel.latituda=null
            koordinateViewModel.longituda=null
            findNavController().popBackStack()

        }
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        var item=menu.findItem(R.id.action_new_place)
        item.isVisible=false;
    }
}
