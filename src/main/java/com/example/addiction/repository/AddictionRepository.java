package com.example.addiction.repository;

import com.example.addiction.entity.Addiction;
import com.example.addiction.entity.AddictionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddictionRepository extends JpaRepository<Addiction, AddictionId> {
    @Query("SELECT a FROM Addiction a WHERE a.userId = :userId")
    List<Addiction> findAddictionsByUserId(@Param("userId") String userId);


}
