package elfak.mosis.projekat

import java.sql.Date

data class Restaurant(
                    var ime: String="",
                      var opis:String="",
                      val longituda:String="",
                      val latituda: String="")
{
    var key:String=""
    var prosecnaOcena:Double= 0.0
    var brojOcena:Int=0
    var idAutora:String=""
    var datumKreiranja: String="";
    override fun toString(): String {
        return ime
    }
}
