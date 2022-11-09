package de.buw.fm4se.featuremodels;

import java.util.List;
import java.util.ArrayList;
import java.text.MessageFormat;
import java.util.stream.Collectors;
import de.buw.fm4se.featuremodels.fm.Feature;
import de.buw.fm4se.featuremodels.fm.GroupKind;
import de.buw.fm4se.featuremodels.fm.FeatureModel;
import de.buw.fm4se.featuremodels.fm.CrossTreeConstraint;

public class FeatureModelTranslator {

	public static String translateToFormula(FeatureModel fm) {
		List<String> formulas = new ArrayList<String>();

		formulas.add(root(fm));
		formulas.addAll(parentRelation(fm.getRoot()));
		formulas.addAll(mandatorySubfeature(fm.getRoot()));
		formulas.addAll(orGroup(fm.getRoot()));
		formulas.addAll(xorGroup(fm.getRoot()));
		formulas.addAll(constrain(fm));
		
		return combineFormula(formulas);
	}

	private static String root(FeatureModel fm) {
		return fm.getRoot().getName();
	}

	// e.g. feature -> parent
	private static List<String> parentRelation(Feature parent) {
		List<String> pFormulas = new ArrayList<String>();

		for(var child: parent.getChildren()) {
			pFormulas.add(MessageFormat.format("{0} -> {1}", child.getName(), parent.getName()));
			if (child.getChildren().size() > 0) { pFormulas.addAll(parentRelation(child)); } // recursive through all children
		}

		return pFormulas;
	}

	// e.g. parent -> mandatoryFeature
	private static List<String> mandatorySubfeature(Feature parent) {
		List<String> msFormulas = new ArrayList<String>();

		for(var child: parent.getChildren()) {
			if (child.isMandatory()) { msFormulas.add(MessageFormat.format("{0} -> {1}", parent.getName(), child.getName())); }
			if (child.getChildren().size() > 0) { msFormulas.addAll(mandatorySubfeature(child)); }
		}

		return msFormulas;
	}

	// e.g. parent -> (feature1 | feature2)
	private static List<String> orGroup(Feature parent) {
		List<String> ogFormulas = new ArrayList<String>();

		if (parent.getChildGroupKind() == GroupKind.OR) {
			String childrenList = parent.getChildren().stream()
																								.map(x -> x.getName())
																								.collect(Collectors.joining(" | ", "(", ")"));
			ogFormulas.add(MessageFormat.format("{0} -> {1}", parent.getName(), childrenList));
		}
		for (var child : parent.getChildren()) { ogFormulas.addAll(orGroup(child)); } // recursive through all children

		return ogFormulas;
	}

	// e.g. parent -> (feature1 & !feature2 | !feature1 & feature2)
	private static List<String> xorGroup(Feature parent) {
		List<String> xgFormulas = new ArrayList<String>();
		
		if (parent.getChildGroupKind() == GroupKind.XOR) {
			// create feature1 & !feature2
			List<String> andList = new ArrayList<String>();
			for(var child : parent.getChildren()){
				String and = parent.getChildren().stream()
																					.map(x -> x.getName() == child.getName() ? x.getName() : "!" + x.getName())
																					.collect(Collectors.joining(" & "));
				andList.add(and);
			}

			// then combine feature1 & !feature2 | !feature1 & feature2
			String or = andList.stream()
													.map(x -> x)
													.collect(Collectors.joining(" | ", "(", ")"));

			// add parent to formula
			xgFormulas.add(MessageFormat.format("{0} -> {1}", parent.getName(), or));
		}
		for (var child : parent.getChildren()) { xgFormulas.addAll(xorGroup(child)); }

		return xgFormulas;
	}

	// e.g. feature1 -> feature2 #require constrain
	// 			feature1 -> !feature2 #exclude constrain
	private static List<String> constrain(FeatureModel fm) {
		return fm.getConstraints().stream()
															.map(x -> MessageFormat.format("{0}{1}{2}",
																														x.getLeft().getName(),
																														x.getKind() == CrossTreeConstraint.Kind.REQUIRES ? " -> " : " -> !",
																														x.getRight().getName()))
															.collect(Collectors.toList());
	}

	// add bracket to each formula
	// add & \n at the end of string
	private static String combineFormula(List<String> formulas) {
		String limbooleFormula = formulas.stream()
																			.map(x -> "(" + x + ")")
																			.collect(Collectors.joining(" & \n"));
		System.out.println("limbooleFormula: " + limbooleFormula);
		return limbooleFormula;
	}
}

// ExampleFmCreator.getSimpleFm()
// (car) & #root
// 
// (motor -> car) &  #parent relation
// (comfort -> car) & #parent relation
// (electric -> motor) & #parent relation
// (gasoline -> motor) & #parent relation
// (heating -> comfort) & #parent relation
// (entertainment -> comfort) & #parent relation
// 
// (car -> motor) & #mandatory subfeature
// 
// (comfort -> (heating | entertainment)) & #or group
// 
// (comfort -> (electric & !gasoline | !electric & gasoline)) & #xor group
// 
// (electric -> heating) #constrain
