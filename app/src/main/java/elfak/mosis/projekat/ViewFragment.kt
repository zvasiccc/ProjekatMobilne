package elfak.mosis.projekat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import elfak.mosis.projekat.databinding.FragmentEditBinding
import elfak.mosis.projekat.databinding.FragmentViewBinding


class ViewFragment : Fragment() {
    private val restaurantsViewModel: RestaurantsViewModel by activityViewModels()
    private lateinit var binding: FragmentViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (restaurantsViewModel.selectedRestaurant != null) {
            binding.textViewIme.text = restaurantsViewModel.selectedRestaurant!!.ime
            binding.textViewOpis.text = restaurantsViewModel.selectedRestaurant!!.opis
            binding.textViewProsecnaOcena.text=restaurantsViewModel.selectedRestaurant!!.prosecnaOcena.toString()

        }
        super.onViewCreated(view, savedInstanceState)
    }

}