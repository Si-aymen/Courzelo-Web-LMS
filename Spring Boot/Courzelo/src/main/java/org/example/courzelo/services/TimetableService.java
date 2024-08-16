package org.example.courzelo.services;

import org.example.courzelo.dto.ChromosomeDTO;
import org.example.courzelo.dto.GeneDTO;
import org.example.courzelo.dto.TimeslotDTO;
import org.example.courzelo.dto.TimetableDTO;
import org.example.courzelo.models.Period;
import org.example.courzelo.models.Timetable;
import org.example.courzelo.repositories.TimetableRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TimetableService {
    private TimetableRepository timetableRepository;
    private Random random = new Random();

    public ChromosomeDTO generateTimetable(List<String> courseIds, List<String> professorIds) {
        List<ChromosomeDTO> population = initializePopulation(courseIds, professorIds);
        int generationCount = 1000;

        for (int i = 0; i < generationCount; i++) {
            population = evolvePopulation(population, courseIds, professorIds);
        }

        return findFittestChromosome(population);
    }

    private List<ChromosomeDTO> initializePopulation(List<String> courseIds, List<String> professorIds) {
        List<ChromosomeDTO> population = new ArrayList<>();

        for (int i = 0; i < 100; i++) { // assuming a population size of 100
            ChromosomeDTO chromosome = new ChromosomeDTO();
            List<GeneDTO> genes = new ArrayList<>();

            for (String courseId : courseIds) {
                String professorId = professorIds.get(random.nextInt(professorIds.size()));
                TimeslotDTO timeSlot = getRandomTimeSlot();

                GeneDTO gene = new GeneDTO(courseId, professorId, timeSlot);
                genes.add(gene);
            }

            chromosome.setGenes(genes);
            chromosome.setFitnessScore(calculateFitness(chromosome));
            population.add(chromosome);
        }

        return population;
    }

    private TimeslotDTO getRandomTimeSlot() {
        DayOfWeek[] daysOfWeek = DayOfWeek.values();
        Period[] periods = Period.values();

        DayOfWeek randomDay = daysOfWeek[random.nextInt(daysOfWeek.length)];
        Period randomPeriod = periods[random.nextInt(periods.length)];

        return new TimeslotDTO(randomDay, randomPeriod);
    }

    private List<ChromosomeDTO> evolvePopulation(List<ChromosomeDTO> population, List<String> courseIds, List<String> professorIds) {
        List<ChromosomeDTO> newPopulation = new ArrayList<>();

        for (ChromosomeDTO chromosome : population) {
            ChromosomeDTO newChromosome = mutate(chromosome, professorIds);
            newChromosome.setFitnessScore(calculateFitness(newChromosome));
            newPopulation.add(newChromosome);
        }

        return newPopulation;
    }

    private ChromosomeDTO mutate(ChromosomeDTO chromosome, List<String> professorIds) {
        List<GeneDTO> genes = new ArrayList<>(chromosome.getGenes());
        int index = random.nextInt(genes.size());
        GeneDTO gene = genes.get(index);

        String newProfessorId = professorIds.get(random.nextInt(professorIds.size()));
        TimeslotDTO newTimeSlot = getRandomTimeSlot();

        gene.setProfessorId(newProfessorId);
        gene.setTimeSlot(newTimeSlot);

        genes.set(index, gene);
        ChromosomeDTO newChromosome = new ChromosomeDTO();
        newChromosome.setGenes(genes);

        return newChromosome;
    }

    private double calculateFitness(ChromosomeDTO chromosome) {
        double fitnessScore = 0.0;

        Map<TimeslotDTO, Set<String>> timeSlotToProfessors = new HashMap<>();
        Map<TimeslotDTO, Set<String>> timeSlotToCourses = new HashMap<>();

        for (GeneDTO gene : chromosome.getGenes()) {
            TimeslotDTO timeSlot = gene.getTimeSlot();
            String professorId = gene.getProfessorId();
            String courseId = gene.getCourseId();

            // Check for professor availability
            timeSlotToProfessors.putIfAbsent(timeSlot, new HashSet<>());
            if (!timeSlotToProfessors.get(timeSlot).add(professorId)) {
                fitnessScore += 1; // Penalty for professor teaching multiple courses at the same time
            }

            // Check for course availability
            timeSlotToCourses.putIfAbsent(timeSlot, new HashSet<>());
            if (!timeSlotToCourses.get(timeSlot).add(courseId)) {
                fitnessScore += 1; // Penalty for course being taught multiple times at the same time
            }

            // Specific constraint for Wednesday evening free
            if (timeSlot.getDayOfWeek() == DayOfWeek.WEDNESDAY && timeSlot.getPeriod() == Period.P4) {
                fitnessScore += 5; // Higher penalty for scheduling on Wednesday evening
            }
        }
        return fitnessScore;
    }
    private ChromosomeDTO findFittestChromosome(List<ChromosomeDTO> population) {
        return population.stream()
                .min(Comparator.comparingDouble(ChromosomeDTO::getFitnessScore))
                .orElse(null);
    }

    public TimetableDTO convertToTimetable(ChromosomeDTO chromosome) {
        TimetableDTO timetable = new TimetableDTO();
        Map<TimeslotDTO, List<GeneDTO>> timeslotToGenes = chromosome.getGenes().stream()
                .collect(Collectors.groupingBy(GeneDTO::getTimeSlot));

        for (Map.Entry<TimeslotDTO, List<GeneDTO>> entry : timeslotToGenes.entrySet()) {
            TimeslotDTO timeSlot = entry.getKey();
            List<GeneDTO> genes = entry.getValue();

            for (GeneDTO gene : genes) {
                timetable.addCourse(gene.getCourseId(), gene.getProfessorId(), timeSlot);
            }
        }

        return timetable;
    }


}
