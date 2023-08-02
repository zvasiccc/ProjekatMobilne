package elfak.mosis.projekat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.navigation.ui.AppBarConfiguration


class ListaMestaFragment : Fragment() {
//OVO JE SECOND FRAGMENT SA VEZBI

    private lateinit var places:ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_mesta, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        places=ArrayList<String>()
        places.add("Tvrdjava")
        places.add("Cair")
        places.add("Park Svetog Save")
        places.add("Trg Kralja Milana")
        val myPlacesList: ListView =requireView().findViewById<ListView>(R.id.my_places_list)
        myPlacesList.adapter=ArrayAdapter<String>(view.context,android.R.layout.simple_list_item_1,places)
        super.onViewCreated(view, savedInstanceState)
    }


}