package mx.ipn.escom.guzmanr.p3

data class Record(
    val RecordID: Int? = null,
    val KidID: Int?,
    val Date: String? = null,
    val EnterTime: String?,
    val ExitTime: String?,
    val MealCount: Int?= null,
    val BathroomCount: Int?= null,
    val Menu: String?,
    val Comment: String?,
    val MealInfo: String?,
    val ChangeClothes: Int?,
    val Evacuations: Int?,
    val Urinations: Int?,
    val ClassIncident: String?,
    val MedicIncident: String?
)
