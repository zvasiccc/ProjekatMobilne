package elfak.mosis.projekat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CustomUserAdapter(context: Context, users: List<User>) :
    ArrayAdapter<User>(context, 0, users) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val user = getItem(position)
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        }
        val textView = convertView?.findViewById<TextView>(android.R.id.text1)
        textView?.text = "${user?.korisnickoIme} - Bodovi: ${user?.bodovi}"
        return convertView!!
    }
}