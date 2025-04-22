package com.example.addiction.service;

import com.example.addiction.dto.AddictionDTO;
import com.example.addiction.entity.Addiction;
import com.example.addiction.entity.AddictionId;
import com.example.addiction.repository.AddictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddictionService {

    @Autowired
    private AddictionRepository addictionRepository;

    // Konversi entity Addiction ke DTO AddictionDTO
    private AddictionDTO convertToDTO(Addiction addiction) {
        AddictionDTO dto = new AddictionDTO();
        dto.setUserId(addiction.getUserId());
        dto.setAddictionId(addiction.getAddictionId());
        dto.setSaver(addiction.getSaver());
        dto.setStreaks(addiction.getStreaks());
        dto.setMotivation(addiction.getMotivation());
        dto.setStartDate(addiction.getStartDate());

        if (addiction.getAddictionData() != null) {
            dto.setAddictionName(addiction.getAddictionData().getAddictionName()); // misal field `name` di AddictionData
        }

        return dto;
    }

    // Konversi DTO ke entity Addiction
    private Addiction convertToEntity(AddictionDTO dto) {
        Addiction addiction = new Addiction();
        addiction.setUserId(dto.getUserId());
        addiction.setAddictionId(dto.getAddictionId());
        addiction.setSaver(dto.getSaver());
        addiction.setStreaks(dto.getStreaks());
        addiction.setMotivation(dto.getMotivation());
        addiction.setStartDate(dto.getStartDate());
        return addiction;
    }

    // Mendapatkan Addiction berdasarkan UserId dan mengembalikan dalam bentuk AddictionDTO
    public List<AddictionDTO> getAddictionsByUser(String userId) {
        List<Addiction> addictions = addictionRepository.findAddictionsByUserId(userId);
        return addictions.stream()
                .map(this::convertToDTO) // convert Addiction entity ke AddictionDTO
                .collect(Collectors.toList());
    }

    // Menyimpan Addiction baru dan mengembalikan AddictionDTO
    public Optional<AddictionDTO> saveAddiction(AddictionDTO dto) {
        Addiction addiction = convertToEntity(dto); // convert DTO ke Addiction entity
        if (addictionRepository.existsById(new AddictionId(addiction.getUserId(), addiction.getAddictionId()))) {
            return Optional.empty(); // Addiction sudah ada
        }
        Addiction savedAddiction = addictionRepository.save(addiction);
        return Optional.of(convertToDTO(savedAddiction)); // Mengembalikan AddictionDTO
    }

    // Mengupdate Addiction dan mengembalikan AddictionDTO
    public Optional<AddictionDTO> updateAddiction(AddictionDTO dto) {
        Addiction addiction = convertToEntity(dto); // convert DTO ke Addiction entity
        return addictionRepository.findById(new AddictionId(addiction.getUserId(), addiction.getAddictionId()))
                .map(existingAddiction -> {
                    if (addiction.getSaver() != null) {
                        existingAddiction.setSaver(addiction.getSaver());
                    }
                    if (addiction.getMotivation() != null) {
                        existingAddiction.setMotivation(addiction.getMotivation());
                    }
                    Addiction updatedAddiction = addictionRepository.save(existingAddiction);
                    return convertToDTO(updatedAddiction); // Mengembalikan AddictionDTO
                });
    }

    // Mereset Addiction dan mengembalikan AddictionDTO
    public Optional<AddictionDTO> resetAddiction(AddictionDTO dto) {
        Addiction addiction = convertToEntity(dto); // convert DTO ke Addiction entity
        return addictionRepository.findById(new AddictionId(addiction.getUserId(), addiction.getAddictionId()))
                .map(existingAddiction -> {
                    LocalDate oldStartDate = existingAddiction.getStartDate();
                    LocalDate newStartDate = LocalDate.now();
                    long duration = ChronoUnit.DAYS.between(oldStartDate, newStartDate);
                    existingAddiction.setStreaks(duration);
                    existingAddiction.setStartDate(newStartDate);
                    Addiction resetAddiction = addictionRepository.save(existingAddiction);
                    return convertToDTO(resetAddiction); // Mengembalikan AddictionDTO
                });
    }

    // Menghapus Addiction dan mengembalikan status sukses
    public boolean deleteAddiction(AddictionDTO dto) {
        Addiction addiction = convertToEntity(dto); // convert DTO ke Addiction entity
        AddictionId addictionId = new AddictionId(addiction.getUserId(), addiction.getAddictionId());
        if (!addictionRepository.existsById(addictionId)) {
            return false;
        }
        addictionRepository.deleteById(addictionId);
        return true;
    }
}
