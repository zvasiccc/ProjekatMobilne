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
import elfak.mosis.projekat.R.id.buttonIzaberiteSliku

class RegisterFragment : Fragment() {

    private lateinit var buttonIzaberiteSliku: Button;
    private lateinit var imageView:ImageView;
    companion object{
        val IMAGE_REQUEST_CODE=100
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonIzaberiteSliku = view.findViewById(R.id.buttonIzaberiteSliku)
        imageView =view.findViewById(R.id.slikaKorisnika)
        buttonIzaberiteSliku.setOnClickListener {
            Toast.makeText(requireContext(), "Izabrali ste sliku", Toast.LENGTH_SHORT).show()
            izaberiSliku()
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
