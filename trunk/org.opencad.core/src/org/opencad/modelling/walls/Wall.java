package org.opencad.modelling.walls;

import org.eclipse.opengl.GL;
import org.opencad.modelling.Primitive;
import org.opencad.modelling.PrimitiveTypeRegister;
import org.opencad.modelling.corners.Corner;

public class Wall extends Primitive {
  private static final long serialVersionUID = -8662848078904155699L;
  static {
    PrimitiveTypeRegister.registerPrimitiveType(Wall.class);
  }

  Corner startingCorner, endingCorner;

  public final Corner getEndingCorner() {
    return endingCorner;
  }

  public final void setEndingCorner(Corner endingCorner) {
    if (this.endingCorner != null) {
      this.endingCorner.removeEnd(this);
    }
    this.endingCorner = endingCorner;
    if (this.endingCorner != null) {
      this.endingCorner.addEnd(this);
    }
  }

  public final Corner getStartingCorner() {
    return startingCorner;
  }

  public final void setStartingCorner(Corner startingCorner) {
    if (this.startingCorner != null) {
      this.startingCorner.removeStart(this);
    }
    this.startingCorner = startingCorner;
    if (this.startingCorner != null) {
      this.startingCorner.addStart(this);
    }
  }

  public Wall(Corner startingCorner, Corner endingCorner) {
    this.startingCorner = startingCorner;
    this.endingCorner = endingCorner;
  }

  public String toString() {
    return String.format("(wall %s %s)", startingCorner, endingCorner);
  }

  public double sqr(double a) {
    return a * a;
  }

  int slight_slope(double[] a, double[] b, int ax, int bx) {
    return (int) (Math.signum(a[ax + 1] - b[bx + 1]) + 3 * Math.signum(a[ax]
        - b[bx]));
  }

  public void editorRender() {
    if (getStartingCorner() != null && getEndingCorner() != null) {
      GL.glColor3d(0d, 0d, 0d);
      GL.glBegin(GL.GL_LINES);
      {
        double[] start_points = getStartingCorner().getStartLimitsOf(this);
        double[] end_points = getEndingCorner().getEndLimitsOf(this);
        double[] these_points = new double[] { getStartingCorner().getX(),
            getStartingCorner().getY(), getEndingCorner().getX(),
            getEndingCorner().getY() };
        int angle = slight_slope(these_points, these_points, 0, 2);
        int angle_1 = slight_slope(start_points, end_points, 0, 2);
        if (angle == angle_1) {
          GL.glVertex2d(start_points[0], start_points[1]);
          GL.glVertex2d(end_points[2], end_points[3]);
        }
        int angle_2 = slight_slope(start_points, end_points, 2, 0);
        if (angle == angle_2) {
          GL.glVertex2d(start_points[2], start_points[3]);
          GL.glVertex2d(end_points[0], end_points[1]);
        }
      }
      GL.glEnd();
      GL.glEnable(GL.GL_LINE_STIPPLE);
      {
        GL.glLineStipple(1, (short) 0x43f0);
        GL.glBegin(GL.GL_LINES);
        {
          GL.glVertex2d(getStartingCorner().getX(), getStartingCorner().getY());
          GL.glVertex2d(getEndingCorner().getX(), getEndingCorner().getY());
        }
        GL.glEnd();
      }
      GL.glDisable(GL.GL_LINE_STIPPLE);
    }
  }

  public static final double height = 3.5d;

  void realRenderRoutine() {
    double[] start_points = getStartingCorner().getStartLimitsOf(this);
    double[] end_points = getEndingCorner().getEndLimitsOf(this);
    double[] these_points = new double[] { getStartingCorner().getX(),
        getStartingCorner().getY(), getEndingCorner().getX(),
        getEndingCorner().getY() };
    int angle = slight_slope(these_points, these_points, 0, 2);
    int angle_1 = slight_slope(start_points, end_points, 0, 2);
    int angle_2 = slight_slope(start_points, end_points, 2, 0);
    GL.glBegin(GL.GL_QUADS);
    {
      if (angle == angle_1) {
        GL.glVertex2d(start_points[0], start_points[1]);
        GL.glVertex3d(start_points[0], start_points[1], height);
        GL.glVertex3d(end_points[2], end_points[3], height);
        GL.glVertex2d(end_points[2], end_points[3]);
      }
      if (angle == angle_2) {
        GL.glVertex2d(start_points[2], start_points[3]);
        GL.glVertex3d(start_points[2], start_points[3], height);
        GL.glVertex3d(end_points[0], end_points[1], height);
        GL.glVertex2d(end_points[0], end_points[1]);
      }
    }
    GL.glEnd();
    if (angle == angle_1 && angle == angle_2) {
      GL.glBegin(GL.GL_POLYGON);
      {
        GL.glVertex3d(start_points[0], start_points[1], height);
        GL.glVertex3d(these_points[0], these_points[1], height);
        GL.glVertex3d(start_points[2], start_points[3], height);
        GL.glVertex3d(end_points[0], end_points[1], height);
        GL.glVertex3d(these_points[2], these_points[3], height);
        GL.glVertex3d(end_points[2], end_points[3], height);
      }
      GL.glEnd();
    }
  }

  public void realRender() {
    if (getStartingCorner() != null && getEndingCorner() != null) {
      GL.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
      GL.glColor3d(0d, 0d, 0d);
      realRenderRoutine();
      GL.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
      GL.glEnable(GL.GL_POLYGON_OFFSET_FILL);
      GL.glPolygonOffset(1f, 1f);
      GL.glColor3d(1d, 1d, 1d);
      realRenderRoutine();
      GL.glDisable(GL.GL_POLYGON_OFFSET_FILL);
    }
  }
}