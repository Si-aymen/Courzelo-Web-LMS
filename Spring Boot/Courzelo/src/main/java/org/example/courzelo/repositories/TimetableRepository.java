package org.example.courzelo.repositories;

import org.example.courzelo.dto.ChromosomeDTO;
import org.example.courzelo.dto.GeneDTO;
import org.example.courzelo.dto.TimeslotDTO;
import org.example.courzelo.models.Period;
import org.example.courzelo.models.Timetable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.*;
@Repository
public interface TimetableRepository extends MongoRepository<Timetable, String> {

}
