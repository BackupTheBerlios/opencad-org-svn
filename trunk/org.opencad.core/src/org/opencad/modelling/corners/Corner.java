package org.opencad.modelling.corners;

import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeMap;

import org.eclipse.opengl.GL;
import org.opencad.modelling.Primitive;
import org.opencad.modelling.PrimitiveTypeRegister;
import org.opencad.modelling.walls.Wall;
import org.opencad.ui.editor.GLEditor;
import org.opencad.ui.editor.GLEditorState;
import org.opencad.ui.editor.Hoverable;
import org.opencad.ui.editor.Selectable;

public class Corner extends Primitive implements Hoverable, Selectable {

	private static final long serialVersionUID = -1332083715502519329L;

	static {
		PrimitiveTypeRegister.registerPrimitiveType(Corner.class);
	}

	private Double x, y;

	private transient boolean hover;

	private transient boolean selected;

	private static double thickness = 0.1d;

	private static double hoverSlack = 0.06d;

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
		return Math.abs(this.x - x) < hoverSlack
				&& Math.abs(this.y - y) < hoverSlack;
	}

	public boolean setHover(boolean hover) {
		boolean changed = this.hover != hover;
		this.hover = hover;
		return changed;
	}

	public boolean isHover() {
		return hover;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public GLEditorState getSelectionState(GLEditor editor) {
		return new SelectCornerState(editor, this);
	}
	
	Double posAngle(double angle) {
		return angle < 0d ? Math.PI * 2 + angle : angle;
	}

	public Double angleStartOf(Wall wall) {
		return posAngle(Math.atan2(wall.getEndingCorner().getY() - y, wall
				.getEndingCorner().getX()
				- x));
	}

	public Double angleEndOf(Wall wall) {
		return posAngle(Math.atan2(wall.getStartingCorner().getY() - y, wall
				.getStartingCorner().getX()
				- x));
	}

	public void editorRender() {
		double selectionRadius = 0.06d;

		double x = getX();
		double y = getY();
		if (isHover()) {
			GL.glColor3d(1d, 0d, 0d);
		} else if (isSelected()) {
			GL.glColor3d(0d, 0.5d, 0d);
		} else {
			GL.glColor3d(0d, 0d, 0d);
		}

		TreeMap<Double, Wall> walls = new TreeMap<Double, Wall>();
		for (Wall wall : startOf) {
			walls.put(angleStartOf(wall), wall);
		}
		for (Wall wall : endOf) {
			walls.put(angleEndOf(wall), wall);
		}
		Double oldAngle = null;
		Double firstAngle = null;
		GL.glBegin(GL.GL_LINES);
		{
			for (Double angle : walls.keySet()) {
				if (oldAngle != null) {
					double avg = (angle + oldAngle) / 2;
					double ox = x + Math.cos(avg) * thickness;
					double oy = y + Math.sin(avg) * thickness;
					GL.glVertex2d(x, y);
					GL.glVertex2d(ox, oy);
				} else {
					firstAngle = angle;
				}
				oldAngle = angle;
			}
			if (firstAngle != oldAngle) {
				firstAngle += Math.PI * 2;
				double avg = (firstAngle + oldAngle) / 2;
				double ox = x + Math.cos(avg) * thickness;
				double oy = y + Math.sin(avg) * thickness;
				GL.glVertex2d(x, y);
				GL.glVertex2d(ox, oy);
			}
		}
		GL.glEnd();

		if (isSelected()) {
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
	}

	public void realRender() {
		GL.glColor3d(0d, 0d, 0d);
		GL.glBegin(GL.GL_LINES);
		{
			GL.glVertex3d(x, y, 0);
			GL.glVertex3d(x, y, Wall.height);
		}
		GL.glEnd();
	}
}