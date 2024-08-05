package org.example.courzelo.repositories;

import org.example.courzelo.models.AttendanceSettings;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AttendanceSettingsRepository extends MongoRepository<AttendanceSettings, String> {
}
