package org.opencad.modelling.walls;

import java.util.ArrayList;
import java.util.TreeSet;

import org.eclipse.opengl.GL;
import org.opencad.modelling.Model;
import org.opencad.modelling.Primitive;
import org.opencad.modelling.PrimitiveTypeRegister;
import org.opencad.modelling.corners.Corner;
import org.opencad.modelling.walls.features.WallFeature;
import org.opencad.ui.editor.Deletable;
import org.opencad.ui.editor.GLEditor;
import org.opencad.ui.editor.GLEditorState;
import org.opencad.ui.editor.Outlineable;
import org.opencad.ui.editor.RenderStage;

public class Wall extends Primitive implements Outlineable, Deletable {
	private static final long serialVersionUID = -8662848078904155699L;
	static {
		PrimitiveTypeRegister.registerPrimitiveType(Wall.class);
	}

	boolean selected, hover;

	Corner startingCorner, endingCorner;

	private TreeSet<WallFeature> features = new TreeSet<WallFeature>();

	public boolean addFeature(WallFeature feature) {
		for (WallFeature aFeature : features) {
			if (aFeature.getStartOffset() <= feature.getStartOffset() && !(aFeature.getMaxStartOffset() <= feature.getStartOffset())) {
				return false;
			}
			if (aFeature.getStartOffset() >= feature.getStartOffset() && !(aFeature.getStartOffset() >= feature.getMaxStartOffset())) {
				return false;
			}
		}
		feature.setWall(this);
		return features.add(feature);
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
	}

	public String toString() {
		return String.format("(wall %s %s)", startingCorner, endingCorner);
	}

	public static double sqr(double a) {
		return a * a;
	}

	int slight_slope(double[] a, double[] b, int ax, int bx) {
		return (int) (Math.signum(a[ax + 1] - b[bx + 1]) + 3 * Math.signum(a[ax] - b[bx]));
	}

	ArrayList<Double> getSegments() {
		ArrayList<Double> segments = new ArrayList<Double>();
		for (WallFeature feature : features) {
			segments.add(feature.getStartOffset());
			segments.add(feature.getMaxStartOffset());
		}
		return segments;
	}

