package elfak.mosis.projekat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
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
        restaurantsViewModel.filterAdapter = ad
        listaFiltriranihRestorana.onItemClickListener =
            AdapterView.OnItemClickListener { p0, _, p2, _ ->
                val str: Restaurant = p0?.adapter?.getItem(p2) as Restaurant
                restaurantsViewModel.selectedRestaurant = str
                findNavController().navigate(R.id.action_listaFiltriranihRestoranaFragment_to_viewFragment)
            }
    }


}