package com.example.userregistration.entity;

import com.example.addiction.entity.Addiction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "user_id", length = 6, nullable = false, unique = true)
    private String userId;

    @Column(name = "avatar_id",length = 6, unique = true)
    private String avatarId;

    @Column(name = "username", unique = true, nullable = false)
    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Username must be at least 3 characters")
    private String username;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Addiction> addictions;
    // Constructor tanpa parameter
    public User() {}

    // Constructor dengan parameter
    public User(String userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    // Getter dan Setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Addiction> getAddictions() {
        return addictions;
    }

    public void setAddictions(List<Addiction> addictions) {
        this.addictions = addictions;
    }
}
