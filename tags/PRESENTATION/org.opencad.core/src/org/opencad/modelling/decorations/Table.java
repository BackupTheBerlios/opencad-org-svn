package org.opencad.modelling.decorations;

import org.eclipse.opengl.GL;
import org.opencad.ui.editor.RenderStage;

public class Table extends Decoration {

	private static final long serialVersionUID = 1L;

	private static final double width = 2d;

	private static final double length = 1d;

	private static final double innerSide = 0.2d;

	private static final double baseHeight = 0.2d;

	private static final double legHeight = 0.5d;

	public String getImage() {
		return "icons/table.gif";
	}

	public void editorRender() {
		if (isSelected()) {
			GL.glColor3d(0d, 0.5d, 1d);
		} else if (isHover()) {
			GL.glColor3d(1d, 0d, 0d);
		} else {
			GL.glColor3d(0d, 0d, 0d);
		}
		GL.glTranslated(x, y, 0);
		GL.glRotated(getRotation(), 0, 0, 1d);
		GL.glBegin(GL.GL_QUADS);
		{
			GL.glVertex2d(0, 0);
			GL.glVertex2d(width, 0);
			GL.glVertex2d(width, length);
			GL.glVertex2d(0, length);
		}
		GL.glEnd();
	}

	public void drawStrip() {
		GL.glBegin(GL.GL_QUAD_STRIP);
		{
			GL.glVertex3d(0, 0, 0);
			GL.glVertex3d(0, 0, baseHeight);
			GL.glVertex3d(width, 0, 0);
			GL.glVertex3d(width, 0, baseHeight);
			GL.glVertex3d(width, length, 0);
			GL.glVertex3d(width, length, baseHeight);
			GL.glVertex3d(0, length, 0);
			GL.glVertex3d(0, length, baseHeight);
			GL.glVertex3d(0, 0, 0);
			GL.glVertex3d(0, 0, baseHeight);
		}
		GL.glEnd();
		GL.glTranslated(innerSide, innerSide, 0);
		double sw = width - 2 * innerSide;
		double sl = length - 2 * innerSide;
		GL.glBegin(GL.GL_QUAD_STRIP);
		{
			GL.glVertex3d(0, 0, 0);
			GL.glVertex3d(0, 0, baseHeight);
			GL.glVertex3d(sw, 0, 0);
			GL.glVertex3d(sw, 0, baseHeight);
			GL.glVertex3d(sw, sl, 0);
			GL.glVertex3d(sw, sl, baseHeight);
			GL.glVertex3d(0, sl, 0);
			GL.glVertex3d(0, sl, baseHeight);
			GL.glVertex3d(0, 0, 0);
			GL.glVertex3d(0, 0, baseHeight);
		}
		GL.glEnd();
	}

	public void drawBase() {
		double li = length - innerSide;
		double wi = width - innerSide;
		GL.glBegin(GL.GL_QUADS);
		{
			GL.glVertex2d(0, 0);
			GL.glVertex2d(width, 0);
			GL.glVertex2d(width, innerSide);
			GL.glVertex2d(0, innerSide);

			GL.glVertex2d(0, innerSide);
			GL.glVertex2d(innerSide, innerSide);
			GL.glVertex2d(innerSide, li);
			GL.glVertex2d(0, li);

			GL.glVertex2d(0, li);
			GL.glVertex2d(width, li);
			GL.glVertex2d(width, length);
			GL.glVertex2d(0, length);

			GL.glVertex2d(width, innerSide);
			GL.glVertex2d(wi, innerSide);
			GL.glVertex2d(wi, li);
			GL.glVertex2d(width, li);

			GL.glVertex2d(0, 0);
			GL.glVertex2d(width, 0);
			GL.glVertex2d(width, innerSide);
			GL.glVertex2d(0, innerSide);

			GL.glVertex2d(0, innerSide);
			GL.glVertex2d(innerSide, innerSide);
			GL.glVertex2d(innerSide, li);
			GL.glVertex2d(0, li);

			GL.glVertex2d(0, li);
			GL.glVertex2d(width, li);
			GL.glVertex2d(width, length);
			GL.glVertex2d(0, length);

			GL.glVertex2d(width, innerSide);
			GL.glVertex2d(wi, innerSide);
			GL.glVertex2d(wi, li);
			GL.glVertex2d(width, li);
		}
		GL.glEnd();
	}

	public void drawLeg() {
		double sw = innerSide;
		double sl = innerSide;
		GL.glBegin(GL.GL_QUAD_STRIP);
		{
			GL.glVertex3d(0, 0, 0);
			GL.glVertex3d(0, 0, legHeight);
			GL.glVertex3d(sw, 0, 0);
			GL.glVertex3d(sw, 0, legHeight);
			GL.glVertex3d(sw, sl, 0);
			GL.glVertex3d(sw, sl, legHeight);
			GL.glVertex3d(0, sl, 0);
			GL.glVertex3d(0, sl, legHeight);
			GL.glVertex3d(0, 0, 0);
			GL.glVertex3d(0, 0, legHeight);
		}
		GL.glEnd();
	}

	public static void getColor(RenderStage stage) {
		switch (stage) {
		case FILL:
			GL.glColor3d(0.6d, 0.3d, 0.0d);
			break;
		case WIRE:
			GL.glColor3d(0.3d, 0.2d, 0d);
			break;
		case ALPHA:
			GL.glColor4d(0.7d, 0.4d, 0.1d, 0.8d);
			break;
		}
	}

	public void realRender(RenderStage stage) {
		getColor(stage);
		GL.glTranslated(x, y, 0);
		GL.glRotated(getRotation(), 0, 0, 1d);
		switch (stage) {
		case FILL:
		case WIRE:
			drawLeg();
			GL.glPushMatrix();
			{
				GL.glTranslated(width - innerSide, 0, 0);
				drawLeg();
				GL.glTranslated(0, length - innerSide, 0);
				drawLeg();
				GL.glTranslated(-width + innerSide, 0, 0);
				drawLeg();
			}
			GL.glPopMatrix();
			GL.glTranslated(0, 0, legHeight);
			drawBase();
			GL.glPushMatrix();
			{
				drawStrip();
			}
			GL.glPopMatrix();
			GL.glTranslated(0, 0, baseHeight);
			drawBase();
			break;
		case ALPHA:
			GL.glTranslated(innerSide, innerSide, baseHeight
					+ legHeight);
			GL.glBegin(GL.GL_QUADS);
			{
				GL.glVertex2d(0, 0);
				GL.glVertex2d(0, length - 2 * innerSide);
				GL.glVertex2d(width - 2 * innerSide, length - 2
						* innerSide);
				GL.glVertex2d(width - 2 * innerSide, 0);
			}
			GL.glEnd();
		}
	}

	public boolean isHoverCoordinates(double x, double y) {
		double[] rot = correctRotation(x, y);
		x = rot[0];
		y = rot[1];
		return x > this.x && this.x + width > x && y > this.y
				&& this.y + length > y;
	}

}
