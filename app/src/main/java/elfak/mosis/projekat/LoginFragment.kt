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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.projekat.databinding.FragmentLoginBinding
import elfak.mosis.projekat.databinding.FragmentRegisterBinding

//import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private lateinit var buttonIdiNaRegistraciju: Button
    //private lateinit var buttonPrijaviSe:Button
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginBinding
    //private val profileViewModel: ProfileViewModel by activityViewModels()
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
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = null
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonIdiNaRegistraciju =view.findViewById(R.id.buttonRegistracija);
        buttonIdiNaRegistraciju.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment2_to_registerFragment)
        }
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
                                    //Toast.makeText(requireContext(),"uspesno ste se prijavili",Toast.LENGTH_SHORT).show()
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