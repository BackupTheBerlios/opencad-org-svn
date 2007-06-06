package org.opencad.modelling.walls;

import org.opencad.modelling.Primitive;

public abstract class WallFeature extends Primitive implements
    Comparable<WallFeature> {
  private Double startOffset;

  private Double width;

  private Double height;

  private Double groundOffset;

  public Double getMaxStartOffset() {
    return startOffset + width;
  }

  public double getMaxGroundOffset() {
    return groundOffset + height;
  }

  public Double getGroundOffset() {
    return groundOffset;
  }

  public void setGroundOffset(Double bottomOffset) {
    this.groundOffset = bottomOffset;
  }

  public Double getWidth() {
    return width;
  }

  public void setWidth(Double endOffset) {
    this.width = endOffset;
  }

  public Double getHeight() {
    return height;
  }

  public void setHeight(Double topOffset) {
    this.height = topOffset;
  }

  public Double getStartOffset() {
    return startOffset;
  }

  public void setStartOffset(Double startOffset) {
    this.startOffset = startOffset;
  }

  public int compareTo(WallFeature o) {
    return startOffset.compareTo(o.startOffset);
  }
}