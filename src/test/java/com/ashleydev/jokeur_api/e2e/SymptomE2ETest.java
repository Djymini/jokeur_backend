package com.ashleydev.jokeur_api.e2e;

import com.ashleydev.jokeur_api.e2e.config.TestContainerConfig;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomHealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomRepository;
import com.ashleydev.jokeur_api.persistence.repositories.UserRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("e2e")
public class SymptomE2ETest extends TestContainerConfig {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SymptomHealthRecordRepository symptomHealthRecordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SymptomRepository symptomRepository;

    @Test
    void shouldCreateListAndDeleteSymptom() throws Exception {

        symptomHealthRecordRepository.deleteAll();
        symptomRepository.deleteAll();
        userRepository.deleteAll();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username":"John",
                                  "email":"symptom@test.com",
                                  "password":"P@ssword1234",
                                  "firstname":"John",
                                  "name":"Doe"
                                }
                                """))
                .andExpect(status().isOk());

        String loginResponse = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email":"symptom@test.com",
                                  "password":"P@ssword1234"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject loginJson = new JSONObject(loginResponse);
        String jwt = loginJson.getString("token");

        String symptomResponse = mockMvc.perform(post("/symptoms")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                   "name":"Diarrhée"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject symptomJson = new JSONObject(symptomResponse);
        String symptomId = symptomJson.getString("id");

        String listResponse = mockMvc.perform(get("/symptoms")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject paginatedResponse = new JSONObject(listResponse);
        JSONArray symptoms = paginatedResponse.getJSONArray("content");

        assertTrue(symptoms.length() > 0, "La liste des symptômes ne devrait pas être vide");

        boolean found = false;
        for (int i = 0; i < symptoms.length(); i++) {
            JSONObject symptom = symptoms.getJSONObject(i);
            if (symptom.getString("id").equals(symptomId)) {
                found = true;
                assertEquals("Diarrhée", symptom.getString("name"));
                break;
            }
        }
        assertTrue(found, "Le symptôme créé devrait être dans la liste");

        mockMvc.perform(delete("/symptoms/" + symptomId)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNoContent());

        String listResponseAfterDelete = mockMvc.perform(get("/symptoms")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject paginatedResponseAfterDelete = new JSONObject(listResponseAfterDelete);
        JSONArray symptomsAfterDelete = paginatedResponseAfterDelete.getJSONArray("content");

        for (int i = 0; i < symptomsAfterDelete.length(); i++) {
            JSONObject symptom = symptomsAfterDelete.getJSONObject(i);
            assertNotEquals(symptomId, symptom.getString("id"),
                    "Le symptôme supprimé ne devrait plus être dans la liste");
        }
    }
}