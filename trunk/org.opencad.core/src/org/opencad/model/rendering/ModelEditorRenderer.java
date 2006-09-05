package org.opencad.model.rendering;

import org.opencad.model.modelling.Model;
import org.opencad.rendering.EditorRenderable;
import org.opencad.rendering.Primitive;

public class ModelEditorRenderer implements EditorRenderable {

  private static final long serialVersionUID = 7804020005397184408L;

  private Model model;

  public void glRender() {
    for (Primitive primitive : model.getPrimitives()) {
      primitive.render(EditorRenderable.class);
    }
  }

  public ModelEditorRenderer(Model model) {
    this.model = model;
  }

}
