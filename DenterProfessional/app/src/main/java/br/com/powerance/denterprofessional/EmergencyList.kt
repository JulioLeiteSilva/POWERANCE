package br.com.powerance.denterprofessional


fun emergenciesList(): List<Emergency>{
    return listOf(
        Emergency("Messi careca", "(19)1111-2222",R.drawable.messi_careca,"Nova","O Messi está careca 1","1111"),
        Emergency("Russin do Molejo", "(19)1211-2222",R.drawable.messi_careca,"Antiga","O Messi está careca 2","2222"),
        Emergency("Messi Bold", "(19)1131-2222",R.drawable.messi_careca,"Nova","O Messi está careca 3","3333"),
        Emergency("Messi Sem Cabelo", "(19)1114-2222",R.drawable.messi_careca,"Nova","O Messi está careca 4","4444")
    )
}
