package org.opencad.modelling.corners;

import org.eclipse.opengl.GL;
import org.opencad.modelling.Primitive;
import org.opencad.modelling.PrimitiveTypeRegister;
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

	private static double hoverSlack = 0.06d;

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
		return String.format("%.2f:%.2f", x, y);
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

	public void editorRender() {
		double outerRadius = 0.04d;
		double innerRadius = 0.02d;
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
		}
		GL.glEnd();
		GL.glBegin(GL.GL_POLYGON);
		{
			GL.glVertex2d(x - innerRadius, y);
			GL.glVertex2d(x, y - innerRadius);
			GL.glVertex2d(x + innerRadius, y);
			GL.glVertex2d(x, y + innerRadius);
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
}