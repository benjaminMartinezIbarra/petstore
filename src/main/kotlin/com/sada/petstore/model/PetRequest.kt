package com.sada.petstore.model

data class PetRequest(val name: String, val category: PetCategory)

fun PetRequest.toPet(): Pet {
    return Pet(id = "",
            name = this.name,
            category = this.category,
            status = PetStatus.AVAILABLE)
}