package br.com.powerance.denterprofessional

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore


class ReputationFragment : Fragment() {

    private lateinit var recyclerViewReviews: RecyclerView

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

        collectionReference.get().addOnSuccessListener { querySnapshot ->
            val listReview = mutableListOf<Review>()

            for (document in querySnapshot) {
                val nome = document.getString("nome")
                val classificacao = document.getDouble("classificacao")?.toFloat()
                val comentario = document.getString("comentario")

                if(nome!= null && classificacao != null && comentario != null){
                    val review = Review(nome, classificacao, comentario)
                    listReview.add(review)
                }
            }
            callback(listReview)
        }.addOnFailureListener {exception ->
            callback(emptyList())
        }

    }

}