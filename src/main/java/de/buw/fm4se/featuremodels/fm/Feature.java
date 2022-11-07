package de.buw.fm4se.featuremodels.fm;

import java.util.ArrayList;
import java.util.List;

public class Feature {
  private String name;
  private boolean mandatory = false;
  private List<Feature> children = new ArrayList<>();
  private GroupKind childGroupKind = GroupKind.NONE;

  public Feature(String name) {
    this.name = name;
  }

  public boolean isMandatory() {
    return mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  public List<Feature> getChildren() {
    return children;
  }

  public String getName() {
    return name;
  }

  /**
   * removes everything from the name that is not a letter or digit
   * 
   * @return
   */
  public String getSimpleName() {
    return name.replaceAll("[^a-zA-Z0-9]", "");
  }

  public void setName(String name) {
    this.name = name;
  }

  public Feature addChild(String name, boolean mandatory) {
    Feature c = new Feature(name);
    c.mandatory = mandatory;
    this.children.add(c);
    return c;
  }

  public GroupKind getChildGroupKind() {
    return childGroupKind;
  }

  public void setChildGroupKind(GroupKind childGroupKind) {
    this.childGroupKind = childGroupKind;
  }
}
