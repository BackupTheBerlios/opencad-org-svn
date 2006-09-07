package org.opencad.model.modelling;

import java.util.LinkedList;

import org.opencad.model.rendering.ModelEditorRenderer;
import org.opencad.rendering.Primitive;
import org.opencad.rendering.Renderable;
import org.opencad.ui.behaviour.Hoverable;

public class Model extends Primitive {

  private static final long serialVersionUID = -4303353063372058728L;

  private LinkedList<Primitive> primitives;

  private LinkedList<Hoverable> hoverables;

  public void addPrimitive(Primitive o) {
    if (o instanceof Hoverable) {
      hoverables.add((Hoverable) o);
    }
    primitives.add(o);
  }

  public void renderPrimitives(Class<? extends Renderable> type) {
    for (Primitive primitive : primitives) {
      primitive.render(type);
    }
  }

  public final LinkedList<Hoverable> getHoverables() {
    return hoverables;
  }


  public Model() {
    primitives = new LinkedList<Primitive>();
    hoverables = new LinkedList<Hoverable>();
    addRenderer(new ModelEditorRenderer(this));
  }

  public String toString() {
    String ret = "Model {\n";
    for (Primitive primitive : primitives) {
      ret += "  " + primitive + "\n";
    }
    return ret + "}";
  }

  public boolean removePrimitive(Primitive o) {
    if (o instanceof Hoverable) {
      hoverables.remove(o);
    }
    return primitives.remove(o);
  }

  public Hoverable trapHoverable(double x, double y) {
    for (Hoverable hoverable : hoverables) {
      if (hoverable.isHoverCoordinates(x, y)) {
        return hoverable;
      }
    }
    return null;
  }

  public boolean informHoverables(double x, double y) {
    boolean changed = false;
    for (Hoverable hoverable : hoverables) {
      if (hoverable.setHover(hoverable.isHoverCoordinates(x, y))) {
        changed = true;
      }
    }
    return changed;
  }

}
