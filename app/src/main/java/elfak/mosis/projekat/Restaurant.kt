package elfak.mosis.projekat

data class Restaurant(
                    var ime: String="",
                      var opis:String="",
                      val longituda:String="",
                      val latituda: String="")
{
    var key:String=""
    var prosecnaOcena:Int=0
    var brojOcena:Int=0
    var idAutora:String=""
    override fun toString(): String {
        return ime
    }
}
