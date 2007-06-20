package org.opencad.modelling.walls.features;

import org.eclipse.opengl.GL;
import org.opencad.modelling.corners.Corner;
import org.opencad.modelling.walls.Wall;
import org.opencad.modelling.walls.WallFeature;
import org.opencad.ui.editor.GLEditor;
import org.opencad.ui.editor.GLEditorState;

public class Window extends WallFeature {
  private static final long serialVersionUID = 1L;

  public Window() {
    setGroundOffset(1d);
    setHeight(1.5d);
    setWidth(2.5d);
    setStartOffset(0d);
  }

  public void editorRender() {
    GL.glTranslated(0d, -Corner.thickness, 0d);
    GL.glBegin(GL.GL_LINES);
    {
      GL.glVertex2d(0, 0);
      GL.glVertex2d(0, Corner.thickness * 2);
      GL.glVertex2d(getWidth(), 0);
      GL.glVertex2d(getWidth(), Corner.thickness * 2);
      GL.glVertex2d(0, Corner.thickness);
      GL.glVertex2d(getWidth() / 6, Corner.thickness);
      GL.glVertex2d(getWidth() * 5 / 6, Corner.thickness);
      GL.glVertex2d(getWidth(), Corner.thickness);
      GL.glVertex2d(getWidth() / 6, Corner.thickness);
      GL.glVertex2d(getWidth() * 2.5 / 6, Corner.thickness * 3);
      GL.glVertex2d(getWidth() * 5 / 6, Corner.thickness);
      GL.glVertex2d(getWidth() * 3.5 / 6, Corner.thickness * 3);
    }
    GL.glEnd();
  }

