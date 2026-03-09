package com.ashleydev.jokeur_api.e2e;

import com.ashleydev.jokeur_api.e2e.config.TestContainerConfig;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomHealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomRepository;
import com.ashleydev.jokeur_api.persistence.repositories.UserRepository;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("e2e")
public class SymptomHealthRecordE2ETest extends TestContainerConfig {

    @Autowired private MockMvc mockMvc;

    @Autowired private UserRepository userRepository;
    @Autowired private HealthRecordRepository healthRecordRepository;
    @Autowired private SymptomRepository symptomRepository;
    @Autowired private SymptomHealthRecordRepository symptomHealthRecordRepository;

    @Test
    void shouldAddSymptomToHealthRecord() throws Exception {

        symptomHealthRecordRepository.deleteAll();
        healthRecordRepository.deleteAll();
        symptomRepository.deleteAll();
        userRepository.deleteAll();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username":"John",
                                  "email":"test@test.com",
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
                                  "email":"test@test.com",
                                  "password":"P@ssword1234"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject loginObj = new JSONObject(loginResponse);

        String jwt = loginObj.getString("token");
        String userId = loginObj.getString("id");

        String healthRecordPayload = """
                {
                  "userId": %s,
                  "petName":"Felix",
                  "animalType":"CAT",
                  "breed":"SIAMESE",
                  "sex":"FEMALE",
                  "birthDate":"2025-02-27",
                  "currentWeight":4.5,
                  "color":"BROWN",
                  "identificationNumber":"ID12345",
                  "tattoo":"ABC101",
                  "allergy":"Aucune"
                }
                """.formatted(userId);

        String healthRecordResponse = mockMvc.perform(post("/health-records")
                        .header("Authorization","Bearer "+jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(healthRecordPayload))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject recordObj = new JSONObject(healthRecordResponse);

        String healthRecordId = recordObj.getString("id");

        String symptomResponse = mockMvc.perform(post("/symptoms")
                        .header("Authorization","Bearer "+jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                   "name":"Vomissements"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject symptomObj = new JSONObject(symptomResponse);

        String symptomId = symptomObj.getString("id");

        String payload = """
                {
                  "healthRecordId": %s,
                  "symptomId": %s,
                  "observationDate":"2026-03-10",
                  "observation":"Vomissement après repas"
                }
                """.formatted(healthRecordId, symptomId);

        mockMvc.perform(post("/symptom-health-records")
                        .header("Authorization","Bearer "+jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());

        mockMvc.perform(
                        get("/symptom-health-records/health-record/" + healthRecordId)
                                .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isOk());


    }
}