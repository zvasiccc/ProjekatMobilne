package elfak.mosis.projekat

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration


class ListaMestaFragment : Fragment() {
//OVO JE SECOND FRAGMENT SA VEZBI

    private lateinit var places:ArrayList<Restaurant>
    private val restaurantsViewModel: RestaurantsViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_mesta, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val myPlacesList: ListView =requireView().findViewById<ListView>(R.id.my_places_list)
        myPlacesList.adapter=ArrayAdapter<Restaurant>(view.context,android.R.layout.simple_list_item_1,restaurantsViewModel.sviRestorani)
        //prikazuje listu mojih stringova sa list view koji se prikazuju korisniku
        myPlacesList.onItemClickListener =
            AdapterView.OnItemClickListener { p0, _, p2, _ ->
                val str: Restaurant = p0?.adapter?.getItem(p2) as Restaurant
                restaurantsViewModel.selectedRestaurant = str
                findNavController().navigate(R.id.action_listaMestaFragment_to_viewFragment)
            }
        myPlacesList.setOnCreateContextMenuListener(object:View.OnCreateContextMenuListener{
            override fun onCreateContextMenu(menu: ContextMenu, v:View?,menuInfo: ContextMenu.ContextMenuInfo){
                val info=menuInfo as AdapterContextMenuInfo
                val restoran:Restaurant=restaurantsViewModel.sviRestorani[info.position]
                menu.add(0,1,1," Pregledaj restoran")
                menu.add(0,2,2,"Izmeni restoran")
                menu.add(0,3,3," Izbrisi ")

            }
        })
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info=item.menuInfo as AdapterContextMenuInfo
        if(item.itemId===1){
            restaurantsViewModel.selectedRestaurant=restaurantsViewModel.sviRestorani[info.position]
            this.findNavController().navigate(R.id.action_listaMestaFragment_to_viewFragment)
        }
        if(item.itemId===2){
            restaurantsViewModel.selectedRestaurant=restaurantsViewModel.sviRestorani[info.position]
            this.findNavController().navigate(R.id.action_listaMestaFragment_to_EditFragment)
        }
        if(item.itemId===3){
            Toast.makeText(this.context,"Brisem izabrani restoran",Toast.LENGTH_SHORT).show()
        }

        return super.onContextItemSelected(item)
    }


}