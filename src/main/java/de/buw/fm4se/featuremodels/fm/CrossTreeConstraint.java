package de.buw.fm4se.featuremodels.fm;

/**
 * representation of simple cross tree constraints where one feature either
 * requires or excludes another
 * 
 */
public class CrossTreeConstraint {
  public enum Kind {
    REQUIRES, EXCLUDES
  }

  private Feature left;
  private Kind kind;
  private Feature right;

  public CrossTreeConstraint(Feature left, Kind kind, Feature right) {
    this.setLeft(left);
    this.setKind(kind);
    this.setRight(right);
  }

  public Feature getLeft() {
    return left;
  }

  public void setLeft(Feature left) {
    this.left = left;
  }

  public Feature getRight() {
    return right;
  }

  public void setRight(Feature right) {
    this.right = right;
  }

  public Kind getKind() {
    return kind;
  }

  public void setKind(Kind kind) {
    this.kind = kind;
  }
}
