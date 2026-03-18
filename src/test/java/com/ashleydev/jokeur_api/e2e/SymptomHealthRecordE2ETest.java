package com.ashleydev.jokeur_api.e2e;

import com.ashleydev.jokeur_api.e2e.config.TestContainerConfig;
import com.ashleydev.jokeur_api.persistence.entities.SymptomEntity;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomHealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomRepository;
import com.ashleydev.jokeur_api.persistence.repositories.UserRepository;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("e2e")
class SymptomHealthRecordE2ETest extends TestContainerConfig {

    @Autowired private MockMvc mockMvc;

    @Autowired private UserRepository userRepository;
    @Autowired private HealthRecordRepository healthRecordRepository;
    @Autowired private SymptomRepository symptomRepository;
    @Autowired private SymptomHealthRecordRepository symptomHealthRecordRepository;

    private static final String BEARER = "Bearer ";
    private String jwt;
    private String userId;
    private String hrId;

    @BeforeEach
    void cleanAndSetup() throws Exception {
        symptomRepository.deleteAll();
        symptomHealthRecordRepository.deleteAll();
        userRepository.deleteAll();
        healthRecordRepository.deleteAll();

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "username": "John",
                                        "email": "test@test.com",
                                        "password": "P@ssword1234",
                                        "firstname": "John",
                                        "name": "Doe"
                                    }
                                """))
                .andExpect(status().isOk());

        String authResponse = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                         { "email": "test@test.com", "password": "P@ssword1234" }
                        """))
                .andReturn().getResponse().getContentAsString();

        JSONObject authObject = new JSONObject(authResponse);
        this.jwt = authObject.getString("token");
        this.userId = authObject.getString("id");

        String hrResponse = mockMvc.perform(post("/health-records")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", BEARER + jwt)
                .content(
                        """
                                         {
                                    "userId": %s,
                                    "petName": "Felix",
                                    "animalType": "CAT",
                                    "breed": "SIAMESE",
                                    "sex": "FEMALE",
                                    "birthDate": "2025-02-27",
                                    "currentWeight": 15.0,
                                    "color": "BROWN",
                                    "identificationNumber": "hin55ykondpoCLI",
                                    "tattoo": "ABC101",
                                    "allergy": "Aucune"
                                }
                                """.formatted(userId)))
                .andReturn().getResponse().getContentAsString();
        this.hrId = new JSONObject(hrResponse).getString("id");
    }
    @Test
    void shouldAddSymptomToHealthRecord() throws Exception{
        SymptomEntity symptom = new SymptomEntity();
        symptom.setName("Vomissements");
        SymptomEntity savedSymptom = symptomRepository.save(symptom);
        String symptomId = savedSymptom.getId().toString();


        String payload = """
                 {
                              "symptomId": %s,
                              "observationDate":"2026-03-10",
                              "observation":"Vomissement après repas"
                            }
                """.formatted(symptomId);

        mockMvc.perform(post("/symptom-health-records/" + hrId)
                .header("Authorization", BEARER + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());

        String response = mockMvc.perform(
                get("/symptom-health-records/" + hrId)
                        .header("Authorization", BEARER + jwt))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONArray symptoms = new JSONArray(response);
        assertTrue(symptoms.length() > 0);

        boolean found = false;

        for (int i = 0; i < symptoms.length(); i++) {

            JSONObject s = symptoms.getJSONObject(i);
            JSONObject symptomObj = s.getJSONObject("symptom");
            if (symptomObj.getLong("id") == Long.parseLong(symptomId)) {
                found = true;
            }
        }
        assertTrue(found);
    }

}