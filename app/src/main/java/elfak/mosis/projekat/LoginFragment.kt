package elfak.mosis.projekat

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.children
import androidx.navigation.Navigation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.projekat.databinding.FragmentLoginBinding
import elfak.mosis.projekat.databinding.FragmentRegisterBinding

//import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private lateinit var buttonIdiNaRegistraciju: Button
    private lateinit var buttonPrijaviSe:Button
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginBinding
    //private lateinit var bindind:LoginFragmentBinding //u fragment login se treba ukljuci na vrh neka slovca
    // Dodato nesto
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        for (x in menu.children) {
            x.isVisible = false
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.fragment_login, container, false)
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonIdiNaRegistraciju =view.findViewById(R.id.buttonRegistracija);
        buttonIdiNaRegistraciju.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment2_to_registerFragment)
        }
        //buttonPrijaviSe=view.findViewById(R.id.buttonLogin)
//        buttonPrijaviSe.setOnClickListener {
//            val profilFragment = ProfilFragment() // Kreiranje instance fragmenta koji želite prikazati
//            val transaction = requireActivity().supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.loginFragment, profilFragment) // Zamjena trenutnog fragmenta sa profilFragmentom
//            transaction.addToBackStack(null) // Dodavanje trenutnog fragmenta na back stack, tako da se može vratiti prethodni fragment pritiskom na "Back" dugme
//            transaction.commit()
//        }
//         fun signin(email:String,password:String){
//            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener<AuthResult>() {
//
//            }
//         }
        binding.buttonLogin.setOnClickListener{

                when {
                    TextUtils.isEmpty(binding.textKorisnickoIme.text.toString()) -> {
                        Toast.makeText(
                            requireContext(),
                            " Niste uneli korisnicko ime",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    TextUtils.isEmpty(binding.textLozinka.text.toString())->{
                        Toast.makeText(requireContext(),"Niste uneli lozinku", Toast.LENGTH_SHORT).show()
                    }
                    else->{
                        val korisnickoIme = binding.textKorisnickoIme.text.toString() + "@gmail.com"
                        val password = binding.textLozinka.text.toString()

                        FirebaseAuth.getInstance().signInWithEmailAndPassword(korisnickoIme,password)
                            .addOnCompleteListener{ task->
                                if(task.isSuccessful){
                                    Toast.makeText(requireContext(),"uspesno ste se prijavili",Toast.LENGTH_SHORT).show()
                                    val intent=Intent(requireContext(),MainActivity::class.java)
                                    intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id",FirebaseAuth.getInstance().currentUser!!.uid)
                                    intent.putExtra("email_id",korisnickoIme)
                                   // startActivity(intent)

                                    Navigation.findNavController(view).navigate(R.id.action_loginFragment2_to_profilFragment)
                                }
                                else{
                                    Toast.makeText(requireContext(),task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
        }

    }



}