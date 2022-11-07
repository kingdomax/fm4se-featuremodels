package de.buw.fm4se.featuremodels.fm;

import java.util.ArrayList;
import java.util.List;

public class FeatureModel {
  private Feature root;
  private List<CrossTreeConstraint> constraints = new ArrayList<>();

  public List<CrossTreeConstraint> getConstraints() {
    return constraints;
  }

  public Feature getRoot() {
    return root;
  }

  public void setRoot(Feature root) {
    this.root = root;
  }

  public void addConstraint(CrossTreeConstraint crossTreeConstraint) {
    constraints.add(crossTreeConstraint);    
  }
}
