package com.sada.petstore.controller

import com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sada.petstore.model.Pet
import com.sada.petstore.model.PetRequest
import com.sada.petstore.model.PetStatus
import com.sada.petstore.model.toPet
import com.sada.petstore.repository.PetRepository
import org.hamcrest.CoreMatchers.equalTo
import org.json.JSONObject
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author benjaminmartinez
 * Date: 19/10/2018
 */
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(PetController::class)
internal class PetControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    private lateinit var json: JacksonTester<PetRequest>

    private lateinit var jsonPet: JacksonTester<Pet>

    @MockBean
    private lateinit var mockPetRepository: PetRepository

    @BeforeAll
    fun setup() {
        JacksonTester.initFields(this, jacksonObjectMapper())
    }


    @Test
    fun `test create a valid pet`() {
        val petRequest = json.readObject("valid-pet.json")

        val petResponse = jsonPet.readObject("valid-pet-result.json")

        given(this.mockPetRepository.savePet(petRequest.toPet()))
                .willReturn(Pet(id = "b28b0d73-af84-4115-aa01-8037b2a8863b",
                        name = petRequest.name,
                        category = petRequest.category,
                        status = PetStatus.AVAILABLE)
                )

        val mvcResult = this.mvc.perform(post("/pets").accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(json.write(petRequest).json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(OpenApiValidationMatchers.openApi().isValid("public/petstore.yml"))
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.status", equalTo("AVAILABLE"))).andReturn()

        JSONAssert.assertEquals(jsonPet.write(petResponse).json , mvcResult.response.contentAsString, JSONCompareMode.STRICT_ORDER)

    }

}