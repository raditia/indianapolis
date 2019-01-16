package com.gdn.genetic_algorithm.impl;

import com.gdn.genetic_algorithm.GeneRecommendationResult;
import com.gdn.genetic_algorithm.IndividualProcessorService;
import com.gdn.genetic_algorithm.IndividualRecommendationResult;
import com.gdn.genetic_algorithm.PopulationRecommendationResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndividualProcessorServiceImpl implements IndividualProcessorService {

    @Override
    public void readInput(List<PopulationRecommendationResult> populationRecommendationResultList) {
        for (PopulationRecommendationResult populationRecommendationResult:populationRecommendationResultList) {
            System.out.println("OPTION " + populationRecommendationResult.getIndex());
            for (IndividualRecommendationResult individualRecommendationResult:populationRecommendationResult.getIndividualList()) {
                System.out.println("\tFLEET : " + individualRecommendationResult.getFleetName());
                for (GeneRecommendationResult geneRecommendationResult: individualRecommendationResult.getGeneList()) {
                    System.out.println("\t\tPICKUP : " + geneRecommendationResult.getSku() + " " + geneRecommendationResult.getCbm());
                }
            }
        }
    }
}
