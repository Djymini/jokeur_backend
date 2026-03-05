package com.ashleydev.jokeur_api.e2e;

import com.ashleydev.jokeur_api.e2e.config.TestContainerConfig;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("e2e")
public class UserHealthRecordE2ETest extends TestContainerConfig {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private HealthRecordRepository healthRecordRepository;

    @Test
    void shouldRegisterLoginAndCreateHealthRecord() throws Exception {
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
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject obj = new JSONObject(authResponse);
        String jwt = obj.getString("token");
        String userId = obj.getString("id");
        String jsonPayload = """
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
        """.formatted(userId);

        String healthRecordResponse = mockMvc.perform(post("/health-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject objHealthRecord = new JSONObject(healthRecordResponse);
        String healthRecordId = objHealthRecord.getString("id");

        mockMvc.perform(get("/health-records/" +healthRecordId)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.petName").value("Felix"));
    }
}
