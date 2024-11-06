package com.example.animalmanager.models

data class User(
    val id: Int = 0,
    val photo: String,
    val nom : String,
    val prenom : String,
    val email : String,
    val role : String,
    val password : String,
)
