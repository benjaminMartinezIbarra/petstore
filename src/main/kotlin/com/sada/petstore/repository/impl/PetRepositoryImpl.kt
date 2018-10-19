package com.sada.petstore.repository.impl

import com.google.common.cache.CacheBuilder
import com.sada.petstore.model.Pet
import com.sada.petstore.model.PetCategory
import com.sada.petstore.repository.PetRepository
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author benjaminmartinez
 * Date: 19/10/2018
 */

@Repository
class PetRepositoryImpl : PetRepository {


    private val petCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build<String, Pet>()

    override fun savePet(pet: Pet): Pet {
        pet.id = UUID.randomUUID().toString()
        petCache.put(pet.id, pet)
        return pet
    }

    override fun getPetById(petId: String): Pet? {
    return petCache.getIfPresent(petId)
    }

    override fun getPets(category: PetCategory?): List<Pet> {
    return petCache.asMap().values.toMutableList()
    }
}
