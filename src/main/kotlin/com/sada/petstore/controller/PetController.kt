package com.sada.petstore.controller

import com.sada.petstore.model.Pet
import com.sada.petstore.model.PetRequest
import com.sada.petstore.model.toPet
import com.sada.petstore.repository.PetRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

/**
 * @author benjaminmartinez
 * Date: 19/10/2018
 */

@RestController

@RequestMapping("/pets")
class PetController(private val petRepository: PetRepository) {
    @PostMapping
    fun createPet(@RequestBody petRequest: PetRequest): ResponseEntity<Pet> {
        val pet = petRepository.savePet(petRequest.toPet())
        return ResponseEntity.created(URI.create("/pets/${pet.id}")).body(pet)
    }

    @GetMapping("/{petId}")
    fun getPetById(@PathVariable petId: String): ResponseEntity<Pet?> {
        return petRepository.getPetById(petId)?.let{
            ResponseEntity.ok(it)
        }?: ResponseEntity.notFound().build()
    }
}