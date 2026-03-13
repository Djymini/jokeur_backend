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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("e2e")
public class UserTreatmentE2ETest extends TestContainerConfig {
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
    void shouldCreateAndRetrieveVariousTreatments() throws Exception {
        String jwt = this.jwt;
        String hrId = this.hrId;

        mockMvc.perform(post("/treatments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                        .content("""
                                {
                                   "name": "Betadine",
                                   "description": "coupure",
                                   "frequency": "DAILY",
                                   "beginDate": "2026-03-11",
                                   "endDate": null,
                                   "treatmentReminderDate": "2026-03-11T09:30",
                                   "healthRecordId": %s
                                 }
                            """.formatted(hrId)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/treatments/" + hrId).header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Betadine"));
    }

    @Test
    void shouldModifyExistingMeasure() throws Exception {
        String jwt = this.jwt;
        String hrId = this.hrId;

        String treatmentRes = mockMvc.perform(post("/treatments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                        .content("""
                                {
                                   "name": "Betadine",
                                   "description": "coupure",
                                   "frequency": "DAILY",
                                   "beginDate": "2026-03-11",
                                   "endDate": null,
                                   "treatmentReminderDate": "2026-03-11T09:30",
                                   "healthRecordId": %s
                                 }
                            """.formatted(hrId)))
                .andReturn().getResponse().getContentAsString();

        String treatmentId = new JSONObject(treatmentRes).getString("id");
        JSONObject reminder = new JSONObject(treatmentRes).getJSONObject("reminder");
        String reminderId = reminder.getString("id");

        mockMvc.perform(put("/treatments/" + hrId + "/" + treatmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                        .content("""
                                {
                                    "id": %s,
                                    "name": "TreatmentUpdate",
                                    "description": "Treatment2",
                                    "frequency": "MONTHLY",
                                    "beginDate": "2026-03-11",
                                    "endDate": "2026-03-11",
                                    "healthRecordId": %s,
                                    "reminder": {
                                      "id": %s,
                                      "description": "Test",
                                      "reminderDate": "2026-03-03T09:30",
                                      "status": "PENDING"
                                    }
                                  }
                            """.formatted(treatmentId, hrId, reminderId)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteTreatment() throws Exception {
        String jwt = this.jwt;
        String hrId = this.hrId;

        String treatmentRes = mockMvc.perform(post("/treatments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                        .content("""
                                {
                                   "name": "Betadine",
                                   "description": "coupure",
                                   "frequency": "DAILY",
                                   "beginDate": "2026-03-11",
                                   "endDate": null,
                                   "treatmentReminderDate": "2026-03-11T09:30",
                                   "healthRecordId": %s
                                 }
                            """.formatted(hrId)))
                .andReturn().getResponse().getContentAsString();

        String treatmentId = new JSONObject(treatmentRes).getString("id");

        mockMvc.perform(delete("/treatments/" + hrId + "/" + treatmentId)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNoContent());
    }
}
