package elfak.mosis.projekat

data class Restaurant(var ime: String, val opis:String, val longituda:String, val latituda: String)
{
    override fun toString(): String {
        return ime
    }
}