	public void editorRender() {
		if (getStartingCorner() != null && getEndingCorner() != null) {
			double[] start_points = getStartingCorner().getStartLimitsOf(this);
			double[] end_points = getEndingCorner().getEndLimitsOf(this);
			double[] these_points = new double[] { getStartingCorner().getX(), getStartingCorner().getY(), getEndingCorner().getX(), getEndingCorner().getY() };
			int angle = slight_slope(these_points, these_points, 0, 2);
			int angle_1 = slight_slope(start_points, end_points, 0, 2);
			int angle_2 = slight_slope(start_points, end_points, 2, 0);
			boolean enable1 = angle == angle_1;
			boolean enable2 = angle == angle_2;
			ArrayList<Double> segments = getSegments();
			GL.glLineStipple(1, (short) 0x43f0);
			double sx = these_points[0];
			double sy = these_points[1];
			double ex = these_points[2];
			double ey = these_points[3];
			double sx1 = start_points[0];
			double sy1 = start_points[1];
			double ex1 = end_points[2];
			double ey1 = end_points[3];
			double sx2 = start_points[2];
			double sy2 = start_points[3];
			double ex2 = end_points[0];
			double ey2 = end_points[1];
			double len = Math.sqrt(sqr(ex - sx) + sqr(ey - sy));
			double sqlen1 = sqr(ex1 - sx1) + sqr(ey1 - sy1);
			double sqlen2 = sqr(ex2 - sx2) + sqr(ey2 - sy2);
			double dx1 = ex1 - sx1;
			double dy1 = ey1 - sy1;
			double dx2 = ex2 - sx2;
			double dy2 = ey2 - sy2;
			double x = sx;
			double y = sy;
			double x1 = sx1;
			double y1 = sy1;
			double x2 = sx2;
			double y2 = sy2;
			boolean odd = true;
			double rotation = 180 + Math.atan2(ey - sy, ex - sx) * 180 / Math.PI;
			int segment = 0;
			boolean old_feature = true;
			if (isSelected()) {
				GL.glColor3d(0d, 0.5d, 1d);
			} else if (isHover()) {
				GL.glColor3d(1d, 0d, 0d);
			} else {
				GL.glColor3d(0d, 0d, 0d);
			}
			for (Double dist : segments) {

				segment++;
				if (dist < 0) {
					odd = !odd;
					continue;
				}
				if (dist > len) {
					break;
				}
				double f = dist / len;
				double fi = 1 - f;
				double nx = sx * fi + ex * f;
				double ny = sy * fi + ey * f;
				double u1 = ((nx - sx1) * dx1 + (ny - sy1) * dy1) / sqlen1;
				double u2 = ((nx - sx2) * dx2 + (ny - sy2) * dy2) / sqlen2;
				double px1 = sx1 + u1 * dx1;
				double py1 = sy1 + u1 * dy1;
				double px2 = sx2 + u2 * dx2;
				double py2 = sy2 + u2 * dy2;
				boolean feature_enabled = (sx1 < px1 && px1 < ex1 || sx1 > px1 && px1 > ex1) && (sx2 < px2 && px2 < ex2 || sx2 > px2 && px2 > ex2);
				if (!feature_enabled) {
					old_feature = false;
					odd = !odd;
					continue;
				}
				if (odd) {
					odd = false;
					GL.glEnable(GL.GL_LINE_STIPPLE);
					GL.glBegin(GL.GL_LINES);
					{
						GL.glVertex2d(x, y);
						GL.glVertex2d(nx, ny);
					}
					GL.glEnd();
					GL.glDisable(GL.GL_LINE_STIPPLE);
					GL.glBegin(GL.GL_LINES);
					{
						if (enable1) {
							GL.glVertex2d(x1, y1);
							GL.glVertex2d(px1, py1);
						}
						if (enable2) {
							GL.glVertex2d(x2, y2);
							GL.glVertex2d(px2, py2);
						}
					}
					GL.glEnd();
				} else {
					odd = true;
					if (old_feature) {
						x = nx;
						y = ny;
						if (enable1) {
							x1 = px1;
							y1 = py1;
						}
						if (enable2) {
							x2 = px2;
							y2 = py2;
						}
						GL.glPushMatrix();
						GL.glPushAttrib(GL.GL_CURRENT_BIT);
						{
							GL.glTranslated(nx, ny, 0);
							GL.glRotated(rotation, 0d, 0d, 1d);
							findFeature((segment - 1) / 2).editorRender();
						}
						GL.glPopAttrib();
						GL.glPopMatrix();
					}
				}
				old_feature = true;
			}
			GL.glEnable(GL.GL_LINE_STIPPLE);
			GL.glBegin(GL.GL_LINES);
			{
				GL.glVertex2d(x, y);
				GL.glVertex2d(ex, ey);
			}
			GL.glEnd();
			GL.glDisable(GL.GL_LINE_STIPPLE);
			GL.glBegin(GL.GL_LINES);
			{
				if (enable1) {
					GL.glVertex2d(x1, y1);
					GL.glVertex2d(ex1, ey1);
				}
				if (enable2) {
					GL.glVertex2d(x2, y2);
					GL.glVertex2d(ex2, ey2);
				}
			}
			GL.glEnd();
		}
	}

	private WallFeature findFeature(int i) {
		return (WallFeature) features.toArray()[i];
	}

	public static final double height = 3.5d;

	public static void getColor(RenderStage stage) {
		switch (stage) {
		case FILL:
			GL.glColor3d(1d, 1d, 0.95d);
			break;
		case WIRE:
			GL.glColor3d(0.4d, 0.4d, 0.35d);
			break;
		}
	}

