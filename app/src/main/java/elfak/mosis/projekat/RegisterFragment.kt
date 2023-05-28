package elfak.mosis.projekat

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
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
        // Inflate the layout for this fragment
        // ovo radi return inflater.inflate(R.layout.fragment_register, container, false)
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //buttonIzaberiteSliku = view.findViewById(R.id.buttonIzaberiteSliku)
        //imageView =view.findViewById(R.id.slikaKorisnika)
        binding.buttonIzaberiteSliku.setOnClickListener {
            Toast.makeText(requireContext(), "Izabrali ste sliku", Toast.LENGTH_SHORT).show()
            izaberiSliku()
        }
        binding.buttonRegister.setOnClickListener{

            val korisnickoIme=binding.editTextKorisnickoIme.text.toString()
            val password=binding.editTextPassword.text.toString()
            val ime = binding.editTextIme.text.toString()
            val prezime=binding.editTextPrezime.text.toString()
            val brojTelefona=binding.editTextBrojTelefona.text.toString()

            database=FirebaseDatabase.getInstance().getReference("Users")
            val User = User(korisnickoIme,password,ime,prezime,brojTelefona)
            database.child(korisnickoIme).setValue(User).addOnSuccessListener {

                binding.editTextKorisnickoIme.text.clear()
                binding.editTextPassword.text.clear()
                binding.editTextIme.text.clear()
                binding.editTextPrezime.text.clear()
                binding.editTextBrojTelefona.text.clear()
                Toast.makeText(requireContext(),"Uspesno ste se registrovali",Toast.LENGTH_SHORT).show()

            }.addOnFailureListener{
                Toast.makeText(requireContext()," neuspesna registracija",Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun izaberiSliku(){
        val intent =Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== IMAGE_REQUEST_CODE && resultCode==RESULT_OK){
         imageView.setImageURI(data?.data)
        }
    }
}
