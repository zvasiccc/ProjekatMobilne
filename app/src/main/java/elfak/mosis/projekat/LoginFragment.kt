package elfak.mosis.projekat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation


class LoginFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_login, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var buttonRegistracija:Button =view.findViewById(R.id.button_registracija);
        buttonRegistracija.setOnClickListener {
            Toast.makeText(requireContext(),"kliknuto dugme ", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(view).navigate(R.id.action_loginFragment2_to_registerFragment)

        }
    }



}