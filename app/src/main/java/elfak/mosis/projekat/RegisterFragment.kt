package elfak.mosis.projekat

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import elfak.mosis.projekat.databinding.FragmentRegisterBinding
import java.io.File
import java.io.FileOutputStream

class RegisterFragment : Fragment() {

    //private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var buttonIzaberiteSliku: Button
    private var storageReference = Firebase.storage
    private lateinit var imageView: ImageView
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var database: DatabaseReference
    private var url: Uri? = null
    companion object {
        const val IMAGE_REQUEST_CODE = 100
        const val CAMERA_REQUEST_CODE = 200
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = null
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById(R.id.slikaKorisnika)
        storageReference = FirebaseStorage.getInstance()

        binding.buttonIzaberiteSliku.setOnClickListener {
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
                TextUtils.isEmpty(binding.editTextPassword.text.toString()) -> {
                    Toast.makeText(requireContext(), "Niste uneli lozinku", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    val korisnickoIme = binding.editTextKorisnickoIme.text.toString()
                    val password = binding.editTextPassword.text.toString()
                    val ime = binding.editTextIme.text.toString()
                    val prezime = binding.editTextPrezime.text.toString()
                    val brojTelefona = binding.editTextBrojTelefona.text.toString()

                    FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(korisnickoIme, password)
                        .addOnCompleteListener(
                            OnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val firebaseUser: FirebaseUser? = task.result?.user
                                    val userID: String? = firebaseUser?.uid
                                    if (userID != null) {
                                        val userReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID)
                                        val imageReferencee = storageReference.getReference("Slike").child(System.currentTimeMillis().toString())
                                        imageReferencee.putFile(url!!)
                                            .addOnCompleteListener { imageUploadTask ->
                                                if (imageUploadTask.isSuccessful) {
                                                    imageReferencee.downloadUrl.addOnSuccessListener { imageUri ->
                                                        val imageUrl = imageUri.toString()
                                                        val user = User(ime,prezime,brojTelefona,imageUrl)
                                                        user.korisnickoIme=korisnickoIme //potrebno je za rangiranje
                                                        userReference.setValue(user)
                                                            .addOnSuccessListener {
                                                                Toast.makeText(requireContext(), "Uspesno ste se registrovali", Toast.LENGTH_SHORT).show()
                                                                val intent = Intent(requireContext(), MainActivity::class.java)
                                                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                                intent.putExtra("user_id", firebaseUser.uid)
                                                                intent.putExtra("email_id", korisnickoIme)
                                                                startActivity(intent)
                                                            }.addOnFailureListener {
                                                                Toast.makeText(requireContext(), "Greska prilikom upisa u bazu", Toast.LENGTH_SHORT).show()
                                                            }
                                                    }
                                                } else {
                                                    Toast.makeText(requireContext(), "Greska prilikom upload-a slike", Toast.LENGTH_SHORT).show()
                                                }
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
        val options = arrayOf<CharSequence>("Izaberi sliku iz galerije", "Slikaj sliku kamerom")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Izaberite opciju")
        builder.setItems(options) { dialog, item ->
            when (item) {
                0 -> {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, IMAGE_REQUEST_CODE)
                }
                1 -> {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
                }
            }
            dialog.dismiss()
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            imageView.setImageURI(data?.data)
            url = data.data!!
        }
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val imageBitmap = data.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
            val tempUrl=saveBitmapAsUrl(imageBitmap)
            url=tempUrl
        }
    }
    private fun saveBitmapAsUrl(bitmap: Bitmap): Uri {
        val tempFile = File(requireContext().cacheDir, "temp_image.jpg")
        tempFile.createNewFile()

        val outStream = FileOutputStream(tempFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
        outStream.flush()
        outStream.close()

        return FileProvider.getUriForFile(
            requireContext(),
            "elfak.mosis.projekat.fileprovider",
            tempFile
        )
    }

}
