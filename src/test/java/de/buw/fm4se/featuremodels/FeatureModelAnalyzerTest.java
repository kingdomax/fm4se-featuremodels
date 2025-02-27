package de.buw.fm4se.featuremodels;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import de.buw.fm4se.featuremodels.fm.FeatureModel;

class FeatureModelAnalyzerTest {

 @Test
  void testCheckConsistentCarFM() {
    assertTrue(FeatureModelAnalyzer.checkConsistent(ExampleFmCreator.getSimpleFm()));
  }
  
  @Test
  void testCheckConsistentBadFM() {
    assertFalse(FeatureModelAnalyzer.checkConsistent(ExampleFmCreator.getBadFm()));
  }
  
  @Test
  void testCheckDeadFeaturesBadFM() {
    FeatureModel fm = ExampleFmCreator.getBadFm();
    List<String> deadFeatures = FeatureModelAnalyzer.deadFeatureNames(fm);
    
    assertTrue(deadFeatures.contains(fm.getRoot().getName()));
    assertTrue(deadFeatures.contains(fm.getRoot().getChildren().get(0).getName()));
  }
  
  @Test
  void testCheckDeadFeaturesCarFM() {
    FeatureModel fm = ExampleFmCreator.getSimpleFm();
    List<String> deadFeatures = FeatureModelAnalyzer.deadFeatureNames(fm);
    
    assertFalse(deadFeatures.contains(fm.getRoot().getName()));
  }

  @Test
  void testCheckMandatoryFeaturesCarFM() {
    List<String> expected = new ArrayList<String>() {{
      add("car");
      add("motor");
    }};

    List<String> actual = FeatureModelAnalyzer.mandatoryFeatureNames(ExampleFmCreator.getSimpleFm());
    
    assertEquals(expected, actual);
  }
}
