package org.opencad.modelling.walls;

import java.util.ArrayList;
import java.util.TreeSet;
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

  private TreeSet<WallFeature> features = new TreeSet<WallFeature>();

  public void addFeature(WallFeature feature) {
    for (WallFeature aFeature : features) {
      if (aFeature.getStartOffset() <= feature.getStartOffset()
          && !(aFeature.getMaxStartOffset() <= feature.getStartOffset())) {
        throw new IllegalArgumentException(String.format(
            "Features overlap (<): %s with %s in %s", aFeature, feature, this));
      }
      if (aFeature.getStartOffset() >= feature.getStartOffset()
          && !(aFeature.getStartOffset() >= feature.getMaxStartOffset())) {
        throw new IllegalArgumentException(String.format(
            "Features overlap (>): %s with %s in %s", aFeature, feature, this));
      }
    }
    features.add(feature);
  }

  public void removeFeature(WallFeature feature) {
    features.remove(feature);
  }

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
    addFeature(new WallFeature() {
      private static final long serialVersionUID = 1L;
      {
        setStartOffset(.1d);
        setWidth(.8d);
      }

      public void editorRender() {
      }

      public void realRender() {
      }

      public String toString() {
        return String.format("(feature %.2f to %.2f)", getStartOffset(),
            getMaxStartOffset());
      }
    });
    addFeature(new WallFeature() {
      private static final long serialVersionUID = 1L;
      {
        setStartOffset(1d);
        setWidth(.8d);
      }

      public void editorRender() {
      }

      public void realRender() {
      }

      public String toString() {
        return String.format("(feature %.2f to %.2f)", getStartOffset(),
            getMaxStartOffset());
      }
    });
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

  ArrayList<Double> getSegments() {
    ArrayList<Double> segments = new ArrayList<Double>();
    for (WallFeature feature : features) {
      segments.add(feature.getStartOffset());
      segments.add(feature.getMaxStartOffset());
    }
    return segments;
  }

  void drawLine(double sx, double sy, double ex, double ey, double ox,
      double oy, double correction, ArrayList<Double> segments) {
    double slope = Math.atan2(ey - sy, ex - sx);
    double cos_slope = Math.cos(slope);
    double sin_slope = Math.sin(slope);
    double x = sx;
    double y = sy;
    ox = ox - correction * Corner.thickness * sin_slope;
    oy = oy + correction * Corner.thickness * cos_slope;
    double offset = Math.sqrt(sqr(ox - sx) + sqr(oy - sy));
    GL.glColor3d(1d, 0d, 0d);
    GL.glVertex2d(ox, oy);
    GL.glColor3d(0d, 0d, 0d);
    double dist = 0d;
    boolean odd = true;
    GL.glBegin(GL.GL_LINES);
    {
      for (Double new_dist : segments) {
        double ddist = new_dist - dist;
        dist = new_dist;
        double nx = x + ddist * cos_slope;
        double ny = y + ddist * sin_slope;
        if (odd) {
          GL.glVertex2d(x, y);
          GL.glVertex2d(nx, ny);
          odd = false;
        } else {
          odd = true;
          x = nx;
          y = ny;
        }
      }
      GL.glVertex2d(x, y);
      GL.glVertex2d(ex, ey);
    }
    GL.glEnd();
  }

  public void editorRender() {
    if (getStartingCorner() != null && getEndingCorner() != null) {
      double[] start_points = getStartingCorner().getStartLimitsOf(this);
      double[] end_points = getEndingCorner().getEndLimitsOf(this);
      double[] these_points = new double[] { getStartingCorner().getX(),
          getStartingCorner().getY(), getEndingCorner().getX(),
          getEndingCorner().getY() };
      int angle = slight_slope(these_points, these_points, 0, 2);
      int angle_1 = slight_slope(start_points, end_points, 0, 2);
      int angle_2 = slight_slope(start_points, end_points, 2, 0);
      ArrayList<Double> segments = getSegments();
      if (angle == angle_1) {
        GL.glColor3d(0d, 0d, 0d);
        drawLine(start_points[0], start_points[1], end_points[2],
            end_points[3], these_points[0], -1d, these_points[1], segments);
      }
      if (angle == angle_2) {
        GL.glColor3d(0d, 0d, 0d);
        GL.glColor3d(0d, 0d, 0d);
        drawLine(start_points[2], start_points[3], end_points[0],
            end_points[1], these_points[0], 1d, these_points[1], segments);
      }
      GL.glEnable(GL.GL_LINE_STIPPLE);
      {
        GL.glLineStipple(1, (short) 0x43f0);
        drawLine(these_points[0], these_points[1], these_points[2],
            these_points[3], these_points[0], 0d, these_points[1], segments);
      }
      GL.glDisable(GL.GL_LINE_STIPPLE);
    }
  }

  public static final double height = 3.5d;

  void realRenderRoutine(boolean fillMode) {
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
        GL.glVertex3d(start_points[0], start_points[1], 0d);
        GL.glVertex3d(start_points[0], start_points[1], height);
        GL.glVertex3d(end_points[2], end_points[3], height);
        GL.glVertex3d(end_points[2], end_points[3], 0d);
      }
      if (angle == angle_2) {
        GL.glVertex3d(start_points[2], start_points[3], 0d);
        GL.glVertex3d(start_points[2], start_points[3], height);
        GL.glVertex3d(end_points[0], end_points[1], height);
        GL.glVertex3d(end_points[0], end_points[1], 0d);
      }
    }
    GL.glEnd();
    if (angle == angle_1 && angle == angle_2) {
      if (fillMode) {
        GL.glBegin(GL.GL_TRIANGLES);
        {
          GL.glVertex3d(start_points[0], start_points[1], height);
          GL.glVertex3d(these_points[0], these_points[1], height);
          GL.glVertex3d(start_points[2], start_points[3], height);
          GL.glVertex3d(end_points[0], end_points[1], height);
          GL.glVertex3d(these_points[2], these_points[3], height);
          GL.glVertex3d(end_points[2], end_points[3], height);
        }
        GL.glEnd();
        GL.glBegin(GL.GL_QUADS);
        {
          GL.glVertex3d(start_points[0], start_points[1], height);
          GL.glVertex3d(start_points[2], start_points[3], height);
          GL.glVertex3d(end_points[0], end_points[1], height);
          GL.glVertex3d(end_points[2], end_points[3], height);
        }
        GL.glEnd();
      } else {
        GL.glBegin(GL.GL_LINES);
        {
          GL.glVertex3d(start_points[0], start_points[1], height);
          GL.glVertex3d(end_points[2], end_points[3], height);
          GL.glVertex3d(start_points[2], start_points[3], height);
          GL.glVertex3d(end_points[0], end_points[1], height);
        }
        GL.glEnd();
      }
    }
  }

  public void realRender() {
    if (getStartingCorner() != null && getEndingCorner() != null) {
      GL.glColor3d(1d, 1d, 1d);
      GL.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
      realRenderRoutine(true);
      GL.glColor3d(0d, 0d, 0d);
      GL.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
      realRenderRoutine(false);
    }
  }
}