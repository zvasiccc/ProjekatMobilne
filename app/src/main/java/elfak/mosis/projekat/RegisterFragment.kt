package elfak.mosis.projekat

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import elfak.mosis.projekat.databinding.FragmentRegisterBinding
import elfak.mosis.projekat.R.id.buttonIzaberiteSliku

class RegisterFragment : Fragment() {

    private lateinit var buttonIzaberiteSliku: Button;
    private lateinit var imageView:ImageView;
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var database:DatabaseReference

    companion object{
        val IMAGE_REQUEST_CODE=100
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonIzaberiteSliku.setOnClickListener {
            Toast.makeText(requireContext(), "Izabrali ste sliku", Toast.LENGTH_SHORT).show()
            izaberiSliku()
        }
        binding.buttonRegister.setOnClickListener {
            when {
                TextUtils.isEmpty(binding.editTextKorisnickoIme.text.toString()) -> {
                    Toast.makeText(
                        requireContext(),
                        "Niste uneli korisnicko ime",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(binding.editTextPassword.text.toString())->{
                    Toast.makeText(requireContext(),"Niste uneli lozinku",Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val korisnickoIme = binding.editTextKorisnickoIme.text.toString()
                    val password = binding.editTextPassword.text.toString()
                    val ime = binding.editTextIme.text.toString()
                    val prezime = binding.editTextPrezime.text.toString()
                    val brojTelefona = binding.editTextBrojTelefona.text.toString()

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(korisnickoIme,password).addOnCompleteListener(
                        OnCompleteListener { task->
                            if(task.isSuccessful){
                                val firebaseUser: FirebaseUser? = task.result?.user
                                val userID: String? = firebaseUser?.uid
                                if (userID != null) {
                                    val userReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID)
                                    val user = User(korisnickoIme, password, ime, prezime, brojTelefona)
                                    userReference.setValue(user).addOnSuccessListener {
                                        Toast.makeText(requireContext(), "Uspesno ste se registrovali", Toast.LENGTH_SHORT).show()
                                        val intent=Intent(requireContext(),MainActivity::class.java)
                                        intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        intent.putExtra("user_id",firebaseUser.uid)
                                        intent.putExtra("email_id",korisnickoIme)
                                        startActivity(intent)
                                    }.addOnFailureListener {
                                        Toast.makeText(requireContext(), "Greska prilikom upisa u bazu", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Neuspesna registracija jer: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }

    private fun izaberiSliku() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            imageView.setImageURI(data?.data)
        }
    }
}
