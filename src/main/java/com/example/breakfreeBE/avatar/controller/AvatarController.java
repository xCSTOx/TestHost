package com.example.breakfreeBE.avatar.controller;

import com.example.breakfreeBE.avatar.service.AvatarService;
import com.example.breakfreeBE.avatar.entity.Avatar;
import com.example.breakfreeBE.common.BaseResponse;
import com.example.breakfreeBE.common.MetaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avatars")
@RequiredArgsConstructor
public class AvatarController {
    private final AvatarService avatarService;

    @GetMapping
    public BaseResponse<List<Avatar>> getAllAvatars() {
        List<Avatar> avatars = avatarService.getAllAvatars();
        return new BaseResponse<>(new MetaResponse(true, "Avatars retrieved successfully"), avatars);
    }
}
