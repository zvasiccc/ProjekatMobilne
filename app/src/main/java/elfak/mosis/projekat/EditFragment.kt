package elfak.mosis.projekat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import elfak.mosis.projekat.databinding.FragmentEditBinding

class EditFragment : Fragment() {

    private val restaurantsViewModel: RestaurantsViewModel by activityViewModels()
    private val profileViewModel:ProfileViewModel by activityViewModels()
    private lateinit var binding: FragmentEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val trenutnoPrijavljeniKorisnik=FirebaseAuth.getInstance().currentUser
        val editName: EditText = binding.editTextImeMesta
        val editDesc: EditText = binding.editTextOpisMesta
        val editLongitude: EditText = binding.editTextLongituda
        val editLatitude: EditText = binding.editTextLatituda

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
            val restoran: Restaurant = Restaurant(name, opis, longituda, latituda)
            restaurantsViewModel.sviRestorani.add(restoran)
            restaurantsViewModel.adapter?.notifyDataSetChanged()
            //profileViewModel.bodovi = profileViewModel.bodovi.plus(10)
            trenutnoPrijavljeniKorisnik?.let{user->
                val database:FirebaseDatabase=FirebaseDatabase.getInstance()
               val userRef:DatabaseReference=database.getReference("Users").child(user.uid)
                userRef.child("bodovi").get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val trenutniBodovi=task.result?.value as? Long?:0
                            val noviBodovi=trenutniBodovi+10
                            userRef.child("bodovi").setValue(noviBodovi)
                            Toast.makeText(
                                requireContext(),
                                "Uspesno ste dodali novo mesto i azurirali bodove na $noviBodovi",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Doslo je do greske ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
            }

            editName.setText("")
            editDesc.setText("")
            editLongitude.setText("")
            editLatitude.setText("")
        }

        binding.buttonOtkazi.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
