package com.example.addiction.entity;

import com.example.userregistration.entity.User;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@IdClass(AddictionId.class)
public class Addiction {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Id
    @Column(name = "addiction_id")
    private String addictionId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "addiction_id", insertable = false, updatable = false)
    private AddictionData addictionData;

    @Column(name = "saver", nullable = false)
    private Integer saver;

    @Column(name = "streaks")
    private Long streaks;

    @Column(name = "motivation", nullable = false)
    private String motivation;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;


    public Addiction() {}

    public Addiction(String userId, String addictionId, Integer saver, String motivation, LocalDate startDate, LocalDate targetDate) {
        this.userId = userId;
        this.addictionId = addictionId;
        this.saver = saver;
        this.motivation = motivation;
        this.startDate = startDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddictionId() {
        return addictionId;
    }

    public void setAddictionId(String addictionId) {
        this.addictionId = addictionId;
    }

    public Long getStreaks() {
        return streaks;
    }

    public void setStreaks(Long streaks) {
        this.streaks = streaks;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public Integer getSaver() {
        return saver;
    }

    public void setSaver(Integer saver) {
        this.saver = saver;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

}
