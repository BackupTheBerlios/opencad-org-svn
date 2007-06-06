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
					&& !(aFeature.getMaxStartOffset() <= feature
							.getStartOffset())) {
				throw new IllegalArgumentException(String.format(
						"Features overlap (<): %s with %s in %s", aFeature,
						feature, this));
			}
			if (aFeature.getStartOffset() >= feature.getStartOffset()
					&& !(aFeature.getStartOffset() >= feature
							.getMaxStartOffset())) {
				throw new IllegalArgumentException(String.format(
						"Features overlap (>): %s with %s in %s", aFeature,
						feature, this));
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
				setStartOffset(2d);
				setWidth(.8d);
				setGroundOffset(1d);
				setHeight(1.6d);
			}

			public void editorRender() {
			}

			public void realRender() {
			}

			public String toString() {
				return String.format("(feature %.2f to %.2f)",
						getStartOffset(), getMaxStartOffset());
			}
		});
		addFeature(new WallFeature() {
			private static final long serialVersionUID = 1L;
			{
				setStartOffset(4d);
				setWidth(.8d);
				setGroundOffset(1d);
				setHeight(1.6d);
			}

			public void editorRender() {
			}

			public void realRender() {
			}

			public String toString() {
				return String.format("(feature %.2f to %.2f)",
						getStartOffset(), getMaxStartOffset());
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
		return (int) (Math.signum(a[ax + 1] - b[bx + 1]) + 3 * Math
				.signum(a[ax] - b[bx]));
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
			double[] these_points = new double[] { getStartingCorner().getX(),
					getStartingCorner().getY(), getEndingCorner().getX(),
					getEndingCorner().getY() };
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
			GL.glColor3d(0d, 0d, 0d);
			for (Double dist : segments) {
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
						// This means sx1 < px1 < ex1
						if (Math.signum(px1 - sx1) == Math.signum(dx1)
								&& enable1) {
							GL.glVertex2d(x1, y1);
							GL.glVertex2d(px1, py1);
						}
						// This means sx1 < px1 < ex1
						if (Math.signum(px2 - sx2) == Math.signum(dx2)
								&& enable2) {
							GL.glVertex2d(x2, y2);
							GL.glVertex2d(px2, py2);
						}
					}
					GL.glEnd();
				} else {
					odd = true;
					x = nx;
					y = ny;
					if (Math.signum(px1 - sx1) == Math.signum(dx1)) {
						x1 = px1;
						y1 = py1;
					}
					if (Math.signum(px2 - sx2) == Math.signum(dx2)) {
						x2 = px2;
						y2 = py2;
					}
				}
			}
			if (odd) {
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
	}

	public static final double height = 3.5d;

	void realRenderRoutine(boolean fillMode) {
		if (getStartingCorner() != null && getEndingCorner() != null) {
			double[] start_points = getStartingCorner().getStartLimitsOf(this);
			double[] end_points = getEndingCorner().getEndLimitsOf(this);
			double[] these_points = new double[] { getStartingCorner().getX(),
					getStartingCorner().getY(), getEndingCorner().getX(),
					getEndingCorner().getY() };
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
			double x1 = sx1;
			double y1 = sy1;
			double x2 = sx2;
			double y2 = sy2;
			boolean odd = true;
			for (Double dist : segments) {
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
				if (odd) {
					odd = false;
					GL.glBegin(GL.GL_QUADS);
					{
						// This means sx1 < px1 < ex1
						if (Math.signum(px1 - sx1) == Math.signum(dx1)
								&& enable1) {
							GL.glVertex3d(x1, y1, 0);
							GL.glVertex3d(px1, py1, 0);
							GL.glVertex3d(px1, py1, height);
							GL.glVertex3d(x1, y1, height);
						}
						// This means sx1 < px1 < ex1
						if (Math.signum(px2 - sx2) == Math.signum(dx2)
								&& enable2) {
							GL.glVertex3d(x2, y2, 0);
							GL.glVertex3d(px2, py2, 0);
							GL.glVertex3d(px2, py2, height);
							GL.glVertex3d(x2, y2, height);
						}
						if (Math.signum(px1 - sx1) == Math.signum(dx1)
								&& enable1
								&& Math.signum(px2 - sx2) == Math.signum(dx2)
								&& enable2) {
							GL.glVertex3d(px1, py1, height);
							GL.glVertex3d(x1, y1, height);
							GL.glVertex3d(x2, y2, height);
							GL.glVertex3d(px2, py2, height);
						}
					}
					GL.glEnd();
				} else {
					odd = true;
					if (Math.signum(px1 - sx1) == Math.signum(dx1)) {
						x1 = px1;
						y1 = py1;
					}
					if (Math.signum(px2 - sx2) == Math.signum(dx2)) {
						x2 = px2;
						y2 = py2;
					}
				}
			}
			if (odd) {
				GL.glBegin(GL.GL_QUADS);
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
			}
		}
	}

	// void realRenderRoutine(boolean fillMode) {
	// double[] start_points = getStartingCorner().getStartLimitsOf(this);
	// double[] end_points = getEndingCorner().getEndLimitsOf(this);
	// double[] these_points = new double[] { getStartingCorner().getX(),
	// getStartingCorner().getY(), getEndingCorner().getX(),
	// getEndingCorner().getY() };
	// int angle = slight_slope(these_points, these_points, 0, 2);
	// int angle_1 = slight_slope(start_points, end_points, 0, 2);
	// int angle_2 = slight_slope(start_points, end_points, 2, 0);
	// GL.glBegin(GL.GL_QUADS);
	// {
	// if (angle == angle_1) {
	// GL.glVertex3d(start_points[0], start_points[1], 0d);
	// GL.glVertex3d(start_points[0], start_points[1], height);
	// GL.glVertex3d(end_points[2], end_points[3], height);
	// GL.glVertex3d(end_points[2], end_points[3], 0d);
	// }
	// if (angle == angle_2) {
	// GL.glVertex3d(start_points[2], start_points[3], 0d);
	// GL.glVertex3d(start_points[2], start_points[3], height);
	// GL.glVertex3d(end_points[0], end_points[1], height);
	// GL.glVertex3d(end_points[0], end_points[1], 0d);
	// }
	// }
	// GL.glEnd();
	// if (angle == angle_1 && angle == angle_2) {
	// if (fillMode) {
	// GL.glBegin(GL.GL_TRIANGLES);
	// {
	// GL.glVertex3d(start_points[0], start_points[1], height);
	// GL.glVertex3d(these_points[0], these_points[1], height);
	// GL.glVertex3d(start_points[2], start_points[3], height);
	// GL.glVertex3d(end_points[0], end_points[1], height);
	// GL.glVertex3d(these_points[2], these_points[3], height);
	// GL.glVertex3d(end_points[2], end_points[3], height);
	// }
	// GL.glEnd();
	// GL.glBegin(GL.GL_QUADS);
	// {
	// GL.glVertex3d(start_points[0], start_points[1], height);
	// GL.glVertex3d(start_points[2], start_points[3], height);
	// GL.glVertex3d(end_points[0], end_points[1], height);
	// GL.glVertex3d(end_points[2], end_points[3], height);
	// }
	// GL.glEnd();
	// } else {
	// GL.glBegin(GL.GL_LINES);
	// {
	// GL.glVertex3d(start_points[0], start_points[1], height);
	// GL.glVertex3d(end_points[2], end_points[3], height);
	// GL.glVertex3d(start_points[2], start_points[3], height);
	// GL.glVertex3d(end_points[0], end_points[1], height);
	// }
	// GL.glEnd();
	// }
	// }
	// }

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