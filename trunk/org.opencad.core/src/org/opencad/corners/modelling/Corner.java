package org.opencad.corners.modelling;

import org.opencad.corners.rendering.CornerEditorRenderer;
import org.opencad.rendering.Primitive;

public class Corner extends Primitive {

  private static final long serialVersionUID = -1332083715502519329L;

  private Double x, y;

  public final Double getX() {
    return x;
  }

  public final void setX(Double x) {
    this.x = x;
  }

  public final Double getY() {
    return y;
  }

  public final void setY(Double y) {
    this.y = y;
  }

  public Corner(Double x, Double y) {
    this.x = x;
    this.y = y;
    addRenderer(new CornerEditorRenderer(this));
  }

  public String toString() {
    return x + "," + y;
  }

}
