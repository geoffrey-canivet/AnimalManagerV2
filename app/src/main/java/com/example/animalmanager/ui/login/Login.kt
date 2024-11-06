package com.example.animalmanager.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.animalmanager.Inscription
import com.example.animalmanager.MainActivity
import com.example.animalmanager.R
import com.example.animalmanager.dao.UserDAO
import com.example.animalmanager.models.User

class Login : AppCompatActivity() {

    private lateinit var userDAO: UserDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val mail = findViewById<EditText>(R.id.mail)
        val password = findViewById<EditText>(R.id.password)
        val btnConnexion = findViewById<Button>(R.id.btnConnexion)
        val btnInscription = findViewById<TextView>(R.id.BtnInscription)

        btnConnexion.setOnClickListener {
            verifUser(mail.text.toString(), password.text.toString())
        }

        btnInscription.setOnClickListener {
            val intent = Intent(this@Login, Inscription::class.java)
            startActivity(intent)
        }

        userDAO = UserDAO(this)
        logUsers()
    }

    private fun verifUser(email: String, password: String) {
        userDAO.openReadable()
        val users = userDAO.getAll()

        var user: User? = null
        for (u in users) {
            if (u.email == email && u.password == password) {
                user = u
                break
            }
        }

        if (user != null) {
            Log.d("Login", "Connexion réussie")
            // garder les infos pour menu gauche de mainActivity
            val intent = Intent(this@Login, MainActivity::class.java).apply {
                putExtra("nom", user.nom)
                putExtra("prenom", user.prenom)
                putExtra("role", user.role)
                putExtra("photo", user.photo)
            }
            startActivity(intent)

        } else {
            Toast.makeText(this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show()
        }
        userDAO.close()
    }

    // debug
    private fun logUsers() {
        userDAO.openReadable()
        val users = userDAO.getAll()
        if (users.isEmpty()) {
            Log.d("Liste utilisateurs:", "Aucun utilisateur trouvé.")
        } else {
            users.forEach { user ->
                Log.d("Liste utilisateurs:", "mail: ${user.email} pass: ${user.password} nom: ${user.nom} prenom: ${user.prenom} role: ${user.role} photo: ${user.photo}")
            }
        }
        userDAO.close()
    }
}
