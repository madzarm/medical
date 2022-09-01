package com.demo.dummyapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiseaseHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "person_id")
    @JsonIgnore
    private Person person;
    private LocalDate dateDiscovered;
    private Long diseaseId;

    public DiseaseHistory(Person person, LocalDate dateDiscovered, Long diseaseId) {
        this.person = person;
        this.dateDiscovered = dateDiscovered;
        this.diseaseId = diseaseId;
    }
}
