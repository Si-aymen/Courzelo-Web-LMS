package org.example.courzelo.services;

import org.example.courzelo.dto.*;
import org.example.courzelo.models.Period;
import org.example.courzelo.models.Professor;
import org.example.courzelo.models.Timetable;
import org.example.courzelo.repositories.ProfessorRepository;
import org.example.courzelo.repositories.TimetableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TimetableService {
    private TimetableRepository timetableRepository;
    private ProfessorRepository professorRepository;
    private static final Logger logger = LoggerFactory.getLogger(TimetableService.class);
    public TimetableService(TimetableRepository timetableRepository, ProfessorRepository professorRepository) {
        this.timetableRepository = timetableRepository;
        this.professorRepository = professorRepository;
    }

    private Random random = new Random();

    public ChromosomeDTO generateTimetable(List<String> courseIds, List<String> professorIds) {
        logger.info("Starting timetable generation for courses: {} and professors: {}", courseIds, professorIds);
        List<Professor> professors = professorRepository.findAllById(professorIds);
        logger.debug("Professors retrieved: {}", professors);
        List<ChromosomeDTO> population = initializePopulation(courseIds, professorIds);
        logger.info("Initial population created with size: {}", population.size());

        int generationCount = 1000;
        for (int i = 0; i < generationCount; i++) {
            logger.info("Evolution cycle: {}", i + 1);
            population = evolvePopulation(population, courseIds, professorIds);
        }

        ChromosomeDTO fittest = findFittestChromosome(population);
        logger.info("Fittest Chromosome after {} generations: {}", generationCount, fittest);

        return fittest;
    }

    private List<ChromosomeDTO> initializePopulation(List<String> courseIds, List<String> professorIds) {
        logger.info("Initializing population for courses: {} and professors: {}", courseIds, professorIds);
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
            logger.debug("Chromosome {} initialized with fitness score: {}", i, chromosome.getFitnessScore());
            population.add(chromosome);
        }

        logger.info("Population initialization complete with size: {}", population.size());
        return population;
    }

    private TimeslotDTO getRandomTimeSlot() {
        DayOfWeek[] daysOfWeek = DayOfWeek.values();
        Period[] periods = Period.values();

        DayOfWeek randomDay = daysOfWeek[random.nextInt(daysOfWeek.length)];
        Period randomPeriod = periods[random.nextInt(periods.length)];

        TimeslotDTO timeSlot = new TimeslotDTO(randomDay, randomPeriod);
        logger.debug("Generated random time slot: {}", timeSlot);
        return timeSlot;
    }

    private List<ChromosomeDTO> evolvePopulation(List<ChromosomeDTO> population, List<String> courseIds, List<String> professorIds) {
        logger.info("Evolving population of size: {}", population.size());
        List<ChromosomeDTO> newPopulation = new ArrayList<>();

        for (int i = 0; i < population.size(); i++) {
            ChromosomeDTO chromosome = population.get(i);
            ChromosomeDTO newChromosome = mutate(chromosome, professorIds);
            newChromosome.setFitnessScore(calculateFitness(newChromosome));
            logger.debug("Chromosome {} evolved with new fitness score: {}", i, newChromosome.getFitnessScore());
            newPopulation.add(newChromosome);
        }

        logger.info("Population evolution complete. New population size: {}", newPopulation.size());
        return newPopulation;
    }

    private ChromosomeDTO mutate(ChromosomeDTO chromosome, List<String> professorIds) {
        logger.debug("Mutating chromosome with genes: {}", chromosome.getGenes());
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
        logger.debug("Chromosome mutated at index {} with new professor ID: {} and time slot: {}", index, newProfessorId, newTimeSlot);

        return newChromosome;
    }

    private double calculateFitness(ChromosomeDTO chromosome) {
        logger.debug("Calculating fitness for chromosome with genes: {}", chromosome.getGenes());
        double fitnessScore = 0.0;

        Map<TimeslotDTO, Set<String>> timeSlotToProfessors = new HashMap<>();
        Map<TimeslotDTO, Set<String>> timeSlotToCourses = new HashMap<>();

        for (GeneDTO gene : chromosome.getGenes()) {
            TimeslotDTO timeSlot = gene.getTimeSlot();
            String professorId = gene.getProfessorId();
            String courseId = gene.getCourseId();

            timeSlotToProfessors.putIfAbsent(timeSlot, new HashSet<>());
            if (!timeSlotToProfessors.get(timeSlot).add(professorId)) {
                fitnessScore += 1; // Penalty for professor teaching multiple courses at the same time
                logger.debug("Penalty applied for professor {} teaching multiple courses at the same time slot: {}", professorId, timeSlot);
            }

            timeSlotToCourses.putIfAbsent(timeSlot, new HashSet<>());
            if (!timeSlotToCourses.get(timeSlot).add(courseId)) {
                fitnessScore += 1; // Penalty for course being taught multiple times at the same time
                logger.debug("Penalty applied for course {} being taught multiple times at the same time slot: {}", courseId, timeSlot);
            }

            if (timeSlot.getDayOfWeek() == DayOfWeek.WEDNESDAY && timeSlot.getPeriod() == Period.P4) {
                fitnessScore += 5; // Higher penalty for scheduling on Wednesday evening
                logger.debug("Higher penalty applied for scheduling on Wednesday evening: {}", timeSlot);
            }
        }

        logger.debug("Fitness score calculated: {}", fitnessScore);
        return fitnessScore;
    }

    private ChromosomeDTO findFittestChromosome(List<ChromosomeDTO> population) {
        logger.info("Finding fittest chromosome in population of size: {}", population.size());
        ChromosomeDTO fittest = population.stream()
                .min(Comparator.comparingDouble(ChromosomeDTO::getFitnessScore))
                .orElse(null);

        logger.info("Fittest Chromosome found: {}", fittest);
        return fittest;
    }

    public TimetableDTO convertToTimetable(ChromosomeDTO chromosome) {
        logger.info("Converting Chromosome to Timetable: {}", chromosome);
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

        logger.info("Conversion to Timetable complete: {}", timetable);
        return timetable;
    }

    public List<TimetableDTO> getTimetable() {
        logger.info("Fetching all timetables from the database");
        List<Timetable> timetables = timetableRepository.findAll();
        List<TimetableDTO> timetableDTOs = timetables.stream().map(this::convertToTimetableDTO).collect(Collectors.toList());
        logger.info("Timetables fetched and converted: {}", timetableDTOs);
        return timetableDTOs;
    }

    private TimetableDTO convertToTimetableDTO(Timetable timetable) {
        logger.debug("Converting Timetable to DTO: {}", timetable);
        TimetableDTO dto = new TimetableDTO();
        dto.setDayOfWeek(timetable.getDayOfWeek());
        dto.setPeriod(timetable.getPeriod());
        dto.setCourseName(timetable.getCourseName());
        Professor professor = professorRepository.findById(timetable.getProfessorId()).orElse(null);
        dto.setProfessorName(professor != null ? professor.getName() : "Unknown");
        logger.debug("Conversion complete: {}", dto);

        return dto;
    }


    public ProfessorDTO addProfessor(ProfessorDTO professorDTO) {
        logger.info("Adding new professor: {}", professorDTO);
        Professor professor = new Professor();
        professor.setName(professorDTO.getName());
        professor.setUnavailableTimeSlots(professorDTO.getUnavailableTimeSlots());
        Professor savedProfessor = professorRepository.save(professor);
        logger.info("Professor added: {}", savedProfessor);
        return convertToProfessorDTO(savedProfessor);
    }

    private ProfessorDTO convertToProfessorDTO(Professor savedProfessor) {
        logger.debug("Converting Professor to DTO: {}", savedProfessor);
        ProfessorDTO dto = new ProfessorDTO();
        dto.setId(savedProfessor.getId());
        dto.setName(savedProfessor.getName());
        dto.setUnavailableTimeSlots(savedProfessor.getUnavailableTimeSlots());
        logger.debug("Conversion complete: {}", dto);
        return dto;
    }


}
