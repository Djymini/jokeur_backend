package com.ashleydev.jokeur_api.e2e;

import com.ashleydev.jokeur_api.e2e.config.TestContainerConfig;
import com.ashleydev.jokeur_api.persistence.repositories.UserRepository;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.measure.MeasureRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("e2e")
public class HealthRecordExportE2ETest extends TestContainerConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @Autowired
    private MeasureRepository measureRepository;

    private String jwt;
    private String healthRecordId;

    @BeforeEach
    void setUp() throws Exception {
        measureRepository.deleteAll();
        healthRecordRepository.deleteAll();
        userRepository.deleteAll();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "ExportUser",
                                    "email": "export@e2e.com",
                                    "password": "P@ssword1234",
                                    "firstname": "Export",
                                    "name": "User"
                                }
                                """))
                .andExpect(status().isOk());

        String authResponse = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "email": "export@e2e.com", "password": "P@ssword1234" }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject authJson = new JSONObject(authResponse);
        jwt = authJson.getString("token");
        String userId = authJson.getString("id");

        String healthRecordResponse = mockMvc.perform(post("/health-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                        .content("""
                                {
                                    "userId": %s,
                                    "petName": "Naya",
                                    "animalType": "CAT",
                                    "breed": "SIAMESE",
                                    "sex": "FEMALE",
                                    "birthDate": "2022-01-01",
                                    "currentWeight": 4.5,
                                    "color": "BLACK",
                                    "identificationNumber": "CHIPE2EEXP01",
                                    "tattoo": null,
                                    "allergy": null
                                }
                                """.formatted(userId)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        healthRecordId = new JSONObject(healthRecordResponse).getString("id");
    }

    @Test
    void shouldExportPdf_andReceiveNonEmptyBinaryResponse() throws Exception {
        mockMvc.perform(post("/health-records/{healthRecordId}/export/pdf", healthRecordId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                        .content("""
                                {
                                    "from": "2020-01-01",
                                    "to": "2030-12-31",
                                    "measureTypes": [],
                                    "includeVaccines": false
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/pdf"))
                .andExpect(header().string("Content-Disposition",
                        org.hamcrest.Matchers.containsString(".pdf")))
                .andExpect(result -> {
                    byte[] body = result.getResponse().getContentAsByteArray();
                    assertTrue(body.length > 0);
                });
    }

    @Test
    void shouldExportXlsx_andReceiveNonEmptyBinaryResponse() throws Exception {
        mockMvc.perform(post("/health-records/{healthRecordId}/export/xlsx", healthRecordId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                        .content("""
                                {
                                    "from": "2020-01-01",
                                    "to": "2030-12-31",
                                    "measureTypes": [],
                                    "includeVaccines": false
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .andExpect(header().string("Content-Disposition",
                        org.hamcrest.Matchers.containsString(".xlsx")))
                .andExpect(result -> {
                    byte[] body = result.getResponse().getContentAsByteArray();
                    assertTrue(body.length > 0);
                });
    }

    @Test
    void shouldExportXlsx_withMeasureTypesSelected() throws Exception {
        mockMvc.perform(post("/health-records/{healthRecordId}/export/xlsx", healthRecordId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                        .content("""
                                {
                                    "from": "2020-01-01",
                                    "to": "2030-12-31",
                                    "measureTypes": ["WEIGHT", "BPM"],
                                    "includeVaccines": false
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .andExpect(result -> {
                    byte[] body = result.getResponse().getContentAsByteArray();
                    assertTrue(body.length > 0);
                });
    }

    @Test
    void shouldReturn4xx_whenExportingUnknownHealthRecord() throws Exception {
        mockMvc.perform(post("/health-records/{healthRecordId}/export/pdf", 999999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                        .content("""
                                {
                                    "from": "2020-01-01",
                                    "to": "2030-12-31",
                                    "measureTypes": [],
                                    "includeVaccines": false
                                }
                                """))
                .andExpect(status().is4xxClientError());
    }
}