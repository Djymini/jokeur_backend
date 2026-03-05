package com.ashleydev.jokeur_api.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "symptom")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SymptomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "symptom", fetch = FetchType.LAZY)
    private List<SymptomHealthRecordEntity> healthRecords = new ArrayList<>();
}
