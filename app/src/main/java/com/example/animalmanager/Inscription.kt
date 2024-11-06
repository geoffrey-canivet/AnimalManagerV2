package com.example.animalmanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.animalmanager.dao.UserDAO
import com.example.animalmanager.models.User
import com.example.animalmanager.ui.login.Login

class Inscription : AppCompatActivity() {

    private lateinit var userDAO: UserDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscription)

        val editNom = findViewById<EditText>(R.id.nomUser)
        val editPrenom = findViewById<EditText>(R.id.prenomUser)
        val editEmail = findViewById<EditText>(R.id.mailUser)
        val editPassword1 = findViewById<EditText>(R.id.passwordUser1)
        val editPassword2 = findViewById<EditText>(R.id.passwordUser2)
        val spinnerUserPhoto: Spinner = findViewById(R.id.spinnerAvat)
        val spinnerUserRole: Spinner = findViewById(R.id.spinnerRole)
        val btnInscription = findViewById<Button>(R.id.btnModifierAnimal)


        userDAO = UserDAO(this)

        // spinner photo
        val optionPhoto = listOf("Style 1", "Style 2", "Style 3", "Style 4")
        val adapterPhoto = ArrayAdapter(this@Inscription, android.R.layout.simple_spinner_item, optionPhoto)
        adapterPhoto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUserPhoto.adapter = adapterPhoto

        val imgUser: ImageView = findViewById(R.id.imgFicheAnimal)
        spinnerUserPhoto.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                gestionImg(position, imgUser)
                val spinPhoto = optionPhoto[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Ne rien faire
            }
        }

        // Spinner Role
        val optionRole = listOf("Soigneur", "Superviseur")
        val adapterRole = ArrayAdapter(this@Inscription, android.R.layout.simple_spinner_item, optionRole)
        adapterPhoto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUserRole.adapter = adapterRole




        // Ajouter user
        btnInscription.setOnClickListener {
            val nom = editNom.text.toString()
            val prenom = editPrenom.text.toString()
            val email = editEmail.text.toString()
            val password1 = editPassword1.text.toString()
            val password2 = editPassword2.text.toString()
            val photo = spinnerUserPhoto.selectedItem.toString()
            val role = spinnerUserRole.selectedItem.toString()

            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
                Toast.makeText(this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password1 != password2) {
                Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newUser = User(
                id = 0,
                nom = nom,
                prenom = prenom,
                email = email,
                password = password1,
                photo = photo,
                role = role
            )

            userDAO.openWritable()
            userDAO.insert(newUser)
            userDAO.close()
            Toast.makeText(this, "Inscription réussie", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
    // Gestion de l'image de l'animal
    private fun gestionImg(position: Int, img: ImageView) {
        when (position) {
            0 -> img.setImageResource(R.drawable.user1)
            1 -> img.setImageResource(R.drawable.user2)
            2 -> img.setImageResource(R.drawable.user3)
            3 -> img.setImageResource(R.drawable.user4)
            else -> img.setImageResource(R.drawable.ajouter)
        }
    }
}
