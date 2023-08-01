package elfak.mosis.projekat

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView


class MapFragment : Fragment() {
    lateinit var map:MapView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var ctx: Context?=getActivity()?.getApplicationContext()
        Configuration.getInstance().load(ctx,PreferenceManager.getDefaultSharedPreferences((ctx!!)))
        //map=requireView().findViewById<MapView>(R.id.map)
    }
    override fun onResume(){
        super.onResume()
        map.onResume()
    }
    override fun onPause(){
        super.onPause()
        map.onPause()
    }

}


