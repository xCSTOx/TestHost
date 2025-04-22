package com.example.addiction.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addiction_data")
@Data
@NoArgsConstructor
public class AddictionData {

    @Id
    @Column(name = "addiction_id", length = 6, nullable = false, unique = true)
    private String addictionId;

    @Column(name = "addiction_name", unique = true, nullable = false)
    private String addictionName;
}
