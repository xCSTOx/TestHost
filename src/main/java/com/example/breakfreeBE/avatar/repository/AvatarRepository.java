package com.example.breakfreeBE.avatar.repository;

import com.example.breakfreeBE.avatar.entity.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvatarRepository extends JpaRepository<Avatar, String> {
}
