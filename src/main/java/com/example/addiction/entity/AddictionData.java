package com.example.addiction.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "addiction_data")
public class AddictionData {

    @Id
    @Column(name = "addiction_id", length = 6, nullable = false, unique = true)
    private String addictionId;

    @Column(name = "addiction_name", unique = true, nullable = false)
    private String addictionName;

    public String getAddictionId() { return addictionId; }
    public void setAddictionId(String addictionId) { this.addictionId = addictionId; }

    public String getAddictionName() { return addictionName; }
    public void setAddictionName(String addictionName) { this.addictionName = addictionName; }
}
