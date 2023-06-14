package br.com.powerance.denterprofessional

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReviewAdapter (private val listReview: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context)
           .inflate(R.layout.reputation_review_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = listReview[position]
        holder.bind(review)
    }

    override fun getItemCount(): Int {
        return listReview.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val textViewNome: TextView = itemView.findViewById(R.id.tvNomeReview)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.rbReview)
        private val textViewComentario: TextView = itemView.findViewById(R.id.tvComentarioReview)

        fun bind(review: Review){
            textViewNome.text = review.nome
            ratingBar.rating = review.classificacao
            textViewComentario.text = review.comentario
        }
    }
}