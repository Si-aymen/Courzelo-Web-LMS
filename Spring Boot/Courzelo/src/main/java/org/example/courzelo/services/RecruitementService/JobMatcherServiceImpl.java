package org.example.courzelo.services.RecruitementService;

import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.RecruitementEntities.JobOffer;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j

public class JobMatcherServiceImpl implements IJobMatcherService{
    private static final int SKILL_WEIGHT = 1;
    private static final int SPECIALITY_WEIGHT = 2;
    private static final int MIN_MATCHING_SCORE = 0;

    @Override
    public List<JobOffer> matchJobs(List<String> candidateSkills, List<String> candidateSpecialities, List<JobOffer> availableJobs) {
        List<JobOffer> matchedJobs = new ArrayList<>();
        for (JobOffer jobOffer : availableJobs) {

            System.out.println("Candidate Skills: " + candidateSkills);
            System.out.println("Job Skills: " + List.of(jobOffer.getSkills().split(",")));

            int matchingScore = calculateMatchingScore(candidateSkills, candidateSpecialities,jobOffer);
            if (matchingScore >= MIN_MATCHING_SCORE) {
                jobOffer.setMatchingScore(matchingScore);
                matchedJobs.add(jobOffer);
            }
        }
        // sort matched jobs by matching score
        matchedJobs.sort(Comparator.comparingInt(JobOffer::getMatchingScore).reversed());
        return matchedJobs;

    }

    @Override
    public int calculateMatchingScore(List<String> candidateSkills, List<String> candidateSpecialities, JobOffer jobOffer) {
        int skillScore = 0;
        int specialityScore = 0;

        if (!candidateSkills.isEmpty()) {
            skillScore = calculateScore(candidateSkills, List.of(jobOffer.getSkills().split(",")), SKILL_WEIGHT);
        }

        if (!candidateSpecialities.isEmpty()) {
            specialityScore = calculateScore(candidateSpecialities, List.of(jobOffer.getSpeciality().split(",")), SPECIALITY_WEIGHT);
        }
        // return sum of skill and speciality scores
        System.out.println(skillScore + specialityScore);
        return skillScore + specialityScore;
    }

    @Override
    public int calculateScore(List<String> candidateAttributes, List<String> jobAttributes, int weight) {
        if (candidateAttributes.isEmpty() || jobAttributes.isEmpty()) {
            return 0;
        }

        Set<String> candidateSet = new HashSet<>(candidateAttributes);
        Set<String> jobSet = new HashSet<>(jobAttributes);

        // calculate intersection
        candidateSet.retainAll(jobSet);

        // apply weight to the intersection
        return candidateSet.size() * weight;
    }


}
