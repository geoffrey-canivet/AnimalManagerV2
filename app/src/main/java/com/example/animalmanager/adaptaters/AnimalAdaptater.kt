package com.example.animalmanager.adaptaters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.animalmanager.R
import com.example.animalmanager.dao.AnimalDAO
import com.example.animalmanager.models.Animal


class AnimalAdapter(
    private val context: Context,
    private val animaux: List<Animal>,
    private val animalDAO: AnimalDAO,
    private val navController: NavController
) : RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>(){

    // ViewHolder / Recup items
    inner class AnimalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeTextView: TextView = itemView.findViewById(R.id.cardAnimType)
        val nomTextView: TextView = itemView.findViewById(R.id.cardAnimNom)
        val isfed: TextView = itemView.findViewById(R.id.cardAnimIsFed)
        val btnSupprimer: ImageView = itemView.findViewById(R.id.icoSupprimer)
        val btnInfo: ImageView = itemView.findViewById(R.id.imageViewInfo)
        val imgType: ImageView = itemView.findViewById(R.id.imageViewHeader)
    }

    // Création d'un item / Ajoute au xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_animal, parent, false)
        return AnimalViewHolder(view)
    }

    // Gestion des items
    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = animaux[position]

        // card
        holder.typeTextView.text = animal.type
        holder.nomTextView.text = animal.nom
        holder.isfed.text = animal.isFed.toString()
        when (animal.type) {
            "Lion" -> holder.imgType.setImageResource(R.drawable.lion)
            "Crocodile" -> holder.imgType.setImageResource(R.drawable.crocodile)
            "Koala" -> holder.imgType.setImageResource(R.drawable.koala)
            "Tigre" -> holder.imgType.setImageResource(R.drawable.tigre)
        }

        // Supprimer un animal
        holder.btnSupprimer.setOnClickListener {
            val idAnimal = animal.id
            animalDAO.openWritable() // Ouvrir la base de données en mode écriture
            animalDAO.deleteAnimal(idAnimal) // Supprimer l'animal de la base de données
            animalDAO.close() // Fermer la base de données

            // Rafraîchir la liste
            (animaux as MutableList).removeAt(position) // Retirer l'animal de la liste
            notifyItemRemoved(position) // Notifier l'adaptateur de la suppression
            notifyItemRangeChanged(position, animaux.size) // Mettre à jour la position des items
            Toast.makeText(context, "Animal supprimé avec succès", Toast.LENGTH_SHORT).show() // Message de confirmation

            // BUG ---- raffraichir fragment pour maj info animal - perd info user
//            val navController = Navigation.findNavController(holder.itemView)
//            navController.navigate(R.id.nav_mes_animaux)
        }

        // Afficher détails animal
        holder.btnInfo.setOnClickListener {
            // Créer un Bundle pour stocker l'id de l'animal
            val bundle = Bundle().apply {
                putInt("animalId", animal.id) // Remplacez 'animal.id' par la variable correspondante dans votre modèle
            }

            // Naviguez vers FicheAnimal via nav
            val navController = Navigation.findNavController(it)
            navController.navigate(R.id.action_nav_mes_animaux_to_nav_fiche_animal, bundle)
        }

    }

    override fun getItemCount() = animaux.size
}