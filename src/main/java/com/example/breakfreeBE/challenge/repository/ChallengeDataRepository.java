package com.example.breakfreeBE.challenge.repository;

import com.example.breakfreeBE.challenge.entity.ChallengeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChallengeDataRepository extends JpaRepository<ChallengeData, String> {

}
