package com.ashleydev.jokeur_api.e2e;

import com.ashleydev.jokeur_api.e2e.config.TestContainerConfig;
import com.ashleydev.jokeur_api.persistence.repositories.UserRepository;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("e2e")
public class UserVaccinE2ETest extends TestContainerConfig {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private HealthRecordRepository healthRecordRepository;

    private String jwt;
    private String hrId;

    @BeforeEach
    void cleanAndSetup() throws Exception {
        healthRecordRepository.deleteAll();
        userRepository.deleteAll();

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

        JSONObject authObj = new JSONObject(authResponse);
        this.jwt = authObj.getString("token");
        String userId = authObj.getString("id");

        String hrResponse = mockMvc.perform(post("/health-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                        .content("""
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
    void shouldCreateAndRetrieveVariousVaccines() throws Exception {
        String jwt = this.jwt;
        String hrId = this.hrId;

        mockMvc.perform(post("/vaccines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                        .content("""
                                {
                                   "name": "Pfizer",
                                   "description": "Contre la rage",
                                   "vaccinator": "Dr Mattmoissat",
                                   "vaccineDate": "2026-03-03",
                                   "vaccineReminderDate": "2026-03-03T09:30",
                                   "healthRecordId": %s
                                 }
                            """.formatted(hrId)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/vaccines/" + hrId).header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pfizer"));
    }

    @Test
    void shouldModifyExistingVaccine() throws Exception {
        String jwt = this.jwt;
        String hrId = this.hrId;

        String vaccineRes = mockMvc.perform(post("/vaccines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                        .content("""
                                {
                                   "name": "Pfizer",
                                   "description": "Contre la rage",
                                   "vaccinator": "Dr Mattmoissat",
                                   "vaccineDate": "2026-03-03",
                                   "vaccineReminderDate": "2026-03-03T09:30",
                                   "healthRecordId": %s
                                 }
                            """.formatted(hrId)))
                .andReturn().getResponse().getContentAsString();

        String vaccineId = new JSONObject(vaccineRes).getString("id");
        JSONObject reminder = new JSONObject(vaccineRes).getJSONObject("reminder");
        String reminderId = reminder.getString("id");

        mockMvc.perform(put("/vaccines/" + hrId + "/" + vaccineId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                        .content("""
                                {
                                    "id": %s,
                                    "name": "VaccineUpdate",
                                    "description": "Vaccine2",
                                    "vaccinator": "Dr Update",
                                    "vaccineDate": "2026-03-03",
                                    "healthRecordId": %s,
                                    "reminder": {
                                      "id": %s,
                                      "description": "Test",
                                      "reminderDate": "2026-03-03T09:30",
                                      "status": "PENDING"
                                    }
                                  }
                            """.formatted(vaccineId, hrId, reminderId)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteVaccine() throws Exception {
        String jwt = this.jwt;
        String hrId = this.hrId;

        String vaccineRes = mockMvc.perform(post("/vaccines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                        .content("""
                                {
                                   "name": "Pfizer",
                                   "description": "Contre la rage",
                                   "vaccinator": "Dr Mattmoissat",
                                   "vaccineDate": "2026-03-03",
                                   "vaccineReminderDate": "2026-03-03T09:30",
                                   "healthRecordId": %s
                                 }
                            """.formatted(hrId)))
                .andReturn().getResponse().getContentAsString();

        String vaccineId = new JSONObject(vaccineRes).getString("id");

        mockMvc.perform(delete("/vaccines/" + hrId + "/" + vaccineId)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNoContent());
    }
}
