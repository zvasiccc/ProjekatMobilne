package elfak.mosis.projekat

import androidx.lifecycle.ViewModel

class ProfileViewModel:ViewModel() {
    var bodovi:Int=0
    var filtiraniKorisnici: ArrayList<User> =ArrayList<User>()
}