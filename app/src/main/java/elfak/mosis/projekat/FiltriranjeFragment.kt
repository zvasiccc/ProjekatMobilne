package elfak.mosis.projekat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import elfak.mosis.projekat.databinding.FragmentFiltriranjeBinding
import elfak.mosis.projekat.databinding.FragmentRegisterBinding
import kotlinx.coroutines.selects.select


class FiltriranjeFragment : Fragment() {
    private val restaurantsViewModel: RestaurantsViewModel by activityViewModels()
    private val profileViewModel:ProfileViewModel by activityViewModels()
    private lateinit var binding:FragmentFiltriranjeBinding
    private var selectedRadioButtonId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFiltriranjeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonPotvrdi: Button =binding.buttonPotvrdi
        val radioGroup:RadioGroup=binding.radioGroup
        radioGroup.setOnCheckedChangeListener{  group, checkedId->
            selectedRadioButtonId=checkedId
            run {
                when (checkedId) {
                    R.id.option1 -> {
                        binding.editTextVrednost.hint="Korisnicko ime"

                    }
                    R.id.option2 -> {
                        binding.editTextVrednost.hint="Minimalna ocena"
                    }
                }
            }
        }
        buttonPotvrdi.setOnClickListener{
            when(selectedRadioButtonId) {
                R.id.option1-> {
                    val usersRef = restaurantsViewModel.database.getReference("Users")
                    usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            restaurantsViewModel.filtiraniRestorani.clear()
                            for (userSnapshot in snapshot.children) {
                                val user = userSnapshot.getValue(User::class.java)
                                //usersnapshot je cela klasa usera zajedno sa key, sadrzi razne reference i vremena
                                //a user su izvuceni moji atributi iz toga
                                if (user!!.korisnickoIme == binding.editTextVrednost.text.toString()) {
                                    for (res in restaurantsViewModel.sviRestorani) {
                                        if (res.idAutora == userSnapshot.key) {
                                            restaurantsViewModel.filtiraniRestorani.add(res)
                                        }
                                    }
                                }
                            }
                            restaurantsViewModel.filterAdapter?.notifyDataSetChanged()
                            findNavController().navigate(R.id.action_filtriranjeFragment_to_listaFiltriranihRestoranaFragment)
                            //ako se brzo dobije odgovor onda druga nit jos nije stigla da ide u drugi fragment
                            // i restaurantsViewModel.filterAdapter je null i ne obavestava se nista
                            //vec kada nit stigne do drugog fragmenta ima sve potrebne podatke i odmah renderuje
                            //i prikazuje sta treba
                        }
                        //ovo je asinhrono i nit posalje zahtev na firebase i ceka odgovor od baze na set on data change, druga nit
                        //nastavlja dalje i moze da se klikne dugme pre nego sto je pribavljeni podaci, druga nit prelazi na
                        //sledeci fragment i tamo prvo renderuje praznu listu, dok se ovde ne ucitaju podaci i posalje notify
                        //da su podaci ucitani i u drugom fragmentu se azurira prikaz liste sa novim opdacima

                        override fun onCancelled(error: DatabaseError) {
                            // Handle the error if any occurs
                            Toast.makeText(
                                requireContext(),
                                "Error fetching users: ${error.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    })
                }
                R.id.option2->{
                    val unesenaMinimalnaOcena:Double=binding.editTextVrednost.text.toString().toDouble()
                     val restaurantsRef=restaurantsViewModel.database.getReference("Restaurants")
                    restaurantsRef.addListenerForSingleValueEvent(object: ValueEventListener
                    {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            restaurantsViewModel.filtiraniRestorani.clear()
                            for(restaurantSnapshot in snapshot.children){
                                val restaurant=restaurantSnapshot.getValue(Restaurant::class.java)
                                if(restaurant?.prosecnaOcena!! >=unesenaMinimalnaOcena )
                                {
                                    restaurantsViewModel.filtiraniRestorani.add(restaurant)
                                }
                            }
                            restaurantsViewModel.filterAdapter?.notifyDataSetChanged()
                            findNavController().navigate(R.id.action_filtriranjeFragment_to_listaFiltriranihRestoranaFragment)
                        }
                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                requireContext(),
                                "Error fetching users: ${error.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }
            }

        }

    }
    override fun onResume() {
        super.onResume()
        binding.radioGroup.clearCheck()
        binding.editTextVrednost.text.clear()
    }
}



