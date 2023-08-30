package elfak.mosis.projekat

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController


class ListaFiltriranihRestoranaFragment : Fragment() {
    private lateinit var listaFiltriranihRestorana:ListView
    private val restaurantsViewModel: RestaurantsViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.title = null
        return inflater.inflate(R.layout.fragment_lista_filtriranih_restorana, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listaFiltriranihRestorana=view.findViewById<ListView>(R.id.filtirani_restorani_list)
        val ad= ArrayAdapter<Restaurant>(
            view.context,
            android.R.layout.simple_list_item_1,
            restaurantsViewModel.filtiraniRestorani
        )
        //prvo je ovde prazna lista, dok mu ne stigne notify data changed(),
        //nakon toga on azurira prikaz i prikazuju se odgovaracuji restorani koje smo
        //sada stavili u restaurantsViewModel.filtiraniRestorani
        listaFiltriranihRestorana.adapter = ad
        restaurantsViewModel.filterAdapter = ad //ovde cekamo obavestenje
        listaFiltriranihRestorana.onItemClickListener =
            AdapterView.OnItemClickListener { p0, _, p2, _ ->
                val str: Restaurant = p0?.adapter?.getItem(p2) as Restaurant
                restaurantsViewModel.selectedRestaurant = str
                findNavController().navigate(R.id.action_listaFiltriranihRestoranaFragment_to_viewFragment)
            }
        listaFiltriranihRestorana.setOnCreateContextMenuListener(object:View.OnCreateContextMenuListener{
            override fun onCreateContextMenu(menu: ContextMenu, v:View?, menuInfo: ContextMenu.ContextMenuInfo){
                val info=menuInfo as AdapterView.AdapterContextMenuInfo
                val restoran:Restaurant=restaurantsViewModel.sviRestorani[info.position]
                menu.add(0,1,1," Pregledaj restoran")
                menu.add(0,2,2,"Izmeni restoran")
                menu.add(0,3,3," Izbrisi ")
                menu.add(0,4,4," Oceni restoran")
            }
        })
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info=item.menuInfo as AdapterView.AdapterContextMenuInfo
        if(item.itemId===1){
            restaurantsViewModel.selectedRestaurant=restaurantsViewModel.filtiraniRestorani[info.position]
            this.findNavController().navigate(R.id.action_listaFiltriranihRestoranaFragment_to_viewFragment)
        }
        if(item.itemId===2){
            restaurantsViewModel.selectedRestaurant=restaurantsViewModel.filtiraniRestorani[info.position]
            this.findNavController().navigate(R.id.action_listaFiltriranihRestoranaFragment_to_EditFragment)
        }
        if(item.itemId===3){
            Toast.makeText(this.context,"Brisem izabrani restoran", Toast.LENGTH_SHORT).show()
            val selectedRestaurant=restaurantsViewModel.filtiraniRestorani[info.position]
            restaurantsViewModel.sviRestorani.remove(selectedRestaurant)
            this.findNavController().navigate(R.id.action_listaFiltriranihRestoranaFragment_self)
        }
        if(item.itemId===4){
            restaurantsViewModel.selectedRestaurant=restaurantsViewModel.filtiraniRestorani[info.position]
            this.findNavController().navigate(R.id.action_listaFiltriranihRestoranaFragment_to_oceniRestoranFragment)
        }

        return super.onContextItemSelected(item)
    }

}