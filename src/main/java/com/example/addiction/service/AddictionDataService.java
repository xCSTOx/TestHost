package com.example.addiction.service;

import com.example.addiction.entity.AddictionData;
import com.example.addiction.repository.AddictionDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddictionDataService {

    @Autowired
    private AddictionDataRepository addictionDataRepository;

    public List<AddictionData> getAllAddictions() {
        return addictionDataRepository.findAll();
    }
}

