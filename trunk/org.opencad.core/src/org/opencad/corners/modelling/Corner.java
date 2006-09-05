package org.opencad.corners.modelling;

import org.opencad.corners.rendering.CornerEditorRenderer;
import org.opencad.corners.ui.DragCornerState;
import org.opencad.rendering.Primitive;
import org.opencad.ui.behaviour.Hoverable;
import org.opencad.ui.behaviour.Selectable;
import org.opencad.ui.editors.GLEditor;

public class Corner extends Primitive implements Hoverable, Selectable {

  private static final long serialVersionUID = -1332083715502519329L;

  private Double x, y;

  private boolean hover;

  private boolean selected;

  private static DragCornerState dragCornerState = new DragCornerState(null,
      null);

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

  public boolean isHoverCoordinates(double x, double y) {
    double hoverSlack = 0.02d;
    return Math.abs(this.x - x) < hoverSlack
        && Math.abs(this.y - y) < hoverSlack;
  }

  public boolean setHover(boolean hover) {
    boolean changed = this.hover != hover;
    this.hover = hover;
    return changed;
  }

  public boolean isHover() {
    return hover;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
  }

  public void setSelected(boolean selected, GLEditor editor) {
    this.selected = selected;
    dragCornerState.setCorner(this);
    dragCornerState.setGlEditor(editor);
    dragCornerState.freshen();
    dragCornerState.notifyEditor();
  }
}
