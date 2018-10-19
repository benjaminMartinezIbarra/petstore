package com.sada.petstore.repository

import com.sada.petstore.model.Pet
import com.sada.petstore.model.PetCategory

interface PetRepository {

    fun savePet(pet: Pet): Pet

    fun getPetById(petId: String): Pet?

    fun getPets(category: PetCategory?): List<Pet>
}
