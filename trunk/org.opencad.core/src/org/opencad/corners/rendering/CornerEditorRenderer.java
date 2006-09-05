package org.opencad.corners.rendering;

import org.eclipse.opengl.GL;
import org.opencad.corners.modelling.Corner;
import org.opencad.rendering.EditorRenderable;

public class CornerEditorRenderer implements EditorRenderable {

  private static final long serialVersionUID = -1650322838685233351L;

  private Corner corner;

  private double outerRadius = 0.04d;
  private double innerRadius = 0.02d;

  public void glRender() {
    double x = corner.getX();
    double y = corner.getY();
    GL.glBegin(GL.GL_LINES);
    {
      GL.glVertex2d(x - outerRadius, y);
      GL.glVertex2d(x - innerRadius, y);
      GL.glVertex2d(x + outerRadius, y);
      GL.glVertex2d(x + innerRadius, y);
      GL.glVertex2d(x, y - innerRadius);
      GL.glVertex2d(x, y - outerRadius);
      GL.glVertex2d(x, y + innerRadius);
      GL.glVertex2d(x, y + outerRadius);
      
      GL.glVertex2d(x - innerRadius, y);
      GL.glVertex2d(x, y - innerRadius);

      GL.glVertex2d(x - innerRadius, y);
      GL.glVertex2d(x, y + innerRadius);

      GL.glVertex2d(x + innerRadius, y);
      GL.glVertex2d(x, y - innerRadius);

      GL.glVertex2d(x + innerRadius, y);
      GL.glVertex2d(x, y + innerRadius);
    }
    GL.glEnd();
  }

  public CornerEditorRenderer(Corner corner) {
    this.corner = corner;
  }

}
