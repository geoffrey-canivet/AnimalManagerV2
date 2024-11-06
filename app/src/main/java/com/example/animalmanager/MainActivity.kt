package com.example.animalmanager

import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.animalmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // acceder aux element xml de la vue
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Menu top
        setSupportActionBar(binding.appBarMain.toolbar)

        // Donnée transmise de Login
        val nom = intent.getStringExtra("nom")
        val prenom = intent.getStringExtra("prenom")
        val role = intent.getStringExtra("role")
        val photo = intent.getStringExtra("photo")

        // info animal
        val imageViewInfo = findViewById<ImageView>(R.id.imageViewMesAnim)
        photoUser(photo.toString(), imageViewInfo)

        // Menu gauche
        val textViewNom = binding.navView.getHeaderView(0).findViewById<TextView>(R.id.textViewMenuNom)
        textViewNom.text = "$nom $prenom"
        val textViewRole = binding.navView.getHeaderView(0).findViewById<TextView>(R.id.textViewMenuRole)
        textViewRole.text = role
        val imageViewMenu = binding.navView.getHeaderView(0).findViewById<ImageView>(R.id.imageViewHeader)
        photoUser(photo.toString(), imageViewMenu)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
               R.id.nav_profil, R.id.nav_mes_animaux
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    // Menu top
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun photoUser(photo: String, imageView: ImageView) {
        when (photo) {
            "Style 1" -> imageView.setImageResource(R.drawable.user1)
            "Style 2" -> imageView.setImageResource(R.drawable.user2)
            "Style 3" -> imageView.setImageResource(R.drawable.user3)
            "Style 4" -> imageView.setImageResource(R.drawable.user4)
            else -> imageView.setImageResource(R.drawable.ajouter)
        }
    }


}