	public void realRender(RenderStage stage) {
		if (getStartingCorner() != null && getEndingCorner() != null) {
			double[] start_points = getStartingCorner().getStartLimitsOf(this);
			double[] end_points = getEndingCorner().getEndLimitsOf(this);
			double[] these_points = new double[] { getStartingCorner().getX(), getStartingCorner().getY(), getEndingCorner().getX(), getEndingCorner().getY() };
			int angle = slight_slope(these_points, these_points, 0, 2);
			int angle_1 = slight_slope(start_points, end_points, 0, 2);
			int angle_2 = slight_slope(start_points, end_points, 2, 0);
			boolean enable1 = angle == angle_1;
			boolean enable2 = angle == angle_2;
			ArrayList<Double> segments = getSegments();
			double sx = these_points[0];
			double sy = these_points[1];
			double ex = these_points[2];
			double ey = these_points[3];
			double sx1 = start_points[0];
			double sy1 = start_points[1];
			double ex1 = end_points[2];
			double ey1 = end_points[3];
			double sx2 = start_points[2];
			double sy2 = start_points[3];
			double ex2 = end_points[0];
			double ey2 = end_points[1];
			double len = Math.sqrt(sqr(ex - sx) + sqr(ey - sy));
			double dx1 = ex1 - sx1;
			double dy1 = ey1 - sy1;
			double dx2 = ex2 - sx2;
			double dy2 = ey2 - sy2;
			double sqlen1 = sqr(dx1) + sqr(dy1);
			double sqlen2 = sqr(dx2) + sqr(dy2);
			double x1 = sx1;
			double y1 = sy1;
			double x2 = sx2;
			double y2 = sy2;
			boolean odd = true;
			boolean old_feature = true;
			double rotation = 180 + Math.atan2(ey - sy, ex - sx) * 180 / Math.PI;
			int segment = 0;
			getColor(stage);
			for (Double dist : segments) {
				segment++;
				if (dist < 0) {
					odd = !odd;
					continue;
				}
				if (dist > len) {
					break;
				}
				double f = dist / len;
				double fi = 1 - f;
				double nx = sx * fi + ex * f;
				double ny = sy * fi + ey * f;
				double u1 = ((nx - sx1) * dx1 + (ny - sy1) * dy1) / sqlen1;
				double u2 = ((nx - sx2) * dx2 + (ny - sy2) * dy2) / sqlen2;
				double px1 = sx1 + u1 * dx1;
				double py1 = sy1 + u1 * dy1;
				double px2 = sx2 + u2 * dx2;
				double py2 = sy2 + u2 * dy2;
				boolean feature_enabled = (sx1 < px1 && px1 < ex1 || sx1 > px1 && px1 > ex1) && (sx2 < px2 && px2 < ex2 || sx2 > px2 && px2 > ex2);
				if (!feature_enabled) {
					old_feature = false;
					odd = !odd;
					continue;
				}
				if (odd) {
					odd = false;
					switch (stage) {
					case FILL: {
						GL.glBegin(GL.GL_QUADS);
					}
						break;
					case WIRE: {
						GL.glBegin(GL.GL_LINES);
					}
						break;
					}
					{
						if (enable1) {
							GL.glVertex3d(x1, y1, 0);
							GL.glVertex3d(px1, py1, 0);
							GL.glVertex3d(px1, py1, height);
							GL.glVertex3d(x1, y1, height);
						}
						if (enable2) {
							GL.glVertex3d(x2, y2, 0);
							GL.glVertex3d(px2, py2, 0);
							GL.glVertex3d(px2, py2, height);
							GL.glVertex3d(x2, y2, height);
						}
						if (enable1 && enable2) {
							GL.glVertex3d(px1, py1, height);
							GL.glVertex3d(x1, y1, height);
							GL.glVertex3d(x2, y2, height);
							GL.glVertex3d(px2, py2, height);
						}
					}
					GL.glEnd();
				} else {
					odd = true;
					if (old_feature) {
						if (enable1) {
							x1 = px1;
							y1 = py1;
						}
						if (enable2) {
							x2 = px2;
							y2 = py2;
						}
						double[] colors = new double[4];
						GL.glGetDoublev(GL.GL_CURRENT_COLOR, colors);
						GL.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
						GL.glPushMatrix();
						{
							GL.glTranslated(nx, ny, 0);
							GL.glRotated(rotation, 0d, 0d, 1d);
							findFeature((segment - 1) / 2).realRender(stage);
						}
						GL.glPopMatrix();
						GL.glPopAttrib();
						GL.glColor3dv(colors);
					}
				}
				old_feature = true;
			}
			switch (stage) {
			case FILL: {
				GL.glBegin(GL.GL_QUADS);
			}
				break;
			case WIRE: {
				GL.glBegin(GL.GL_LINES);
			}
				break;
			}
			{
				if (enable1) {
					GL.glVertex3d(x1, y1, 0);
					GL.glVertex3d(ex1, ey1, 0);
					GL.glVertex3d(ex1, ey1, height);
					GL.glVertex3d(x1, y1, height);
				}
				if (enable2) {
					GL.glVertex3d(x2, y2, 0);
					GL.glVertex3d(ex2, ey2, 0);
					GL.glVertex3d(ex2, ey2, height);
					GL.glVertex3d(x2, y2, height);
				}
				if (enable1 && enable2) {
					GL.glVertex3d(ex1, ey1, height);
					GL.glVertex3d(x1, y1, height);
					GL.glVertex3d(x2, y2, height);
					GL.glVertex3d(ex2, ey2, height);
				}
			}
			GL.glEnd();
			switch (stage) {
			case FILL: {
				GL.glBegin(GL.GL_TRIANGLES);
				{
					GL.glVertex3d(sx1, sy1, height);
					GL.glVertex3d(sx2, sy2, height);
					GL.glVertex3d(sx, sy, height);
					GL.glVertex3d(ex1, ey1, height);
					GL.glVertex3d(ex2, ey2, height);
					GL.glVertex3d(ex, ey, height);
				}
				GL.glEnd();
			}
				break;
			case WIRE: {
				GL.glBegin(GL.GL_LINES);
				{
					GL.glVertex3d(sx1, sy1, height);
					GL.glVertex3d(sx1, sy1, 0);
					GL.glVertex3d(sx2, sy2, height);
					GL.glVertex3d(sx2, sy2, 0);
					GL.glVertex3d(ex1, ey1, height);
					GL.glVertex3d(ex1, ey1, 0);
					GL.glVertex3d(ex2, ey2, height);
					GL.glVertex3d(ex2, ey2, 0);
				}
				GL.glEnd();
			}
			}
		}
	}

