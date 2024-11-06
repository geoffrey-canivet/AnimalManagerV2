package com.example.animalmanager.ui.ficheAnimal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.animalmanager.R
import com.example.animalmanager.dao.AnimalDAO

class FicheAnimal : Fragment() {
    private lateinit var animalDAO: AnimalDAO
    private var animalId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fiche_animal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sexe = view.findViewById<EditText>(R.id.prenomUser)
        val nom = view.findViewById<EditText>(R.id.nomUser)
        val age = view.findViewById<EditText>(R.id.telUser)
        val imgAnimal = view.findViewById<ImageView>(R.id.imgFicheAnimal)
        val btnRetour = view.findViewById<Button>(R.id.btnRetour)
        val btnModifier = view.findViewById<Button>(R.id.btnModifierAnimal)

        animalDAO = AnimalDAO(requireContext())

        // Bouton de retour
        btnRetour.setOnClickListener {
            findNavController().navigate(R.id.action_nav_fiche_animal_to_nav_mes_animaux)
        }

        // récupérer l'ID
        animalId = arguments?.getInt("animalId")

        // si id est non null
        animalId?.let { id ->
            animalDAO.openReadable()
            val animal = animalDAO.getById(id.toLong())

            if (animal != null) {
                Log.d("FicheAnimal", "Animal trouvé : ${animal.nom}, Age: ${animal.age}, Type: ${animal.type}")

                // afficher les info
                sexe.setText(animal.sexe)
                nom.setText(animal.nom)
                age.setText(animal.age)
                when (animal.type) {
                    "Lion" -> imgAnimal.setImageResource(R.drawable.lion)
                    "Crocodile" -> imgAnimal.setImageResource(R.drawable.crocodile)
                    "Koala" -> imgAnimal.setImageResource(R.drawable.koala)
                    "Tigre" -> imgAnimal.setImageResource(R.drawable.tigre)
                    else -> imgAnimal.setImageResource(R.drawable.ajouter)
                }

                // Mettre à jour l'animal
                btnModifier.setOnClickListener {
                    animal.sexe = sexe.text.toString()
                    animal.nom = nom.text.toString()
                    animal.age = age.text.toString()

                    animalDAO.openWritable()
                    val result = animalDAO.update(animal)

                    if (result > 0) {
                        Toast.makeText(requireContext(), "Animal mis à jour avec succès", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_nav_fiche_animal_to_nav_mes_animaux)
                    } else {
                        Toast.makeText(requireContext(), "Erreur lors de la mise à jour de l'animal", Toast.LENGTH_SHORT).show()
                    }
                    animalDAO.close()
                }
            } else {
                Log.e("FicheAnimal", "Aucun animal trouvé avec l'ID $id")
                animalDAO.close()
            }
        }
    }

}