package br.com.powerance.denterprofessional

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.powerance.denterprofessional.datastore.Payload
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder


class ReputationFragment : Fragment() {

    private lateinit var recyclerViewReviews: RecyclerView
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reputation, container, false)

        recyclerViewReviews = view.findViewById(R.id.recyclerViewReviews)
        recyclerViewReviews.layoutManager = LinearLayoutManager(requireContext())

        getListOfReviews { listReviews ->
            val adapter = ReviewAdapter(listReviews)
            recyclerViewReviews.adapter = adapter
        }

        return view
    }

    private fun getListOfReviews(callback: (List<Review>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val collectionReference = db.collection("reviews")
//        val dataProfile = (activity as MenuActivity).dataProfile
//        val profile = gson.fromJson((dataProfile.result?.payload as String), Payload::class.java)
        auth = Firebase.auth
        val user = auth.currentUser
        val uidUser = user!!.uid
        collectionReference.get().addOnSuccessListener { querySnapshot ->
            val listReview = mutableListOf<Review>()

            for (document in querySnapshot) {
                val uid = document.getString("uid")
                if( uid == uidUser){
                    val nome = document.getString("nome")
                    val classificacao = document.getString("classificacao")?.toFloat()
                    val comentario = document.getString("comentario")

                    if(nome!= null && classificacao != null && comentario != null){
                        val review = Review(nome, classificacao, comentario)
                        listReview.add(review)
                    }
                }
            }
            callback(listReview)
        }.addOnFailureListener {exception ->
            callback(emptyList())
        }

    }

}