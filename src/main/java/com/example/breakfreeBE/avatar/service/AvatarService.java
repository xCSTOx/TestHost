package com.example.breakfreeBE.avatar.service;

import com.example.breakfreeBE.avatar.entity.Avatar;
import com.example.breakfreeBE.avatar.repository.AvatarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvatarService {
    private final AvatarRepository avatarRepository;

    public List<Avatar> getAllAvatars() {
        return avatarRepository.findAll();
    }
}
