package com.example.animalmanager.ui.ajouterAnimaux

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController // Import pour la navigation
import com.example.animalmanager.R
import com.example.animalmanager.dao.AnimalDAO
import com.example.animalmanager.models.Animal

class AjouterAnimaux : Fragment() {

    private lateinit var animalDAO: AnimalDAO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ajouter_animaux, container, false)

        // Configuration du Spinner
        val spinnerType: Spinner = view.findViewById(R.id.spinnerAvat)
        val options = listOf("Lion", "Tigre", "Koala", "Crocodile")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerType.adapter = adapter

        val imgAnimal: ImageView = view.findViewById(R.id.imgAnimal)
        spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                gestionImg(position, imgAnimal)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        // Ajouter un animal
        animalDAO = AnimalDAO(requireContext())
        val btnAjouter = view.findViewById<Button>(R.id.btnModifierAnimal)
        btnAjouter.setOnClickListener {
            val type = spinnerType.selectedItem.toString()
            val sexe = view.findViewById<EditText>(R.id.prenomUser).text.toString()
            val nom = view.findViewById<EditText>(R.id.nomUser).text.toString()
            val age = view.findViewById<EditText>(R.id.telUser).text.toString()
            val isFed = view.findViewById<CheckBox>(R.id.checkIsFed).isChecked

            if (sexe.isNotEmpty() && nom.isNotEmpty() && age.isNotEmpty()) {
                val newAnimal = Animal(
                    sexe = sexe,
                    type = type,
                    nom = nom,
                    age = age,
                    isFed = isFed
                )

                animalDAO.openWritable()
                animalDAO.insert(newAnimal)
                animalDAO.close()

                Toast.makeText(requireContext(), "Animal ajouté avec succès", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }

        // Bouton retour via navigation
        val btnRetour = view.findViewById<Button>(R.id.btnRetour)
        btnRetour.setOnClickListener {
            findNavController().navigate(R.id.action_nav_ajouter_des_animaux_to_nav_mes_animaux)

        }

        return view
    }

    // Gestion de l'image de l'animal
    private fun gestionImg(position: Int, img: ImageView) {
        when (position) {
            0 -> img.setImageResource(R.drawable.lion)
            1 -> img.setImageResource(R.drawable.tigre)
            2 -> img.setImageResource(R.drawable.koala)
            3 -> img.setImageResource(R.drawable.crocodile)
            else -> img.setImageResource(R.drawable.ajouter)
        }
    }

    // Afficher db Animal
    private fun afficherTousLesAnim() {
        animalDAO.openReadable()
        val utilisateurs = animalDAO.getAll()
        animalDAO.close()

        if (utilisateurs.isNotEmpty()) {
            utilisateurs.forEach { animal ->
                Log.d("Utilisateur", "id: ${animal.id}, type: ${animal.type}, sexe: ${animal.sexe}, nom: ${animal.nom}, age: ${animal.age}, isFed: ${animal.isFed}")
            }
        } else {
            Log.d("Utilisateur", "Aucun utilisateur trouvé dans la base de données.")
        }
    }

}