	double[] getIntersectionOf(double x, double y, double... line) {
		double dx = line[2] - line[0];
		double dy = line[3] - line[1];
		double sqlen = sqr(dx) + sqr(dy);
		double u = ((x - line[0]) * dx + (y - line[1]) * dy) / sqlen;
		double[] ret = new double[] { line[0] + u * dx, line[1] + u * dy, u };
		return ret;
	}

	public double[] getProjectionOf(double x, double y) {
		double[] intersection = getIntersectionOf(x, y, getStartingCorner().getX(), getStartingCorner().getY(), getEndingCorner().getX(), getEndingCorner().getY());
		if (intersection[2] > 0 && intersection[2] < 1) {
			return intersection;
		}
		return null;
	}

	public boolean isHoverCoordinates(double x, double y) {
		double[] prj = getProjectionOf(x, y);
		if (prj != null) {
			return sqr(x - prj[0]) + sqr(y - prj[1]) < sqr(Corner.thickness);
		}
		return false;
	}

	public GLEditorState getSelectionState(GLEditor editor) {
		return null;
	}

	public int getZIndex() {
		return -100;
	}

	public double getLength() {
		return Math.sqrt(sqr(getEndingCorner().getX() - getStartingCorner().getX()) + sqr(getEndingCorner().getY() - getStartingCorner().getY()));
	}

	public Object[] getChildren() {
		return features.toArray();
	}

	public String getText() {
		return "Wall";
	}

	public boolean hasChildren() {
		return features.size() > 0;
	}

	public String getImage() {
		return "icons/wall.gif";
	}

	public void delete(Model model) {
		model.removePrimitive(this);
		for (WallFeature feature : features) {
			feature.delete(model);
		}
		startingCorner.removeStart(this);
		endingCorner.removeEnd(this);
		startingCorner.delete(model);
		endingCorner.delete(model);
	}
}