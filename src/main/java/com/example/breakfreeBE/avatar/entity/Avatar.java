package com.example.breakfreeBE.avatar.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "avatar")
@Data
public class Avatar {
    @Id
    private String avatarId;
    private String avatarUrl;
}
