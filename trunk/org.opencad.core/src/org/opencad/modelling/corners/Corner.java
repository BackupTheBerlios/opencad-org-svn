package org.opencad.modelling.corners;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.opengl.GL;
import org.opencad.modelling.Primitive;
import org.opencad.modelling.PrimitiveTypeRegister;
import org.opencad.modelling.walls.Wall;
import org.opencad.ui.editor.GLEditor;
import org.opencad.ui.editor.GLEditorState;
import org.opencad.ui.editor.Outlineable;
import org.opencad.ui.editor.RenderStage;

public class Corner extends Primitive implements Outlineable {
	private static final long serialVersionUID = -1332083715502519329L;
	static {
		PrimitiveTypeRegister.registerPrimitiveType(Corner.class);
	}

	private Double x, y;

	public static double thickness = 0.1d;

	private static double hoverSlack = 2 * thickness;

	private HashSet<Wall> startOf = new HashSet<Wall>();

	private HashSet<Wall> endOf = new HashSet<Wall>();

	public void addStart(Wall start) {
		startOf.add(start);
	}

	public void removeStart(Wall start) {
		startOf.remove(start);
	}

	public void addEnd(Wall start) {
		endOf.add(start);
	}

	public void removeEnd(Wall start) {
		endOf.remove(start);
	}

	public final Double getX() {
		return x;
	}

	public final void setX(Double x) {
		this.x = x;
	}

	public final Double getY() {
		return y;
	}

	public final void setY(Double y) {
		this.y = y;
	}

	public Corner(Double x, Double y) {
		this.x = x;
		this.y = y;
	}

	public String toString() {
		return String.format("(corner %.2f:%.2f)", x, y);
	}

	public boolean isHoverCoordinates(double x, double y) {
		return Math.abs(this.x - x) < hoverSlack && Math.abs(this.y - y) < hoverSlack;
	}

	public GLEditorState getSelectionState(GLEditor editor) {
		return new SelectCornerState(editor, this);
	}

	Double posAngle(double angle) {
		return angle < 0d ? Math.PI * 2 + angle : angle;
	}

	public Double angleStartOf(Wall wall) {
		if (wall.getEndingCorner() != null) {
			return posAngle(Math.atan2(wall.getEndingCorner().getY() - y, wall.getEndingCorner().getX() - x));
		}
		return 0d;
	}

	public Double angleEndOf(Wall wall) {
		if (wall.getStartingCorner() != null) {
			return posAngle(Math.atan2(wall.getStartingCorner().getY() - y, wall.getStartingCorner().getX() - x));
		}
		return 0d;
	}

	public TreeMap<Double, Wall> getWalls() {
		TreeMap<Double, Wall> walls = new TreeMap<Double, Wall>();
		for (Wall wall : startOf) {
			walls.put(angleStartOf(wall), wall);
		}
		for (Wall wall : endOf) {
			walls.put(angleEndOf(wall), wall);
		}
		return walls;
	}

	public double[] getStartLimitsOf(Wall wall) {
		Double angle = angleStartOf(wall);
		return getLimitsOf(wall, angle);
	}

	public double[] getEndLimitsOf(Wall wall) {
		Double angle = angleEndOf(wall);
		return getLimitsOf(wall, angle);
	}

	public double[] getLimitsOf(Wall wall, Double angle) {
		TreeMap<Double, Wall> walls = getWalls();
		double[] points = new double[4];
		Double oldAngle;
		if (angle.equals(walls.firstKey())) {
			oldAngle = walls.lastKey();
		} else {
			oldAngle = walls.headMap(angle).lastKey();
		}
		double avg;
		double len;
		if (oldAngle.equals(angle)) {
			avg = angle - Math.PI / 2;
			len = thickness;
		} else {
			avg = (angle + oldAngle) / 2;
			len = thickness / Math.sin(angle - avg);
		}
		points[0] = x + Math.cos(avg) * len;
		points[1] = y + Math.sin(avg) * len;
		if (angle.equals(walls.lastKey())) {
			oldAngle = walls.firstKey();
		} else {
			SortedMap<Double, Wall> larger = walls.tailMap(angle);
			Set<Double> angles = larger.keySet();
			for (Double largerAngle : angles) {
				if (largerAngle > angle) {
					oldAngle = largerAngle;
					break;
				}
			}
		}
		if (oldAngle.equals(angle)) {
			avg = angle + Math.PI / 2;
			len = thickness;
		} else {
			avg = (angle + oldAngle) / 2;
			len = thickness / Math.sin(oldAngle - avg);
		}
		points[2] = x + Math.cos(avg) * len;
		points[3] = y + Math.sin(avg) * len;
		return points;
	}

