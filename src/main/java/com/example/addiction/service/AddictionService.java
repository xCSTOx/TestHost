package com.example.addiction.service;

import com.example.addiction.entity.Addiction;
import com.example.addiction.entity.AddictionId;
import com.example.addiction.repository.AddictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class AddictionService {

    @Autowired
    private AddictionRepository addictionRepository;

    public List<Addiction> getAddictionsByUser(String userId) {
        return addictionRepository.findByUserId(userId);
    }

    public Optional<Addiction> saveAddiction(Addiction addiction) {
        if (addictionRepository.existsById(new AddictionId(addiction.getUserId(), addiction.getAddictionId()))) {
            return Optional.empty(); // Addiction sudah ada
        }
        return Optional.of(addictionRepository.save(addiction));
    }

    public Optional<Addiction> updateAddiction(Addiction addiction) {
        return addictionRepository.findById(new AddictionId(addiction.getUserId(), addiction.getAddictionId()))
                .map(existingAddiction -> {
                    if (addiction.getSaver() != null) {
                        existingAddiction.setSaver(addiction.getSaver());
                    }

                    if (addiction.getMotivation() != null) {
                        existingAddiction.setMotivation(addiction.getMotivation());
                    }
                    return addictionRepository.save(existingAddiction);
                });
    }

    public Optional<Addiction> resetAddiction(Addiction addiction) {
        return addictionRepository.findById(new AddictionId(addiction.getUserId(), addiction.getAddictionId()))
                .map(existingAddiction -> {
                    LocalDate oldStartDate = existingAddiction.getStartDate();
                    LocalDate newStartDate = LocalDate.now();
                    long duration = ChronoUnit.DAYS.between(oldStartDate, newStartDate);
                    existingAddiction.setStreaks(duration);
                    existingAddiction.setStartDate(newStartDate);
                    return addictionRepository.save(existingAddiction);
                });
    }

    public boolean deleteAddiction(Addiction addiction) {
        AddictionId addictionId = new AddictionId(addiction.getUserId(), addiction.getAddictionId());
        if (!addictionRepository.existsById(addictionId)) {
            return false;
        }
        addictionRepository.deleteById(addictionId);
        return true;
    }
}