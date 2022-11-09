package de.buw.fm4se.featuremodels;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import de.buw.fm4se.featuremodels.fm.Feature;
import de.buw.fm4se.featuremodels.fm.FeatureModel;
import de.buw.fm4se.featuremodels.exec.LimbooleExecutor;

/**
 * This code needs to be implemented by translating FMs to input for Limboole
 * and interpreting the output
 *
 */
public class FeatureModelAnalyzer {

  public static boolean checkConsistent(FeatureModel fm) {
    String formula = FeatureModelTranslator.translateToFormula(fm);
		return checkSatisfiability(formula);
  }

	private static boolean checkSatisfiability(String formula) {
    String result;
    try {
      result = LimbooleExecutor.runLimboole(formula, true);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    if (result.contains("UNSATISFIABLE")) {
      return false;
    }
    return true;
	}

	// 1 check
	// ==> "& feature" = unsatisfiable
  public static List<String> deadFeatureNames(FeatureModel fm) {
		String formula = FeatureModelTranslator.translateToFormula(fm);

		List<String> deadFeatures = getAllChildren(fm.getRoot()).stream()
																														.filter(f -> !checkSatisfiability(formula + " & " + f))
																														.collect(Collectors.toList());

		System.out.println("deadFeatures: " + deadFeatures);
    return deadFeatures;
  }

	// 2 check
	// ==> "& feature" = satisfy
	// ==> "& !feature" = unsatisfiable
  public static List<String> mandatoryFeatureNames(FeatureModel fm) {
		String formula = FeatureModelTranslator.translateToFormula(fm);

		List<String> firstCheck = getAllChildren(fm.getRoot()).stream()
																													.filter(f -> checkSatisfiability(formula + " & " + f))
																													.collect(Collectors.toList());
		List<String> secondCheck = firstCheck.stream()
																					.filter(f -> !checkSatisfiability(formula + " & !" + f))
																					.collect(Collectors.toList());

		System.out.println("mandatoryFeatures: " + secondCheck);
		return secondCheck;
  }

	private static List<String> getAllChildren(Feature feature) {
		List<String> features = new ArrayList<>() {{ add(feature.getName()); }}; // add self

		for (var child : feature.getChildren()) { features.addAll(getAllChildren(child)); } // recursive throgh tree node

		return features;
	}
}

// example of dead feature
// eshop &
// (eshop -> (catalogue & payment & security)) &
// (payment -> (bank & card)) &
// (security -> ((high | standard) & !(high & standard))) &
// (card -> high) 
// 
// & standard # deadFeature