	public void editorRender() {
		double selectionRadius = hoverSlack;
		double x = getX();
		double y = getY();
		if (isHover()) {
			GL.glColor3d(1d, 0d, 0d);
		} else if (isSelected()) {
			GL.glColor3d(0d, 0.5d, 0d);
		} else {
			GL.glColor3d(0d, 0d, 0d);
		}
		if (isSelected() || isHover()) {
			GL.glEnable(GL.GL_LINE_STIPPLE);
			{
				GL.glLineStipple(1, (short) 0xAAAA);
				GL.glBegin(GL.GL_LINES);
				{
					GL.glVertex2d(x - selectionRadius, y - selectionRadius);
					GL.glVertex2d(x - selectionRadius, y + selectionRadius);
					GL.glVertex2d(x + selectionRadius, y - selectionRadius);
					GL.glVertex2d(x + selectionRadius, y + selectionRadius);
					GL.glVertex2d(x - selectionRadius, y - selectionRadius);
					GL.glVertex2d(x + selectionRadius, y - selectionRadius);
					GL.glVertex2d(x - selectionRadius, y + selectionRadius);
					GL.glVertex2d(x + selectionRadius, y + selectionRadius);
				}
				GL.glEnd();
			}
			GL.glDisable(GL.GL_LINE_STIPPLE);
		}
		TreeMap<Double, Wall> walls = getWalls();
		if (walls.size() == 1) {
			Wall wall = walls.get(walls.firstKey());
			double[] lim;
			if (startOf.contains(wall)) {
				lim = getStartLimitsOf(wall);
			} else if (endOf.contains(wall)) {
				lim = getEndLimitsOf(wall);
			} else {
				throw new IllegalStateException();
			}
			GL.glBegin(GL.GL_LINES);
			{
				GL.glVertex2d(lim[0], lim[1]);
				GL.glVertex2d(lim[2], lim[3]);
			}
			GL.glEnd();
		}
	}

	public void realRender(RenderStage stage) {
		Wall.getColor(stage);
		if (stage == RenderStage.ALPHA) return;
		TreeMap<Double, Wall> walls = getWalls();
		if (walls.size() == 1) {
			Wall wall = walls.get(walls.firstKey());
			double[] lim;
			if (startOf.contains(wall)) {
				lim = getStartLimitsOf(wall);
			} else if (endOf.contains(wall)) {
				lim = getEndLimitsOf(wall);
			} else {
				throw new IllegalStateException();
			}
			switch (stage) {
			case FILL: {
				GL.glBegin(GL.GL_QUADS);
				{
					GL.glVertex3d(lim[0], lim[1], 0);
					GL.glVertex3d(lim[2], lim[3], 0);
					GL.glVertex3d(lim[2], lim[3], Wall.height);
					GL.glVertex3d(lim[0], lim[1], Wall.height);
				}
				GL.glEnd();
			}
				break;
			case WIRE: {
				GL.glBegin(GL.GL_LINES);
				{
					GL.glVertex3d(lim[0], lim[1], 0);
					GL.glVertex3d(lim[2], lim[3], 0);
					GL.glVertex3d(lim[0], lim[1], Wall.height);
					GL.glVertex3d(lim[2], lim[3], Wall.height);
				}
				GL.glEnd();
			}
			}
		}
	}

	public int getZIndex() {
		return 0;
	}

	public Object[] getChildren() {
		return getWalls().values().toArray();
	}

	public String getText() {
		return String.format("Corner @%.2f:%.2f", x, y);
	}

	public boolean hasChildren() {
		return true;
	}

	public String getImage() {
		return "icons/corner.gif";
	}
}