package elfak.mosis.projekat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import elfak.mosis.projekat.databinding.FragmentProfilBinding
import elfak.mosis.projekat.databinding.FragmentViewBinding


class ProfilFragment : Fragment() {
    private lateinit var binding:FragmentProfilBinding
    private val profileViewModel: ProfileViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfilBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            binding.textViewBodovi.text=profileViewModel.bodovi.toString()

        super.onViewCreated(view, savedInstanceState)

    }
}