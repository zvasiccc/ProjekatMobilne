package elfak.mosis.projekat

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
//import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private lateinit var buttonIdiNaRegistraciju: Button;
    private lateinit var buttonPrijaviSe:Button
    //private lateinit var auth: FirebaseAuth
    // Dodato nesto
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_login, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonIdiNaRegistraciju =view.findViewById(R.id.buttonRegistracija);
        buttonIdiNaRegistraciju.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment2_to_registerFragment)
        }
        buttonPrijaviSe=view.findViewById(R.id.buttonLogin)
        buttonPrijaviSe.setOnClickListener{

        }

    }



}