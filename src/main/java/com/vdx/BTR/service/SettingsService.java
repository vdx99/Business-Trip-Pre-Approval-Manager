package com.vdx.BTR.service;

import com.vdx.BTR.model.AppSettings;
import com.vdx.BTR.repository.AppSettingsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SettingsService {

    private final AppSettingsRepository settingsRepository;

    public SettingsService(AppSettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    // pobierz jedyne ustawienia (tworzy z defaultem, jeśli brak)
    public AppSettings getSettings() {
        return settingsRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> {
                    AppSettings s = new AppSettings(BigDecimal.valueOf(500)); // default
                    return settingsRepository.save(s);
                });
    }

    public void updateThreshold(BigDecimal newThreshold) {
        AppSettings settings = getSettings();
        settings.setApprovalThreshold(newThreshold);
        settingsRepository.save(settings);
    }
}
