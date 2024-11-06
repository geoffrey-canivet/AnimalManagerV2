package com.example.animalmanager.ui.mesAnimaux

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animalmanager.R
import com.example.animalmanager.adaptaters.AnimalAdapter
import com.example.animalmanager.dao.AnimalDAO
import com.example.animalmanager.models.Animal

class FragMesAnimaux : Fragment() {

    private lateinit var animalDAO: AnimalDAO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_frag_mes_animaux, container, false)

        val textViewNbAnim = rootView.findViewById<TextView>(R.id.textViewNbAnim)
        val textViewNbAnimFed = rootView.findViewById<TextView>(R.id.textViewAnimFed)
        val textViewFavorit = rootView.findViewById<TextView>(R.id.textViewFavorit)

        animalDAO = AnimalDAO(requireContext())

        // info animaux
        // nb anim
        animalDAO.openReadable()
        val listAnimal = animalDAO.getAll()
        animalDAO.close()
        textViewNbAnim.text = listAnimal.size.toString()

        // animal favorit
        animalDAO.openReadable()
        val animalFav = animalDAO.animalFav()
        animalDAO.close()
        textViewFavorit.text = "Type le plus fréquent : $animalFav"
        textViewFavorit.text = animalFav

        // nb animaux nourri
        animalDAO.openReadable()
        val nbAnimFed = animalDAO.nbAnimalNourris()
        animalDAO.close()
        textViewNbAnimFed.text = nbAnimFed.toString()

        // afficher la liste
        afficherTousLesAnim(rootView)
        return rootView
    }

    // Récup toute les infos de la db et les affiche
    private fun afficherTousLesAnim(rootView: View) {
        animalDAO.openReadable()
        val listAnimal = animalDAO.getAll()
        animalDAO.close()
        if (listAnimal.isNotEmpty()) {
            afficherListe(rootView, listAnimal)
        } else {
            Log.d("MesAnim", "Aucun Animal trouvé dans la base de données.")
        }
    }

    // RecyclerView
    private fun afficherListe(rootView: View, animals: List<Animal>) {
        val recyclerViewContacts = rootView.findViewById<RecyclerView>(R.id.recyclerAnimal)
        recyclerViewContacts.layoutManager = LinearLayoutManager(requireContext())
        val navController = findNavController()
        val adapter = AnimalAdapter(requireContext(), animals, animalDAO, navController)
        recyclerViewContacts.adapter = adapter
    }
}
