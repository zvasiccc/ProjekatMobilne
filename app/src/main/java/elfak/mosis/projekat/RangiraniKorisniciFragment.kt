package elfak.mosis.projekat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.firebase.database.*


class RangiraniKorisniciFragment : Fragment() {
    private lateinit var rangiraniKorisniciListView: ListView
    private lateinit var database: FirebaseDatabase
    private val restaurantsViewModel: RestaurantsViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rangirani_korisnici, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rangiraniKorisniciListView=view.findViewById<ListView>(R.id.rangirani_korisnici_list_view)
        database=restaurantsViewModel.database
        val usersRef=database.getReference("Users")
        val listaKorisnika:ArrayList<User> = ArrayList<User>()
        val usersAdapter = CustomUserAdapter(requireContext(),listaKorisnika)
        rangiraniKorisniciListView.adapter = usersAdapter
        usersRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listaKorisnika.clear()
                for(userSnapshot in snapshot.children){
                    val user=userSnapshot.getValue(User::class.java)
                    user?.let{
                        listaKorisnika.add(it)
                    }
                }
                listaKorisnika.sortByDescending { it.bodovi }
                usersAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),"Doslo je do greske",Toast.LENGTH_SHORT).show()

            }

        })


    }

}