  public void realRender(boolean fillMode) {
    GL.glTranslated(0d, -Corner.thickness, 0d);
    if (fillMode) {
      GL.glBegin(GL.GL_QUADS);
      {
        GL.glVertex3d(0, 0, 0);
        GL.glVertex3d(getWidth(), 0, 0);
        GL.glVertex3d(getWidth(), 0, getGroundOffset());
        GL.glVertex3d(0, 0, getGroundOffset());
        GL.glVertex3d(0, 0, getMaxGroundOffset());
        GL.glVertex3d(getWidth(), 0, getMaxGroundOffset());
        GL.glVertex3d(getWidth(), 0, Wall.height);
        GL.glVertex3d(0, 0, Wall.height);
        GL.glVertex3d(getWidth(), 0, getGroundOffset());
        GL.glVertex3d(getWidth(), 0, getMaxGroundOffset());
        GL.glVertex3d(getWidth(), 2 * Corner.thickness, getMaxGroundOffset());
        GL.glVertex3d(getWidth(), 2 * Corner.thickness, getGroundOffset());
        GL.glVertex3d(0, 0, getGroundOffset());
        GL.glVertex3d(0, 0, getMaxGroundOffset());
        GL.glVertex3d(0, 2 * Corner.thickness, getMaxGroundOffset());
        GL.glVertex3d(0, 2 * Corner.thickness, getGroundOffset());
        GL.glVertex3d(getWidth(), 0, getGroundOffset());
        GL.glVertex3d(0, 0, getGroundOffset());
        GL.glVertex3d(0, 2 * Corner.thickness, getGroundOffset());
        GL.glVertex3d(getWidth(), 2 * Corner.thickness, getGroundOffset());
        GL.glVertex3d(getWidth(), 0, getMaxGroundOffset());
        GL.glVertex3d(0, 0, getMaxGroundOffset());
        GL.glVertex3d(0, 2 * Corner.thickness, getMaxGroundOffset());
        GL.glVertex3d(getWidth(), 2 * Corner.thickness, getMaxGroundOffset());
        GL.glVertex3d(getWidth(), 0, Wall.height);
        GL.glVertex3d(0, 0, Wall.height);
        GL.glVertex3d(0, 2 * Corner.thickness, Wall.height);
        GL.glVertex3d(getWidth(), 2 * Corner.thickness, Wall.height);
        GL.glVertex3d(0, 2 * Corner.thickness, 0);
        GL.glVertex3d(getWidth(), 2 * Corner.thickness, 0);
        GL.glVertex3d(getWidth(), 2 * Corner.thickness, getGroundOffset());
        GL.glVertex3d(0, 2 * Corner.thickness, getGroundOffset());
        GL.glVertex3d(0, 2 * Corner.thickness, getMaxGroundOffset());
        GL.glVertex3d(getWidth(), 2 * Corner.thickness, getMaxGroundOffset());
        GL.glVertex3d(getWidth(), 2 * Corner.thickness, Wall.height);
        GL.glVertex3d(0, 2 * Corner.thickness, Wall.height);
      }
      GL.glEnd();
    } else {
      GL.glBegin(GL.GL_LINES);
      {
        GL.glVertex3d(0, 0, 0);
        GL.glVertex3d(getWidth(), 0, 0);
        GL.glVertex3d(0, 2 * Corner.thickness, 0);
        GL.glVertex3d(getWidth(), 2 * Corner.thickness, 0);
        GL.glVertex3d(0, 0, Wall.height);
        GL.glVertex3d(getWidth(), 0, Wall.height);
        GL.glVertex3d(0, 2 * Corner.thickness, Wall.height);
        GL.glVertex3d(getWidth(), 2 * Corner.thickness, Wall.height);
        GL.glVertex3d(0, 0, getGroundOffset());
        GL.glVertex3d(getWidth(), 0, getGroundOffset());
        GL.glVertex3d(0, 2 * Corner.thickness, getGroundOffset());
        GL.glVertex3d(getWidth(), 2 * Corner.thickness, getGroundOffset());
        GL.glVertex3d(0, 0, getMaxGroundOffset());
        GL.glVertex3d(getWidth(), 0, getMaxGroundOffset());
        GL.glVertex3d(0, 2 * Corner.thickness, getMaxGroundOffset());
        GL.glVertex3d(getWidth(), 2 * Corner.thickness, getMaxGroundOffset());
        GL.glVertex3d(0, 0, getGroundOffset());
        GL.glVertex3d(0, 0, getMaxGroundOffset());
        GL.glVertex3d(0, 2 * Corner.thickness, getMaxGroundOffset());
        GL.glVertex3d(0, 2 * Corner.thickness, getGroundOffset());
        GL.glVertex3d(getWidth(), 0, getGroundOffset());
        GL.glVertex3d(getWidth(), 0, getMaxGroundOffset());
        GL.glVertex3d(getWidth(), 2 * Corner.thickness, getMaxGroundOffset());
        GL.glVertex3d(getWidth(), 2 * Corner.thickness, getGroundOffset());
        GL.glVertex3d(0, 0, getGroundOffset());
        GL.glVertex3d(0, 2 * Corner.thickness, getGroundOffset());
        GL.glVertex3d(getWidth(), 0, getGroundOffset());
        GL.glVertex3d(getWidth(), 2 * Corner.thickness, getGroundOffset());
        GL.glVertex3d(0, 0, getMaxGroundOffset());
        GL.glVertex3d(0, 2 * Corner.thickness, getMaxGroundOffset());
        GL.glVertex3d(getWidth(), 0, getMaxGroundOffset());
        GL.glVertex3d(getWidth(), 2 * Corner.thickness, getMaxGroundOffset());
      }
      GL.glEnd();
    }
  }

  public int getZIndex() {
    return -50;
  }

  public boolean isHover() {
    return false;
  }

  public boolean isHoverCoordinates(double x, double y) {
    return false;
  }

  public boolean setHover(boolean hover) {
    return false;
  }

  public GLEditorState getSelectionState(GLEditor editor) {
    return null;
  }

  public boolean isSelected() {
    return false;
  }

  public void setSelected(boolean selected) {
  }
}