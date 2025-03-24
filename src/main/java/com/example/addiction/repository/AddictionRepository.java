package com.example.addiction.repository;

import com.example.addiction.entity.Addiction;
import com.example.addiction.entity.AddictionId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AddictionRepository extends JpaRepository<Addiction, AddictionId> {
    List<Addiction> findByUserId(String userId);
}
