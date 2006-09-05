package org.opencad.model.modelling;

import java.util.LinkedList;

import org.opencad.model.rendering.ModelEditorRenderer;
import org.opencad.rendering.Primitive;

public class Model extends Primitive {

  private static final long serialVersionUID = -4303353063372058728L;

  private LinkedList<Primitive> primitives;

  public final LinkedList<Primitive> getPrimitives() {
    return primitives;
  }

  private boolean dirty;

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  public boolean isDirty() {
    return dirty;
  }

  public Model() {
    primitives = new LinkedList<Primitive>();
    dirty = false;
    addRenderer(new ModelEditorRenderer(this));
  }

  public String toString() {
    String ret = "Model {\n";
    for (Primitive primitive : primitives) {
      ret += "  " + primitive + "\n";
    }
    return ret + "}";
  }
}
