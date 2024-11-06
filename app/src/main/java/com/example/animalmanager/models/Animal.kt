package com.example.animalmanager.models

data class Animal(
    var id: Int = 0,
    var type: String,
    var sexe: String,
    var age: String,
    var nom: String,
    var isFed: Boolean
)