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
        int angle = (int) (Math.signum(these_points[1] - these_points[3]) + 3 * Math
            .signum(these_points[0] - these_points[2]));
        int angle_1 = (int) (Math.signum(start_points[1] - end_points[3]) + 3 * Math
            .signum(start_points[0] - end_points[2]));
        if (angle == angle_1) {
          GL.glVertex2d(start_points[0], start_points[1]);
          GL.glVertex2d(end_points[2], end_points[3]);
        }
        int angle_2 = (int) (Math.signum(start_points[3] - end_points[1]) + 3 * Math
            .signum(start_points[2] - end_points[0]));
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

  public void realRender() {
    if (getStartingCorner() != null && getEndingCorner() != null) {
      GL.glColor3d(0d, 0d, 0d);
      GL.glBegin(GL.GL_LINES);
      {
        GL
            .glVertex3d(getStartingCorner().getX(), getStartingCorner().getY(),
                0);
        GL.glVertex3d(getEndingCorner().getX(), getEndingCorner().getY(), 0);
        GL.glVertex3d(getStartingCorner().getX(), getStartingCorner().getY(),
            height);
        GL.glVertex3d(getEndingCorner().getX(), getEndingCorner().getY(),
            height);
      }
      GL.glEnd();
    }
  }
}